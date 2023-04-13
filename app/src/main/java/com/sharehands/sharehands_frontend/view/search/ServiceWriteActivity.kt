package com.sharehands.sharehands_frontend.view.search

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
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
import com.sharehands.sharehands_frontend.BuildConfig
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivityServiceWriteBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ServiceWriteActivity: AppCompatActivity() {
    // 카메라 허용, 갤러리 허용, 이미지 URL 코드
    private val CAMERA_REQUEST_CODE = 101
    private val GALLERY_REQUEST_CODE = 201
    private var imageUri: Uri? = null

    lateinit var binding: ActivityServiceWriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_write)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_service_write)

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
                    val imagePath = imageUri?.path
                    Log.d("imagePath", "${imagePath}")
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
                }
            }
        }
    }

    private fun imageDialogClickEvent(dialog: AlertDialog, exitButton: ImageView, selectCamera: LinearLayout, selectGallery: LinearLayout) {
        exitButton.setOnClickListener {
            dialog.dismiss()
        }

        selectCamera.setOnClickListener {
            dialog.dismiss()
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                val photoFile = createImageFile()
                photoFile?.let {
                    imageUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
                }
            }
        }

        selectGallery.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, GALLERY_REQUEST_CODE)
        }

    }


}