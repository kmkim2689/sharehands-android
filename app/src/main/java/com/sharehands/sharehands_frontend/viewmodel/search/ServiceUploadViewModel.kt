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
    private var _imageUriList = MutableLiveData<ArrayList<String>>()
    val imageUriList: LiveData<ArrayList<String>>
        get() = _imageUriList

    private var _imagePartList = MutableLiveData<ArrayList<MultipartBody.Part>>()
    val imagePartList: LiveData<ArrayList<MultipartBody.Part>>
        get() = _imagePartList

    // 게시글 작성한 사항들에 대한 데이터 클래스를 라이브 데이터로 만들기.
//    private var _serviceInfo = MutableLiveData<ServiceUpload>()
//    val serviceInfo: LiveData<ServiceUpload>
//        get() = _serviceInfo

    private var _numOfImages = MutableLiveData<String>().apply { value = "0" }
    val numOfImages: LiveData<String>
        get() = _numOfImages

    private var _category = MutableLiveData<String>()
    val category: LiveData<String>
        get() = _category

    // 사용할 edittext
    private var _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title
    private var _intro = MutableLiveData<String>()
    val intro: LiveData<String>
        get() = _intro
    private var _due = MutableLiveData<String>()
    val due: LiveData<String>
        get() = _due
    private var _area = MutableLiveData<String>()
    val area: LiveData<String>
        get() = _area
    private var _startDate = MutableLiveData<String>()
    val startDate: LiveData<String>
        get() = _startDate
    private var _endDate = MutableLiveData<String>()
    val endDate: LiveData<String>
        get() = _endDate
    private var _weekdayList = MutableLiveData<ArrayList<String>>()
    val weekdayList: LiveData<ArrayList<String>>
        get() = _weekdayList
    private var _startTime = MutableLiveData<String>()
    val startTime: LiveData<String>
        get() = _startTime
    private var _endTime = MutableLiveData<String>()
    val endTime: LiveData<String>
        get() = _endTime
    private var _maxNum = MutableLiveData<String>()
    val maxNum: LiveData<String>
        get() = _maxNum
    private var _expense = MutableLiveData<String>()
    val expense: LiveData<String>
        get() = _expense
    private var _detail = MutableLiveData<String>()
    val detail: LiveData<String>
        get() = _detail
    private var _tel = MutableLiveData<String>()
    val tel: LiveData<String>
        get() = _tel
    private var _email = MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email
    private var _etc = MutableLiveData<String>()
    val etc: LiveData<String>
        get() = _etc

    // edittext count. default값을 0으로 설정하기 위하여 apply scope function 사용
    var titleCount = MutableLiveData<String>().apply { value = "0" }
    var introCount = MutableLiveData<String>().apply { value = "0" }
    var detailCount = MutableLiveData<String>().apply { value = "0" }

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

        _imageUriList.value!!.add(imageUri)
        _imagePartList.value!!.add(imagePart)

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
        _category.value = category
    }

    // due date 변경
    fun onDateChanged(editText: String, date: String) {
        when (editText) {
            "due" -> _due.value = date
            "start" -> _startDate.value = date
            "end" -> _endDate.value = date
        }

    }

    fun onTimeChanged(text: String, time: String) {
        when (text) {
            "start" -> _startTime.value = time
            "end" -> _endTime.value = time
        }
    }

    fun onAreaChanged(text: String) {
        _area.value = text
    }

    // xml 파일에서 textwatcher를 사용.(onTextChanged)
    fun onTitleChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        // (Mutable)LiveData의 값을 업데이트하는 메소드 : postValue(변경할 값)
//        title.value.postValue(s.toString()) // 추후 observe 하게 될 때(버튼 클릭 시) 사용하기 위함.
        Log.d("changed text", "${s.toString()}")
        Log.d("title text count", "${count}")
        titleCount.value = s.length.toString()
        _title.value = s.toString()
    }

    fun onIntroChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//        intro.postValue(s.toString())
        Log.d("changed text", "${s.toString()}")
        Log.d("title text count", "${count}")
        introCount.value = s.length.toString()
        _intro.value = s.toString()
    }

    fun onDetailChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//        detail.postValue(s.toString())
        Log.d("changed text", "${s.toString()}")
        Log.d("title text count", "${count}")
        detailCount.value = s.length.toString()
        _detail.value = s.toString()
    }

    fun onMaxNumChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//        maxNum.postValue(s.toString())
        Log.d("changed text", "${s.toString()}")
        Log.d("title text count", "${count}")
        _maxNum.value = s.toString()
    }

    fun onExpenseChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//        expense.postValue(s.toString())
        Log.d("changed text", "${s.toString()}")
        Log.d("title text count", "${count}")
        _expense.value = s.toString()
    }


    fun onChkboxChanged(weekdayChecked: ArrayList<Int>) {
        val weekdayList = ArrayList<String>()
        for (i in 0 until weekdayChecked.size) {
            when (i) {
                0 ->  {
                    if (weekdayChecked[i] == 1) {
                        weekdayList.add("일")
                    }
                }
                1 ->  {
                    if (weekdayChecked[i] == 1) {
                        weekdayList.add("월")
                    }
                }
                2 ->  {
                    if (weekdayChecked[i] == 1) {
                        weekdayList.add("화")
                    }
                }
                3 ->  {
                    if (weekdayChecked[i] == 1) {
                        weekdayList.add("수")
                    }
                }
                4 ->  {
                    if (weekdayChecked[i] == 1) {
                        weekdayList.add("목")
                    }
                }
                5 ->  {
                    if (weekdayChecked[i] == 1) {
                        weekdayList.add("금")
                    }
                }
                6 ->  {
                    if (weekdayChecked[i] == 1) {
                        weekdayList.add("토")
                    }
                }
            }
        }

        Log.d("게시글 작성 weekdayList", "${weekdayList}")
        _weekdayList.value = weekdayList
    }

    fun onRadioBlocked(contact: String) {
        when (contact) {
            "phone" -> _tel.value = "비공개"
            "email" -> _email.value = "비공개"
            "etc" -> _etc.value = "비공개"
        }
    }

    fun onPhoneChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        Log.d("changed text", "${s}")
        Log.d("tel text count", "${count}")
        _tel.value = s.toString()
    }

    fun onEmailChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        Log.d("changed text", "${s}")
        Log.d("email text count", "${count}")
        _email.value = s.toString()
    }

    fun onEtcChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        Log.d("changed text", "${s}")
        Log.d("etc count", "${count}")
        _etc.value = s.toString()
    }

    fun onInactiveClick() {
        val result = ServiceUpload(
            category.value,
            title.value,
            intro.value,
            due.value,
            area.value,
            startDate.value,
            endDate.value,
            weekdayList.value,
            startTime.value,
            endTime.value,
            maxNum.value,
            expense.value,
            detail.value,
            tel.value,
            email.value,
            etc.value,
            imagePartList.value
        )
        Log.d("봉사활동 채워진 것들", "${result}")
    }

    // TODO 확인 버튼 클릭 시 동작을 정의
    fun upload() {

    }
}