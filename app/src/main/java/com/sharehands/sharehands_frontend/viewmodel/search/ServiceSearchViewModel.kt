package com.sharehands.sharehands_frontend.viewmodel.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.search.SearchResult
import com.sharehands.sharehands_frontend.network.search.ServiceList
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceSearchViewModel: ViewModel() {
    val sp = SharedPreferencesManager.getInstance(MainActivity())
    val token = sp.getString("token", "null")
    private var _serviceSum = MutableLiveData<Int>()
    val serviceSum: LiveData<Int>
        get() = _serviceSum

    // 괄호 안에 초기화를 해야 리스트에 추가 시 오류가 안나옴
    private var _servicesList = MutableLiveData<ArrayList<ServiceList>>(ArrayList())
    val servicesList: LiveData<ArrayList<ServiceList>>
        get() = _servicesList



    private var _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean>
        get() = _isSuccessful

    // 네트워크 통신
    fun loadServices(category: Int, sort: Int, page: Int): Boolean {

        var isSuccessful = false
        if (token != "null") {
            if (page == 1) {
                RetrofitClient.createRetorfitClient().getServicesInitial(token, category, sort)
                    .enqueue(object : Callback<SearchResult> {
                        override fun onResponse(
                            call: Call<SearchResult>,
                            response: Response<SearchResult>
                        ) {
                            Log.d("봉사활동 목록 GET response code", "${response.code()}")
                            if (response.code() == 200) {
                                val result = response.body()
                                if (result != null) {
                                    _serviceSum.value = result.workCounter.toInt()
                                    for (elem in result.serviceList) {
                                        _servicesList.value!!.add(elem)
                                    }
                                    Log.d("봉사활동 목록 GET 성공", "${result}")
                                    Log.d("봉사활동 전체 목록", "${servicesList.value}")
                                    isSuccessful = true
                                } else {
                                    Log.d("봉사활동 목록 GET 실패", "데이터 비어있음")
                                }
                            } else {
                                Log.d("봉사활동 목록 GET 실패", "${response.code()}")
                                // TODO 더미데이터 코드 제거할것
                                _servicesList.value!!.apply {
                                    add(ServiceList(1, "https://tago.kr/images/sub/TG300-D02_img01.png", "https://image.ajunews.com//content/image/2021/09/13/20210913143050976154.jpg",
                                        "케이엠", true, "봉사갑시다", "동대문구", "10명", "2022. 01. 01 ~ 2022. 03. 03", "화, 목"))
                                    add(ServiceList(2, "https://tago.kr/images/sub/TG300-D02_img01.png", "https://image.ajunews.com//content/image/2021/09/13/20210913143050976154.jpg",
                                        "케이엔", true, "봉사2", "동대문구", "10명", "2022. 01. 01 ~ 2022. 03. 03", "화, 목"))
                                }
                            }
                        }

                        override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                            Log.d("봉사활동 목록 GET 실패", "${t.message}")
                        }

                    })


            } else {
                RetrofitClient.createRetorfitClient().getServicesAdditional(
                    token,
                    category,
                    sort,
                    servicesList.value?.get(servicesList.value!!.size - 1)!!.workId.toInt()
                ).enqueue(object : Callback<SearchResult> {
                    override fun onResponse(
                        call: Call<SearchResult>,
                        response: Response<SearchResult>
                    ) {
                        Log.d("봉사활동 목록 추가 GET response code", "${response.code()}")
                        if (response.code() == 200) {
                            val result = response.body()
                            if (result != null) {
                                _serviceSum.value = result.workCounter.toInt()
                                for (elem in result.serviceList) {
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

                    override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                        Log.d("봉사활동 추가 목록 GET 실패", "${t.message}")
                    }

                })
            }
        } else {
            Log.d("봉사활동 목록 GET 실패", "no token")

        }
        return isSuccessful
    }

    fun applyService(token: String, serviceId: Int): Boolean {
        var applyResult = false
        RetrofitClient.createRetorfitClient().applyService(token, serviceId)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            applyResult = true
                        }
                        Log.d("봉사활동 신청 결과 코드", "${response.code()}")
                    } else {
                        Log.d("봉사활동 신청 실패", "${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("봉사활동 신청 실패 - 네트워크 오류", "${t.message}")
                }

            })
        return applyResult
    }

    fun cancelService(token: String, serviceId: Int): Boolean {
        var cancelResult = false
        RetrofitClient.createRetorfitClient().cancelApplyService(token, serviceId)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            cancelResult = true
                        }
                        Log.d("봉사활동 신청 취소 결과 코드", "${response.code()}")
                    } else {
                        Log.d("봉사활동 신청 취소 실패", "${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("봉사활동 신청 취소 실패 - 네트워크 오류", "${t.message}")
                }

            })
        return cancelResult
    }
}