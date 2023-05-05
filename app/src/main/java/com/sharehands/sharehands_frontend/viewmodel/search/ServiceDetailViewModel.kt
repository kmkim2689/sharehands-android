package com.sharehands.sharehands_frontend.viewmodel.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.search.ServiceContent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class ServiceDetailViewModel:ViewModel() {
    private var _contents = MutableLiveData<ServiceContent>().apply { ServiceContent(
        null, null, null, null, null, null,
        null, null, null, null, null,null, null, null,
        null, null, null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null) }
    val contents: LiveData<ServiceContent>
        get() = _contents

    fun showContents(token: String, serviceId: Int): Boolean {
        var isSuccessful = false
        RetrofitClient.createRetorfitClient().getService(token, serviceId)
            .enqueue(object : Callback<ServiceContent> {
                override fun onResponse(
                    call: Call<ServiceContent>,
                    response: Response<ServiceContent>
                ) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            val result = response.body()
                            _contents.value = result
                            isSuccessful = true
                        } else {
                            Log.d("봉사활동 상세 데이터 불러오기 실패", response.code().toString())
                        }
                    }
                }

                override fun onFailure(call: Call<ServiceContent>, t: Throwable) {
                    Log.d("봉사활동 상세 데이터 불러오기 실패", t.message.toString())
                }

            })
        return isSuccessful
    }
}