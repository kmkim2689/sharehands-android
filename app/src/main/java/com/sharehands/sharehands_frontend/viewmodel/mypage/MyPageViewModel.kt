package com.sharehands.sharehands_frontend.viewmodel.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.mypage.MyPageInitial
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageViewModel: ViewModel() {
    private var _result = MutableLiveData<MyPageInitial>()
    val result: LiveData<MyPageInitial>
        get() = _result

    private var _profileUrl = MutableLiveData<String>()
    val profileUrl: LiveData<String>
        get() = _profileUrl

    fun getInfo(token: String) {
        RetrofitClient.createRetorfitClient().viewMyPage(token)
            .enqueue(object : Callback<MyPageInitial> {
                override fun onResponse(
                    call: Call<MyPageInitial>,
                    response: Response<MyPageInitial>
                ) {
                    val result = response.body()
                    if (result != null) {
                        _result.value = result
                        _profileUrl.value = result.profileUrl
                        Log.d("profileurl", "${profileUrl.value}")
                    }
                }

                override fun onFailure(call: Call<MyPageInitial>, t: Throwable) {
                    Log.d("마이 페이지 불러오기 실패", "${t.message}")
                }

            })
    }

    fun loadImageUrl(): String? {
        return profileUrl.value
    }
}