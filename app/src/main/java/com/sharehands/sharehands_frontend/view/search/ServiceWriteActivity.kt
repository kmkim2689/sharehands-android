package com.sharehands.sharehands_frontend.view.search

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.sharehands.sharehands_frontend.BuildConfig
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivityServiceWriteBinding
import com.sharehands.sharehands_frontend.viewmodel.search.ServiceUploadViewModel
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ServiceWriteActivity: AppCompatActivity() {
    // 카메라 허용, 갤러리 허용, 이미지 URL 코드
    private val CAMERA_REQUEST_CODE = 101
    private val GALLERY_REQUEST_CODE = 201
    private var imageUri: Uri? = null
    private var imageUriString: String = ""
    private lateinit var viewModel: ServiceUploadViewModel
    // 이미지 url을 담는 리스트
    private var imageArrayList = ArrayList<String>()

    private lateinit var binding: ActivityServiceWriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_write)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_service_write)
        viewModel = ViewModelProvider(this).get(ServiceUploadViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // 이미지의 경로를 선택하는 Dialog
        val serviceImageDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_get_picture, null)
        val serviceImageDialogBuilder = AlertDialog.Builder(this).setView(serviceImageDialogView)
        val serviceImageDialog = serviceImageDialogBuilder.create()

        // Image Dialog Buttons
        val exitImageBtn = serviceImageDialogView.findViewById<ImageView>(R.id.iv_dialog_image_exit)
        val selectCamera = serviceImageDialogView.findViewById<LinearLayout>(R.id.layout_select_camera)
        val selectGallery = serviceImageDialogView.findViewById<LinearLayout>(R.id.layout_select_gallery)

        serviceImageDialog.apply {
            window?.setBackgroundDrawableResource(R.drawable.dialog_picture)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setGravity(Gravity.BOTTOM)
            window?.attributes?.width = WindowManager.LayoutParams.WRAP_CONTENT
            window?.attributes?.height = WindowManager.LayoutParams.WRAP_CONTENT
        }

        binding.layoutPhotoSelect.setOnClickListener {
            serviceImageDialog.show()
            imageDialogClickEvent(serviceImageDialog, exitImageBtn, selectCamera, selectGallery)
        }

        binding.ivGoBack.setOnClickListener {
            finish()
        }
    }

    private fun imageDialogClickEvent(dialog: AlertDialog, exitButton: ImageView, selectCamera: LinearLayout, selectGallery: LinearLayout) {
        exitButton.setOnClickListener {
            dialog.dismiss()
        }

        selectCamera.setOnClickListener {
            dialog.dismiss()
            // 카메라 어플로 넘어가는 인텐트를 마련
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            /*
            resolveActivity(packageManager) != null is a condition that checks if there is at least one activity in the package (identified by packageManager) that can handle a specific intent.

            In Android programming, an intent is a message that can be used to request an action from another component of the same or another application. An activity, on the other hand, is a user interface component that represents a single screen with a user interface.

            resolveActivity() is a method of the PackageManager class that returns the ActivityInfo object that describes the activity that can handle the intent. If there is no activity that can handle the intent, resolveActivity() returns null.

            Therefore, resolveActivity(packageManager) != null checks whether there is at least one activity that can handle the intent specified in the package identified by packageManager. If this condition is true, it means that there is an activity that can handle the intent, and the code can proceed to launch the activity. If it is false, it means that there is no activity that can handle the intent, and the code should handle the error or provide an alternative solution.
            */
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                val photoFile = createImageFile()
                photoFile?.let {
                    imageUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
                    Log.d("imageUri - camera", "${imageUri}")
                }
            }
        }

        selectGallery.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, GALLERY_REQUEST_CODE)
        }

    }

    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        Log.d("imageFileName", "JPEG_${timeStamp}_.jpg, ${storageDir}")
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    val pickedImage: Uri? = data?.data
                    if (pickedImage != null) {
                        imageUri = pickedImage
                        imageUriString = pickedImage.toString()
                        Log.d("imageUrl", "${imageUriString}")
                        val imagePath = imageUri?.path
                        Log.d("imagePath", "${imagePath}")
                        Glide.with(this).load(imageUriString).into(binding.ivTest)

                        // 파일로 만들기
                        val imageFile = File(absolutelyPath(pickedImage, this))
                        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
                        // 서버로 보낼 이미지 파일
                        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
                        Log.d("imagePart", "${imagePart}")
                    }

                    // Do something with the image path
                }
                GALLERY_REQUEST_CODE -> {
                    val selectedImage = data?.data
                    val projection = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = selectedImage?.let { contentResolver.query(it, projection, null, null, null) }
                    cursor?.moveToFirst()
                    val columnIndex = cursor?.getColumnIndex(projection[0])
                    val imagePath = columnIndex?.let { cursor.getString(it) }
                    Log.d("imagePath", "${imagePath}")
                    cursor?.close()
                    // Do something with the image path

                    if (selectedImage != null) {
                        imageUri = selectedImage
                        imageUriString = selectedImage.toString()
                        Log.d("imageUri - gallery", "${imageUriString}")
                        val imagePath = imageUri?.path
                        Log.d("imagePath", "${imagePath}")
                        Glide.with(this).load(imageUriString).into(binding.ivTest)
                        // 파일로 만들기
                        val imageFile = File(absolutelyPath(selectedImage, this))
                        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
                        // 서버로 보낼 이미지 파일에 해당됨
                        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
                        Log.d("imagePart", "${imagePart}")
                    }
                }
            }
        }
    }

    fun absolutelyPath(uri: Uri, context: Context): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri!!, projection, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val path = cursor?.getString(columnIndex!!)
        cursor?.close()
        return path!!
    }
}