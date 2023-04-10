package com.sharehands.sharehands_frontend.viewmodel.signin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharehands.sharehands_frontend.model.signin.PostUserResponse
import com.sharehands.sharehands_frontend.network.ApiService
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.signin.UserInterest
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserInterestViewModel(): ViewModel() {
    private var _response = MutableLiveData<PostUserResponse>()
    val response: LiveData<PostUserResponse>
        get() = _response

    fun postUserInterest(userInterest: UserInterest) {
        viewModelScope.launch {
            try {
                RetrofitClient.createRetorfitClient().postUserInterest(userInterest)
                    .enqueue(object : Callback<PostUserResponse> {
                        override fun onResponse(
                            call: Call<PostUserResponse>,
                            response: Response<PostUserResponse>
                        ) {
                            if (response.isSuccessful) {
                                Log.d("회원 관심 POST 성공", response.body().toString())
                                _response.value = response.body()
                            } else {
                                Log.d("회원 관심 POST 실패", response.body().toString())
                            }
                        }

                        override fun onFailure(call: Call<PostUserResponse>, t: Throwable) {
                            Log.d("회원정보 POST 실패", t.toString())
                        }

                    })

            } catch (e: Exception) {
                Log.d("회원정보 POST 예외 발생", "${e}")
            }
        }
    }
}