package com.sharehands.sharehands_frontend.viewmodel.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sharehands.sharehands_frontend.model.search.ServiceItem
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceSearchViewModel: ViewModel() {
    val sp = SharedPreferencesManager.getInstance(MainActivity())
    val token = sp.getString("token", "null")
    private var _servicesList = MutableLiveData<ArrayList<ServiceItem>>()
    val servicesList: LiveData<ArrayList<ServiceItem>>
        get() = _servicesList

//    private var _isSuccessful = MutableLiveData<Boolean>()
//    val isSuccessful: LiveData<Boolean>
//        get() = _isSuccessful

    // 네트워크 통신 정의하기
    fun loadServices(category: Int, sort: Int, page: Int): Boolean {
        var isSuccessful = false
        if (token != "null") {
            if (page == 1) {
                RetrofitClient.createRetorfitClient().getServicesInitial(token, category, sort)
                    .enqueue(object : Callback<List<ServiceItem>> {
                        override fun onResponse(
                            call: Call<List<ServiceItem>>,
                            response: Response<List<ServiceItem>>
                        ) {
                            Log.d("봉사활동 목록 GET response code", "${response.code()}")
                            if (response.code() == 200) {
                                val result = response.body()
                                if (result != null) {
                                    for (elem in result) {
                                        _servicesList.value!!.add(elem)
                                    }
                                    Log.d("봉사활동 목록 GET 성공", "${result}")
                                    Log.d("봉사활동 전체 목록", "${servicesList.value}")
                                    isSuccessful = true
                                } else {
                                    Log.d("봉사뢀동 목록 GET 실패", "데이터 비어있음")
                                }
                            } else {
                                Log.d("봉사활동 목록 GET 실패", "${response.code()}")
                            }
                        }

                        override fun onFailure(call: Call<List<ServiceItem>>, t: Throwable) {
                            Log.d("봉사활동 목록 GET 실패", "${t.message}")
                        }

                    })

            } else {
                RetrofitClient.createRetorfitClient().getServicesAdditional(
                    token,
                    category,
                    sort,
                    servicesList.value?.get(servicesList.value!!.size - 1)!!.serviceId
                ).enqueue(object : Callback<List<ServiceItem>> {
                    override fun onResponse(
                        call: Call<List<ServiceItem>>,
                        response: Response<List<ServiceItem>>
                    ) {
                        Log.d("봉사활동 목록 추가 GET response code", "${response.code()}")
                        if (response.code() == 200) {
                            val result = response.body()
                            if (result != null) {
                                for (elem in result) {
                                    _servicesList.value!!.add(elem)
                                }
                                Log.d("봉사활동 목록 GET 성공", "${result}")
                                Log.d("봉사활동 전체 목록", "${servicesList.value}")
                                isSuccessful = true
                            } else {
                                Log.d("봉사뢀동 목록 GET 실패", "데이터 비어있음")
                            }
                        } else {
                            Log.d("봉사활동 목록 GET 실패", "${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<List<ServiceItem>>, t: Throwable) {
                        Log.d("봉사활동 추가 목록 GET 실패", "${t.message}")
                    }

                })
            }
        } else {
            Log.d("봉사활동 목록 GET 실패", "no token")
        }
        return isSuccessful

    }

}