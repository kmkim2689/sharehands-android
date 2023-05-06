package com.sharehands.sharehands_frontend.viewmodel.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.search.ServiceContent
import com.sharehands.sharehands_frontend.view.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class ServiceDetailViewModel:ViewModel() {
    private var _contents = MutableLiveData<ServiceContent>()
//        .apply { ServiceContent(
//            null, null, null, null, null, null,
//            null, null, null, null, null,null, null, null,
//            null, null, null,null,null, ArrayList<String>(),null,null,
//            null,null,null,null,null,null,null,null,null) }
    val contents: LiveData<ServiceContent>
        get() = _contents

    private var _isAuthor = MutableLiveData<Boolean>()
    val isAuthor: LiveData<Boolean>
        get() = _isAuthor

    private var _isApplied = MutableLiveData<Boolean>()
    val isApplied: LiveData<Boolean>
        get() = _isApplied

    private var _photoList = MutableLiveData<List<String>?>()
    val photoList: LiveData<List<String>?>
        get() = _photoList

    private var _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean>
        get() = _isSuccessful

    fun showContents(token: String, serviceId: Int) {
        Log.d("봉사활동 상세 불러오기 아이디", "${serviceId}")
        RetrofitClient.createRetorfitClient().getService(token, serviceId)
            .enqueue(object : Callback<ServiceContent> {
                override fun onResponse(
                    call: Call<ServiceContent>,
                    response: Response<ServiceContent>
                ) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            val result = response.body()
                            Log.d("봉사활동 상세 불러오기 데이터 result 변수", "${result}")
                            _contents.value = result!!
                            _isApplied.value = result.didApply!!
                            _isAuthor.value = result.author!!
                            val photoList = result.photoList
                            _isSuccessful.value = true
                        } else {
                            Log.d("봉사활동 상세 데이터 불러오기 실패", response.code().toString())
                            _isSuccessful.value = false
                        }
                    }
                }

                override fun onFailure(call: Call<ServiceContent>, t: Throwable) {
                    Log.d("봉사활동 상세 데이터 불러오기 실패", t.message.toString())
                    _isSuccessful.value = false
                }
            })
    }

    fun apply(token: String, serviceId: Int) {
        RetrofitClient.createRetorfitClient().applyService(token, serviceId.toLong())
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Log.d("지원 성공", "${response.code()}")
                    _isSuccessful.value = true
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("지원 실패", "${t.message}")
                    _isSuccessful.value = false
                }

            })
    }

    fun cancelApply(token: String, serviceId: Int) {
        RetrofitClient.createRetorfitClient().cancelApplyService(token, serviceId.toLong())
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Log.d("지원 취소 성공", "${response.code()}")
                    _isSuccessful.value = true
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("지원 취소 실패", "${t.message}")
                    _isSuccessful.value = false
                }

            })
    }
}