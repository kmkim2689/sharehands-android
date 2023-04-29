package com.sharehands.sharehands_frontend.viewmodel.signin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharehands.sharehands_frontend.model.signin.PostUserResponse
import com.sharehands.sharehands_frontend.network.ApiService
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.signin.UserInfoDetail
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserInfoViewModel: ViewModel() {
//    Response<Boolean>는 Retrofit 라이브러리에서 제공하는 클래스로, 네트워크 통신을 통해 받은 응답을 담는 클래스입니다.
//
//여기서 Boolean은 응답 바디에 담긴 데이터의 타입을 나타냅니다. 즉, 해당 네트워크 통신에서는 Boolean 타입의 데이터를 받는다는 것을 의미합니다.
//
//Response는 통신이 성공적으로 완료되었는지 여부를 나타내는 isSuccessful() 메서드와, 응답 바디에 담긴 데이터를 반환하는 body() 메서드를 제공합니다. 따라서 Response<Boolean>은 Boolean 타입의 데이터를 담은 응답을 받았으며, 이 응답이 성공적으로 처리되었는지 여부를 확인할 수 있는 클래스입니다.
    private var _response = MutableLiveData<String>().apply { value = "null" }
    val response: LiveData<String>
        get() = _response

    fun postUserInfo(userInfoDetail: UserInfoDetail) {
        viewModelScope.launch {
            try {
                RetrofitClient.createRetorfitClient().postUserDetail(userInfoDetail)
                    .enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                Log.d("회원정보 post 성공", "${response.code()}")
                                _response.value = "success"
                            } else {

                                Log.d("회원정보 post 실패", "${response.code()}")
                                _response.value = "fail"
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.d("회원정보 post 실패", "통신 오류")
                        }


                    })

            } catch (e: Exception) {
                Log.d("회원정보 POST 예외 발생", "${e}")
            }
        }
    }
}