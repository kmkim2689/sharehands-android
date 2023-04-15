package com.sharehands.sharehands_frontend.viewmodel.search

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sharehands.sharehands_frontend.model.search.ServicePic
import com.sharehands.sharehands_frontend.model.search.ServicePicPart
import com.sharehands.sharehands_frontend.model.search.ServiceUpload
import okhttp3.MultipartBody

class ServiceUploadViewModel(): ViewModel() {


    // 사진 추가/삭제하는 족족 화면에 보낼 사진 목록을 띄워주도록 하기 위함.(기기 내 uri의 리스트 관찰)
    private var _imageUriList = MutableLiveData<ArrayList<ServicePic>>()
    val imageUriList: LiveData<ArrayList<ServicePic>>
        get() = _imageUriList

    private var _imagePartList = MutableLiveData<ArrayList<ServicePicPart>>()
    val imagePartList: LiveData<ArrayList<ServicePicPart>>
        get() = _imagePartList

    // 게시글 작성한 사항들에 대한 데이터 클래스를 라이브 데이터로 만들기.
    private var _serviceInfo = MutableLiveData<ServiceUpload>()
    val serviceInfo: LiveData<ServiceUpload>
        get() = _serviceInfo

    private var _numOfImages = MutableLiveData<String>().apply { value = "0" }
    val numOfImages: LiveData<String>
        get() = _numOfImages

    init {

        val initItems: ArrayList<ServicePic> = ArrayList()
        _imageUriList.value = initItems
    }

    // 사용할 edittext
    val title = MutableLiveData<String>()
    val intro = MutableLiveData<String>()
    val maxNum = MutableLiveData<String>()
    val expense = MutableLiveData<String>()
    val detail = MutableLiveData<String>()
    val tel = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val etc = MutableLiveData<String>()

    // edittext count. default값을 0으로 설정하기 위하여 apply scope function 사용
    val titleCount = MutableLiveData<String>().apply { value = "0" }
    val introCount = MutableLiveData<String>().apply { value = "0" }
    val detailCount = MutableLiveData<String>().apply { value = "0" }




    // TODO 사진 추가하는 메소드
    // TODO 인자 : RecyclerView에 띄워줄 사진의 uri
    // TODO 동작1 : _imageUriList에 URI를 추가하고, 리사이클러뷰 item list에 add하기
    // TODO 동작2 glide로 리스트에 있는 모든 사진들 렌더링하기
    fun addImage(imageUri: String, imagePart: MultipartBody.Part) {

        if (_imagePartList.value == null) {
            _imagePartList.value = arrayListOf()
        }

        if (_imageUriList.value == null) {
            _imageUriList.value = arrayListOf()
        }

        _imageUriList.value!!.add(ServicePic(imageUri))
        _imagePartList.value!!.add(ServicePicPart(imagePart))

        Log.d("imageUriList에 사진 추가", "${imageUriList.value}")
        Log.d("imagePartList에 사진 추가", "${imagePartList.value}")

        _numOfImages.value = imageUriList.value!!.size.toString()
    }

    // TODO 사진 삭제하는 메소드
    fun deleteImage(position: Int) {
        _imageUriList.value!!.removeAt(position)
        _imagePartList.value!!.removeAt(position)
        Log.d("imageUriList ${position+1}번째 사진 삭제", "${imageUriList.value}")
        Log.d("imagePartList ${position+1}번째 사진 삭제", "${imagePartList.value}")
    }




    // TODO 게시글 작성 사항 서버로 전달하는 메소드

    // TODO 봉사활동 이름 edittext change 메소드. 글자수 세기
    fun onTextChanged(s: CharSequence, start :Int, before : Int, count: Int){
        // handle
    }

    // TODO 스피너 변경 리스너
    fun onCategoryChanged(position: Int) {
        var category = when (position) {
            0 -> "교육"
            1 -> "문화"
            2 -> "보건"
            3 -> "환경"
            4 -> "기술"
            5 -> "해외"
            6 -> "캠페인"
            7 -> "재난"
            8 -> "기타"
            else -> "error"
        }
        Log.d("게시글 작성 category 선택", category)
        _serviceInfo.value?.category = category
    }

    // due date 변경
    fun onDateChanged(editText: String, date: String) {
        when (editText) {
            "due" -> _serviceInfo.value?.due = date
            "start" -> _serviceInfo.value?.startDate = date
            "end" -> _serviceInfo.value?.endDate = date
        }

    }

    // xml 파일에서 textwatcher를 사용.(onTextChanged)
    fun onTitleChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        // (Mutable)LiveData의 값을 업데이트하는 메소드 : postValue(변경할 값)
        title.postValue(s.toString()) // 추후 observe 하게 될 때(버튼 클릭 시) 사용하기 위함.
        Log.d("changed text", "${s.toString()}")
        Log.d("title text count", "${count}")
        titleCount.value = s.length.toString()
        _serviceInfo.value?.name = s.toString()
    }

    fun onIntroChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        intro.postValue(s.toString())
        Log.d("changed text", "${s.toString()}")
        Log.d("title text count", "${count}")
        introCount.value = s.length.toString()
        _serviceInfo.value?.intro = s.toString()
    }

    fun onDetailChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        detail.postValue(s.toString())
        Log.d("changed text", "${s.toString()}")
        Log.d("title text count", "${count}")
        detailCount.value = s.length.toString()
        _serviceInfo.value?.detailDesc = s.toString()
    }


    // TODO 확인 버튼 클릭 시 동작을 정의
    fun upload() {

    }
}