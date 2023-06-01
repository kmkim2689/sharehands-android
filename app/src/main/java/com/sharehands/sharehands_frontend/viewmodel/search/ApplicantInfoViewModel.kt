package com.sharehands.sharehands_frontend.viewmodel.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.mypage.MyPageDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApplicantInfoViewModel: ViewModel() {
    private var _userInfo = MutableLiveData<MyPageDetail>()
    val userInfo: LiveData<MyPageDetail>
        get() = _userInfo

    private var _interests = MutableLiveData<String>()
    val interests: LiveData<String>
        get() = _interests

    fun getUserInfo(token: String, userId: Long) {
        if (token != "null" && userId.toInt() != 0) {
            RetrofitClient.createRetorfitClient().viewUserDetail(token, userId)
                .enqueue(object : Callback<MyPageDetail> {
                    override fun onResponse(
                        call: Call<MyPageDetail>,
                        response: Response<MyPageDetail>
                    ) {
                        if (response.code() == 200) {
                            Log.d("유저 상세정보 불러오기 성공", "${response.body()}")
                            _userInfo.value = response.body()
                            val interestsList = _userInfo.value?.interests
                            var interestString = ""
                            if (interestsList != null) {
                                for (elem in interestsList) {
                                    interestString += elem
                                    interestString += " "
                                }
                            }
                            _interests.value = interestString
                            Log.d("interests", "$interests")
                        } else {
                            Log.d("유저 상세정보 불러오기 실패", "${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<MyPageDetail>, t: Throwable) {
                        Log.d("유저 상세정보 불러오기 실패", "${t.message}")
                    }

                })
        }
    }
}