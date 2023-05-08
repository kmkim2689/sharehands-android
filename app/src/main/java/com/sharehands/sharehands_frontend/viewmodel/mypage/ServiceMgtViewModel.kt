package com.sharehands.sharehands_frontend.viewmodel.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.mypage.RecruitedService
import com.sharehands.sharehands_frontend.network.mypage.RecruitedServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceMgtViewModel: ViewModel() {
    private var _recruitedNum = MutableLiveData<Int>()
    val recruitedNum: LiveData<Int>
        get() = _recruitedNum

    private var _recruitedServices = MutableLiveData<ArrayList<RecruitedService>>( ArrayList() )
    val recruitedServices: LiveData<ArrayList<RecruitedService>>
        get() = _recruitedServices

    private var _isRecruitedSuccessful = MutableLiveData<Boolean>()
    val isRecruitedSuccessful: LiveData<Boolean>
        get() = _isRecruitedSuccessful

    private var _appliedNum = MutableLiveData<Int>()
    val appliedNum: LiveData<Int>
        get() = _appliedNum

    private var _appliedServices = MutableLiveData<ArrayList<RecruitedService>>( ArrayList() )
    val appliedServices: LiveData<ArrayList<RecruitedService>>
        get() = _appliedServices

    private var _isAppliedServiceSuccessful = MutableLiveData<Boolean>()
    val isAppliedServiceSuccessful: LiveData<Boolean>
        get() = _isAppliedServiceSuccessful

    private var _completedNum = MutableLiveData<Int>()
    val completedNum: LiveData<Int>
        get() = _completedNum

    private var _completedServices = MutableLiveData<ArrayList<RecruitedService>>( ArrayList() )
    val completedServices: LiveData<ArrayList<RecruitedService>>
        get() = _appliedServices

    private var _isCompletedSuccessful = MutableLiveData<Boolean>()
    val isCompletedSuccessful: LiveData<Boolean>
        get() = _isCompletedSuccessful

    private var _isApplySuccessful = MutableLiveData<Boolean>()
    val isApplySuccessful: LiveData<Boolean>
        get() = _isApplySuccessful

    private var _isDeleteSuccessful = MutableLiveData<Boolean>()
    val isDeleteSuccessful: LiveData<Boolean>
        get() = _isDeleteSuccessful

    private var _isCancelSuccessful = MutableLiveData<Boolean>()
    val isCancelSuccessful: LiveData<Boolean>
        get() = _isCancelSuccessful

    // 모집한봉사
    fun getRecruitedList(token: String) {
        RetrofitClient.createRetorfitClient().getRecruitList(token)
            .enqueue(object : Callback<RecruitedServices> {
                override fun onResponse(
                    call: Call<RecruitedServices>,
                    response: Response<RecruitedServices>
                ) {
                    if (response.isSuccessful) {
                        Log.d("모집한 봉사 호출 성공", "${response.body()}")
                        val result = response.body()
                        if (result != null) {
                            _recruitedNum.value = result.serviceCounter.toInt()
                            if (result.serviceList.isNotEmpty()) {
                                for (elem in result.serviceList) {
                                    _recruitedServices.value!!.add(elem)
                                }
                            }


                            _isRecruitedSuccessful.value = true
                        } else {
                            _isRecruitedSuccessful.value = false
                        }
                    } else {
                        Log.d("모집한 봉사 호출 실패", "${response.code()}")
                        _isRecruitedSuccessful.value = false
                    }
                }

                override fun onFailure(call: Call<RecruitedServices>, t: Throwable) {
                    Log.d("모집한 봉사 호출 실패", "${t.message}")
                    _isRecruitedSuccessful.value = false
                }

            })
    }

    fun getAppliedList(token: String) {
        RetrofitClient.createRetorfitClient().getAppliedList(token)
            .enqueue(object : Callback<RecruitedServices> {
                override fun onResponse(
                    call: Call<RecruitedServices>,
                    response: Response<RecruitedServices>
                ) {
                    if (response.isSuccessful) {
                        Log.d("지원한 봉사 호출 성공", "${response.body()}")
                        val result = response.body()
                        if (result != null) {
                            _appliedNum.value = result.serviceCounter.toInt()
                            if (result.serviceList.isNotEmpty()) {
                                for (elem in result.serviceList) {
                                    Log.d("추가됨", "${elem}")
                                    _appliedServices.value!!.add(elem)
                                }

                            }
                            _isAppliedServiceSuccessful.value = true

                        } else {
                            _isAppliedServiceSuccessful.value = false
                        }
                    } else {
                        Log.d("지원한 봉사 호출 실패", "${response.code()}")
                        _isAppliedServiceSuccessful.value = false
                    }
                }

                override fun onFailure(call: Call<RecruitedServices>, t: Throwable) {
                    Log.d("지원한 봉사 호출 실패", "${t.message}")
                    _isAppliedServiceSuccessful.value = false
                }

            })
    }

    fun getCompletedList(token: String) {
        RetrofitClient.createRetorfitClient().getCompleteList(token)
            .enqueue(object : Callback<RecruitedServices> {
                override fun onResponse(
                    call: Call<RecruitedServices>,
                    response: Response<RecruitedServices>
                ) {
                    if (response.isSuccessful) {
                        Log.d("완료한 봉사 호출 성공", "${response.body()}")
                        val result = response.body()
                        if (result != null) {
                            _completedNum.value = result.serviceCounter.toInt()
                            if (result.serviceList.isNotEmpty()) {
                                for (elem in result.serviceList) {
                                    _completedServices.value!!.add(elem)
                                }
                            }
                            _isCompletedSuccessful.value = true
                        } else {
                            _isCompletedSuccessful.value = false
                        }
                    } else {
                        Log.d("완료한 봉사 호출 실패", "${response.code()}")
                        _isCompletedSuccessful.value = false
                    }

                }

                override fun onFailure(call: Call<RecruitedServices>, t: Throwable) {
                    Log.d("완료한 봉사 호출 실패", "${t.message}")
                    _isCompletedSuccessful.value = false
                }

            })


    }
    fun cancelService(token: String, serviceId: Int) {
        RetrofitClient.createRetorfitClient().cancelApplyService(token, serviceId.toLong())
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("지원 취소 성공", "${response.code()}")
                        _isCancelSuccessful.value = true
                    } else {
                        Log.d("지원 취소 실패", "${response.code()}")
                        _isCancelSuccessful.value = false
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("지원 취소 실패", "${t.message}")
                    _isCancelSuccessful.value = false
                }

            })
    }

    fun deleteService(token: String, serviceId: Int) {
        RetrofitClient.createRetorfitClient().deleteService(token, serviceId.toLong())
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("봉사활동 삭제 성공", "${response.code()}")
                        _isDeleteSuccessful.value = true
                    } else {
                        _isDeleteSuccessful.value = false
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("봉사활동 삭제 실패", "${t.message}")
                    _isDeleteSuccessful.value = false
                }

            })
    }
}