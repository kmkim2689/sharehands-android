package com.sharehands.sharehands_frontend.viewmodel.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.mypage.MyPageDetail
import com.sharehands.sharehands_frontend.network.signin.UserInfoEdit
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.mypage.UserInfoActivity
import org.apache.commons.lang3.mutable.Mutable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageDetailViewModel: ViewModel() {
    private var _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean>
        get() = _isSuccessful

    private var _result = MutableLiveData<MyPageDetail?>()
    val result: LiveData<MyPageDetail?>
        get() = _result

    private var _complete = MutableLiveData<String>()
    val complete: LiveData<String>
        get() = _complete

    private var _level = MutableLiveData<String>()
    val level: LiveData<String>
        get() = _level

    private var _participate = MutableLiveData<String>()
    val participate: LiveData<String>
        get() = _participate

    private var _levelPercent = MutableLiveData<String>()
    val levelPercent: LiveData<String>
        get() = _levelPercent

    private var _reviewPercent = MutableLiveData<String>()
    val reviewPercent: LiveData<String>
        get() = _reviewPercent

    private var _recruit = MutableLiveData<String>()
    val recruit: LiveData<String>
        get() = _recruit

    private var _review = MutableLiveData<String>()
    val review: LiveData<String>
        get() = _review

    private var _reviewScore = MutableLiveData<String>()
    val reviewScore: LiveData<String>
        get() = _reviewScore

    private var _volunteer = MutableLiveData<String>()
    val volunteer: LiveData<String>
        get() = _volunteer

    // 변경 가능 부분들
    private var _nickname = MutableLiveData<String>()
    val nickname: LiveData<String>
        get() = _nickname

    private var _tel = MutableLiveData<String>()
    val tel: LiveData<String>
        get() = _tel

    private var _location = MutableLiveData<String>()
    val location: LiveData<String>
        get() = _location

    private var _interests = MutableLiveData<ArrayList<String>>(arrayListOf<String>())
    val interests: LiveData<ArrayList<String>>
        get() = _interests


    fun getUserInfo(token: String) {
        RetrofitClient.createRetorfitClient().viewMyPageDetail(token)
            .enqueue(object : Callback<MyPageDetail> {
                override fun onResponse(
                    call: Call<MyPageDetail>,
                    response: Response<MyPageDetail>
                ) {
                    if (response.isSuccessful) {
                        val res = response.body()
                        if (res != null) {
                            Log.d("유저 상세정보 불러오기", "${res}")
                            _result.value = res
                            _complete.value = res.complete.toString()
                            _level.value = res.level.toString()
                            _participate.value = res.participate.toString()
                            _levelPercent.value = res.levelPercent.toString()
                            _reviewPercent.value = res.reviewPercent.toString()
                            _recruit.value = res.recruit.toString()
                            _review.value = res.review.toString()
                            _reviewScore.value = res.reviewScore.toString()
                            _volunteer.value = res.volunteer.toString()
                            _nickname.value = res.nickname
                            _tel.value = res.tel
                            _location.value = res.location
                            Log.d("interests", "${res.interests}")
                            res.interests.forEach {
                                Log.d("interest", "${it}")
                                _interests.value?.add(it)
                                Log.d("사용자 상세정보 interests", "${interests.value}")
                            }

                        }

                    } else {
                        Log.d("사용자 상세정보 불러오기 실패", "${response.code()}")
                    }
                }

                override fun onFailure(call: Call<MyPageDetail>, t: Throwable) {
                    Log.d("사용자 상세정보 불러오기 실패", "${t.message}")
                }

            })



    }

    fun onNicknameChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        _nickname.value = s.toString()
        Log.d("nickname changed", "${nickname}")
    }

    fun onTelChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        _tel.value = s.toString()
        Log.d("nickname changed", "${tel}")
    }

    fun setLocation(location: String) {
        _location.value = location
    }

    fun post(token: String, interests: ArrayList<String>) {
        _interests.value = interests
        val editData = UserInfoEdit(nickname.value, tel.value, location.value, interests)
        Log.d("editData", "${editData}")
        RetrofitClient.createRetorfitClient().editUserInfo(token, editData)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("회원정보 수정 성공", "${response.body()}")
                        _isSuccessful.value = true
                    } else {
                        Log.d("회원정보 수정 실패", "${response.code()}")
                        _isSuccessful.value = false
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("회원정보 수정 실패", "${t.message}")
                    _isSuccessful.value = false
                }

            })

    }

}