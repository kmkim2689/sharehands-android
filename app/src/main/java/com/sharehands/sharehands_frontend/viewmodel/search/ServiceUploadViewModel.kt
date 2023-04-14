package com.sharehands.sharehands_frontend.viewmodel.search

import android.util.Log
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sharehands.sharehands_frontend.model.search.ServiceUpload
import com.sharehands.sharehands_frontend.model.search.ServiceUploadPics

class ServiceUploadViewModel: ViewModel() {
    // 사진 추가/삭제하는 족족 화면에 보낼 사진 목록을 띄워주도록 하기 위함.(기기 내 uri의 리스트 관찰)
    private var _imageUriList = MutableLiveData<ServiceUploadPics>()
    val imageUriList: LiveData<ServiceUploadPics>
        get() = _imageUriList

    // 게시글 작성한 사항들에 대한 데이터 클래스를 라이브 데이터로 만들기.
    private var _serviceInfo = MutableLiveData<ServiceUpload>()
    val serviceInfo: LiveData<ServiceUpload>
        get() = _serviceInfo

    // TODO 사진 추가하는 메소드
    // TODO 인자 : RecyclerView에 띄워줄 사진의 uri
    // TODO 동작1 : _imageUriList에 URI를 추가하고, 리사이클러뷰 item list에 add하기
    // TODO 동작2 glide로 리스트에 있는 모든 사진들 렌더링하기
    fun addImage(imageUri: String) {

    }

    // TODO 사진 삭제하는 메소드

    // TODO 게시글 작성 사항 서버로 전달하는 메소드

    // TODO 봉사활동 이름 edittext change 메소드. 글자수 세기
    fun onTextChanged(s: CharSequence, start :Int, before : Int, count: Int) {
        Log.d("text change", "${s}, ${start}, ${before}, ${count}")
//        when (editText.id)
    }
}