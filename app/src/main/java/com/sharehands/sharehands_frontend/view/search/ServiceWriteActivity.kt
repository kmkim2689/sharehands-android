package com.sharehands.sharehands_frontend.view.search

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog.show
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.sharehands.sharehands_frontend.BuildConfig
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.adapter.search.ServicePicRVAdapter
import com.sharehands.sharehands_frontend.databinding.ActivityServiceWriteBinding
import com.sharehands.sharehands_frontend.model.search.ServicePic
import com.sharehands.sharehands_frontend.network.search.location.KakaoMapApiService
import com.sharehands.sharehands_frontend.network.search.location.KakaoMapClient
import com.sharehands.sharehands_frontend.network.search.location.KakaoMapData
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.ProgressDialog
import com.sharehands.sharehands_frontend.viewmodel.search.ServiceUploadViewModel
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.*
import kotlin.collections.ArrayList

class ServiceWriteActivity: AppCompatActivity() {
    // 카메라 허용, 갤러리 허용, 이미지 URL 코드
    private val CAMERA_REQUEST_CODE = 101
    private val GALLERY_REQUEST_CODE = 201
    private var imageUri: Uri? = null
    lateinit var photoURI: Uri
    private var imageUriString: String = ""
    lateinit var currentPhotoPath: String
    private lateinit var viewModel: ServiceUploadViewModel
    // 이미지 url을 담는 리스트
    private var imageArrayList = ArrayList<String>()

    // 날짜 선택을 위한 calendar dialog 구현을 위한 변수들
    private val calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)

    // 시간 선택을 위한 timepicker dialog 구현
    private val tHour = calendar.get(Calendar.HOUR_OF_DAY)
    private val tMinute = calendar.get(Calendar.MINUTE)

    private lateinit var binding: ActivityServiceWriteBinding

    // TODO 생명주기 관리하기

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_write)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_service_write)
        viewModel = ViewModelProvider(this).get(ServiceUploadViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
        }

        // 버튼
        val buttonActive = binding.btnSubmitActive
        val buttonInactive = binding.btnSubmitInactive

        val pictureList = ArrayList<String>()
        // 사진 리사이클러뷰 어댑터
        val servicePicRVAdapter = ServicePicRVAdapter(this, pictureList, viewModel)

        // 레이아웃의 리사이클러뷰의 어댑터를 불러왔던 어댑터로 설정
        binding.rvPhotoList.adapter = servicePicRVAdapter
        binding.rvPhotoList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

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
            if (binding.tvPicCurrentCnt.text.toString().toInt() < 5) {
                serviceImageDialog.show()
                imageDialogClickEvent(serviceImageDialog, exitImageBtn, selectCamera, selectGallery)
            }
        }

        // 카테고리 선택 스피너
        val spinnerAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.category,
            android.R.layout.simple_spinner_item
        )

        // 드롭다운 레이아웃
        spinnerAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.spinnerCategory.apply {
            adapter = spinnerAdapter
            dropDownVerticalOffset = 120
            onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.onCategoryChanged(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
        }

        // 모집 마감일
        binding.btnDueDate.setOnClickListener {
            val dueDatePickerSetting = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                var dueDate = "${year}년 ${(month+1).toString().padStart(2, '0')}월 ${dayOfMonth.toString().padStart(2, '0')}일"
                binding.tvDueDate.text = dueDate
                viewModel.onDateChanged("due", dueDate)
            }

            // 띄워주기 위해 .show()메소드 이용
            val dueDatePickerDialog = DatePickerDialog(this, dueDatePickerSetting, year, month, day)
            dueDatePickerDialog.apply {
                datePicker.minDate = System.currentTimeMillis()
                show()
            }
        }

        // 봉사 장소
        val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                // 결과 처리 코드
                val derivedLocation = data?.getStringExtra("location").toString()
                Log.d("reserved address data", derivedLocation)
                binding.tvServiceLocation.text = derivedLocation
                viewModel.onAreaChanged(derivedLocation)
            }
        }

        binding.btnLocation.setOnClickListener {
            val locationIntent = Intent(this, LocationSearchActivity::class.java)
            activityResultLauncher.launch(locationIntent)
        }



        // 봉사 기간
        val startDatePickerSetting = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            var startDate = "${year}년 ${(month+1).toString().padStart(2, '0')}월 ${dayOfMonth.toString().padStart(2, '0')}일"
            binding.tvStartDateContent.text = startDate
            viewModel.onDateChanged("start", startDate)
        }

        val startDatePickerDialog = DatePickerDialog(this, startDatePickerSetting, year, month, day)

        val endDatePickerSetting = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            var endDate = "${year}년 ${(month+1).toString().padStart(2, '0')}월 ${dayOfMonth.toString().padStart(2, '0')}일"
            binding.tvEndDateContent.text = endDate
            viewModel.onDateChanged("end", endDate)
        }

        val endDatePickerDialog = DatePickerDialog(this, endDatePickerSetting, year, month, day)

        // 시작일
        binding.btnStartDate.setOnClickListener {

            startDatePickerDialog.apply {
                datePicker.minDate = System.currentTimeMillis()
                show()
            }
        }

        // 종료일
        binding.btnEndDate.setOnClickListener {
            if (binding.tvStartDateContent != null) {
                endDatePickerDialog.apply {
                    // TODO mindate를 startDay 이후로 설정
//                    val startDateCalendar = Calendar.getInstance()
//                    startDateCalendar.set(2023, Calendar.MAY, 1)
//                    val minDate = startDateCalendar.timeInMillis
//                    datePicker.minDate = minDate
                    show()
                }
            } else {
                // TODO 시작일을 고르라는 스낵바를 띄우기
            }

        }

        // 봉사 요일 선택
        val chkSun = binding.checkboxSunday
        val chkMon = binding.checkboxMonday
        val chkTue = binding.checkboxTuesday
        val chkWed = binding.checkboxWednesday
        val chkThu = binding.checkboxThursday
        val chkFri = binding.checkboxFriday
        val chkSat = binding.checkboxSaturday

        val chkboxList = listOf<CheckBox>(chkSun, chkMon, chkTue, chkWed, chkThu, chkFri, chkSat)
        val weekdayChecked = arrayListOf<Int>(0, 0, 0, 0, 0, 0, 0)
        for ((i, elem) in chkboxList.withIndex()) {
            elem.setOnClickListener {
                if (elem.isChecked) {
                    weekdayChecked[i] = 1
                    Log.d("게시글 작성 weekday list", "${weekdayChecked}")

                } else {
                    weekdayChecked[i] = 0
                    Log.d("게시글 작성 weekday list", "${weekdayChecked}")
                }
                viewModel.onChkboxChanged(weekdayChecked)
            }
        }

        // 시작시간
        binding.btnStartTime.setOnClickListener {
            val startTimePickerSetting = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                var startTime = "${hourOfDay.toString().padStart(2, '0')} : ${minute.toString().padStart(2, '0')}"
                binding.tvStartTimeContent.text = startTime
                viewModel.onTimeChanged("start", startTime)
            }

            val startTimePickerDialog = TimePickerDialog(this, startTimePickerSetting, tHour, tMinute, false)
            startTimePickerDialog.show()

        }

        binding.btnEndTime.setOnClickListener {
            Log.d("end버튼", "클릭")
            val endTimePickerSetting = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                var endTime = "${hourOfDay.toString().padStart(2, '0')} : ${minute.toString().padStart(2, '0')}"
                binding.tvEndTimeContent.text = endTime
                viewModel.onTimeChanged("end", endTime)
            }

            val endTimePickerDialog = TimePickerDialog(this, endTimePickerSetting, tHour, tMinute, false)
            endTimePickerDialog.show()

        }

        // 라디오박스 체크
        val radioPhoneOpen = binding.radioPhoneOpen
        val radioPhoneClose = binding.radioPhoneClose
        val radioEmailOpen = binding.radioEmailOpen
        val radioEmailClose = binding.radioEmailClose
        val radioEtcOpen = binding.radioEtcOpen
        val radioEtcClose = binding.radioEtcClose

        var closedArray = arrayListOf<Int>(0, 0, 0)

        radioPhoneOpen.setOnClickListener {
            binding.editPhone.visibility = View.VISIBLE
            binding.editPhoneBlocked.visibility = View.GONE
            Log.d("라디오 공개 체크", "전화")
            if (closedArray[0] == 1) {
                closedArray[0] = 0
            }
            Log.d("라디오 비공개수", "${closedArray}")
        }


        radioPhoneClose.setOnClickListener {
            if (closedArray.count { it == 1 } < 2) {
                binding.editPhone.text = null
                binding.editPhone.visibility = View.GONE
                binding.editPhoneBlocked.visibility = View.VISIBLE
                viewModel.onRadioBlocked("phone")
                Log.d("라디오 비공개 체크", "전화")
                closedArray[0] = 1
                Log.d("라디오 비공개수", "${closedArray}")
            } else {
                if (closedArray[0] != 1) {
                    radioPhoneClose.isChecked = false
                    radioPhoneOpen.isChecked = true
                    Log.d("라디오 비공개수", "${closedArray}")
                }

            }

        }


        radioEmailOpen.setOnClickListener {
            binding.editEmail.visibility = View.VISIBLE
            binding.editEmailBlocked.visibility = View.GONE
            Log.d("라디오 공개 체크", "이메일")
            if (closedArray[1] == 1) {
                closedArray[1] = 0
            }
            Log.d("라디오 비공개수", "${closedArray}")
        }

        radioEmailClose.setOnClickListener {
            if (closedArray.count { it == 1 } < 2) {
                binding.editEmail.text = null
                binding.editEmail.visibility = View.GONE
                binding.editEmailBlocked.visibility = View.VISIBLE
                viewModel.onRadioBlocked("email")
                Log.d("라디오 비공개 체크", "이메일")
                closedArray[1] = 1
                Log.d("라디오 비공개수", "${closedArray}")
            } else {
                if (closedArray[1] != 1) {
                    radioEmailClose.isChecked = false
                    radioEmailOpen.isChecked = true
                    Log.d("라디오 비공개수", "${closedArray}")
                }

            }
        }

        radioEtcOpen.setOnClickListener {
            binding.editEtc.visibility = View.VISIBLE
            binding.editEtcBlocked.visibility = View.GONE
            Log.d("라디오 공개 체크", "기타")
            if (closedArray[2] == 1) {
                closedArray[2] = 0
            }
            Log.d("라디오 비공개수", "${closedArray}")
        }

        radioEtcClose.setOnClickListener {
            if (closedArray.count { it == 1 } < 2) {
                binding.editEtc.text = null
                binding.editEtc.visibility = View.GONE
                binding.editEtcBlocked.visibility = View.VISIBLE
                viewModel.onRadioBlocked("etc")
                Log.d("라디오 비공개 체크", "이메일")
                closedArray[2] = 1
                Log.d("라디오 비공개수", "${closedArray}")
            } else {
                if (closedArray[2] != 1) {
                    radioEtcClose.isChecked = false
                    radioEtcOpen.isChecked = true
                    Log.d("라디오 비공개수", "${closedArray}")
                }

            }
        }

        // 다 채워졌으면 active button
        viewModel.isValid.observe(this) {
            if (viewModel.isValid.value == true) {
                if (buttonActive.visibility == View.INVISIBLE) {
                    buttonActive.visibility = View.VISIBLE
                    buttonInactive.visibility = View.INVISIBLE
                    buttonActive.startAnimation(
                        AnimationUtils.loadAnimation(
                            this@ServiceWriteActivity,
                            R.anim.fade_in
                        )
                    )
                }
            } else {
                if (buttonInactive.visibility == View.INVISIBLE) {
                    buttonActive.visibility = View.INVISIBLE
                    buttonInactive.visibility = View.VISIBLE
                    buttonInactive.startAnimation(
                        AnimationUtils.loadAnimation(
                            this@ServiceWriteActivity,
                            R.anim.fade_in
                        )
                    )
                }

            }
        }

        binding.btnSubmitActive.setOnClickListener {
            val progressDialog = ProgressDialog(this, "업로드 중")
            progressDialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
            progressDialog.setCancelable(false)
            progressDialog.show()

            val token = SharedPreferencesManager.getInstance(this)
                .getString("token", "null")

            if (token != "null") {
                viewModel.upload(token)
                viewModel.isSuccessful.observe(this) {
                    if (viewModel.isSuccessful.value == true) {
                        progressDialog.dismiss()
                        Log.d("게시글 작성 완료", "${viewModel.isSuccessful.value}")
                        showSnackbar("게시글 작성이 완료되었습니다.")
                        finish()
                    } else {
                        Log.d("게시글 작성 실패", "${viewModel.isSuccessful.value}")
                        // TODO 스낵바 띄우기
                        showSnackbar("네트워크 오류가 발생하였습니다. 다시 시도해보세요.")

                        binding.apply {
                            tvWarning.text = "네트워크 오류가 발생했습니다. 다시 시도해보세요."
                            tvWarning.setTextColor(Color.RED)
                        }
                    }
                }
            }
        }

        binding.btnSubmitInactive.setOnClickListener {
            Log.d("비활성화 클릭", "경고메시지")
            binding.tvWarning.visibility = View.VISIBLE
            binding.tvWarning.startAnimation(
                AnimationUtils.loadAnimation(
                    this@ServiceWriteActivity,
                    R.anim.fade_in
                )
            )
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
            takePictureFullSize()
        }

        selectGallery.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, GALLERY_REQUEST_CODE)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED
            && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permisson: " + permissions[0] + " was " + grantResults[0])
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    Log.d("imageUri", "${photoURI}")
                    if (photoURI != null) {
                        imageUriString = photoURI.toString()
                        val imagePath = photoURI?.path
                        Log.d("imagePath", "${imagePath}")
                        val imageFile = File(imagePath)
                        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
                        // 서버로 보낼 이미지 파일
                        val imagePart = MultipartBody.Part.createFormData("files", imageFile.name, requestFile)
                        Log.d("imagePart", "${imagePart}")

                        viewModel.addImage(imageUriString, imagePart)

                        var pictureList = ArrayList<String>()
                        Log.d("image list", "${pictureList}")
                        for (i in 0 until viewModel.imageUriList.value!!.size) {
                            pictureList.add(viewModel.imageUriList.value!![i])
                            Log.d("image List added", "${pictureList}")
                        }

                        val servicePicRVAdapter = ServicePicRVAdapter(this, pictureList, viewModel)
                        binding.rvPhotoList.adapter = servicePicRVAdapter
                        binding.rvPhotoList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

                    }

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
                        // 파일로 만들기
                        val imageFile = File(absolutelyPath(selectedImage, this))
                        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
                        // 서버로 보낼 이미지 파일에 해당됨
                        val imagePart = MultipartBody.Part.createFormData("files", imageFile.name, requestFile)
                        Log.d("imagePart", "${imagePart}")

                        viewModel.addImage(imageUriString, imagePart)

                        var pictureList = ArrayList<String>()
                        Log.d("image list", "${pictureList}")
                        for (i in 0 until viewModel.imageUriList.value!!.size) {
                            pictureList.add(viewModel.imageUriList.value!![i])
                            Log.d("image List added", "${pictureList}")
                        }

                        val servicePicRVAdapter = ServicePicRVAdapter(this, pictureList, viewModel)
                        binding.rvPhotoList.adapter = servicePicRVAdapter
                        binding.rvPhotoList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

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
        Log.d("image path", "${path}")
        return path!!
    }

    private fun takePictureFullSize() {
        photoURI = Uri.EMPTY
        val fullSizePictureIntent = getPictureIntent_App_Specific(applicationContext)
        fullSizePictureIntent.resolveActivity(packageManager)?.also {
            startActivityForResult(fullSizePictureIntent, CAMERA_REQUEST_CODE)
        }
    }

    /**
     * 카메라 호출할 Intent 생성
     */
    fun getPictureIntent_App_Specific(context: Context): Intent {

        val fullSizeCaptureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        //1) File 생성 - 촬영 사진이 저장 될
        //photoFile 경로 = /storage/emulated/0/Android/data/패키지명/files/Pictures/
        val photoFile: File? = try {
            createImageFile(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES))

        } catch (ex: IOException) {
            // Error occurred while creating the File
            ex.printStackTrace()
            null
        }
        Log.d("image file", "${photoFile}")

        photoFile?.also {
            //2) 생성된 File로 부터 Uri 생성 (by FileProvider)
            //URI 형식 EX) content://com.example.img.fileprovider/cameraImg/JPEG_20211124_202832_6573897384086993610.jpg
            photoURI = FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID + ".fileprovider",
                it
            )

            //3) 생성된 Uri를 Intent에 Put
            fullSizeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        }
        return fullSizeCaptureIntent
    }


    /**
     * 빈 파일 생성
     */
    @Throws(IOException::class)
    private fun createImageFile(storageDir: File?): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            Log.i("syTest", "Created File AbsolutePath : $absolutePath")
        }
    }

//    스낵바 띄우기
    private fun showSnackbar(text: String) {
        val snackbar = Snackbar.make(binding.coordinatorLayout, text, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }
}