package com.sharehands.sharehands_frontend.viewmodel.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.mypage.CompletedServices
import com.sharehands.sharehands_frontend.network.mypage.RecruitedService
import com.sharehands.sharehands_frontend.network.mypage.RecruitedServices
import com.sharehands.sharehands_frontend.network.mypage.ScrapedServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class ServiceMgtViewModel: ViewModel() {
    var _isInitialized = MutableLiveData<Boolean>(true)

    private var _recruitedNum = MutableLiveData<String>()
    val recruitedNum: LiveData<String>
        get() = _recruitedNum

    private var _recruitedResult = MutableLiveData<RecruitedServices>()
    val recruitedResult: LiveData<RecruitedServices>
        get() = _recruitedResult

    var _recruitedServices = MutableLiveData<ArrayList<RecruitedService>>( ArrayList() )
    val recruitedServices: LiveData<ArrayList<RecruitedService>>
        get() = _recruitedServices

    private var _isRecruitedSuccessful = MutableLiveData<Boolean>()
    val isRecruitedSuccessful: LiveData<Boolean>
        get() = _isRecruitedSuccessful

    private var _appliedNum = MutableLiveData<String>()
    val appliedNum: LiveData<String>
        get() = _appliedNum

    private var _appliedResult = MutableLiveData<RecruitedServices>()
    val appliedResult: LiveData<RecruitedServices>
        get() = _appliedResult

    var _appliedServices = MutableLiveData<ArrayList<RecruitedService>>( ArrayList() )
    val appliedServices: LiveData<ArrayList<RecruitedService>>
        get() = _appliedServices

    private var _isAppliedServiceSuccessful = MutableLiveData<Boolean>()
    val isAppliedServiceSuccessful: LiveData<Boolean>
        get() = _isAppliedServiceSuccessful

    private var _completedNum = MutableLiveData<String>()
    val completedNum: LiveData<String>
        get() = _completedNum

    private var _completedResult = MutableLiveData<CompletedServices?>()
    val completedResult: LiveData<CompletedServices?>
        get() = _completedResult

    private var _completedServices = MutableLiveData<ArrayList<RecruitedService?>>( ArrayList() )
    val completedServices: LiveData<ArrayList<RecruitedService?>>
        get() = _completedServices

    private var _isCompletedSuccessful = MutableLiveData<Boolean>()
    val isCompletedSuccessful: LiveData<Boolean>
        get() = _isCompletedSuccessful

    private var _scrapedNum = MutableLiveData<String>()
    val scrapedNum: LiveData<String>
        get() = _scrapedNum

    private var _currScrapedNum = MutableLiveData<Int>(0)
    val currScrapedNum: LiveData<Int>
        get() = _currScrapedNum

    private var _scrapedResult = MutableLiveData<ScrapedServices>()
    val scrapedResult: LiveData<ScrapedServices>
        get() = _scrapedResult

    var _scrapedServices = MutableLiveData<ArrayList<RecruitedService?>>( ArrayList() )
    val scrapedServices: LiveData<ArrayList<RecruitedService?>>
        get() = _scrapedServices

    private var _isScrapSuccessful = MutableLiveData<Boolean>()
    val isScrapSuccessful: LiveData<Boolean>
        get() = _isScrapSuccessful

    private var _isApplySuccessful = MutableLiveData<Boolean>()
    val isApplySuccessful: LiveData<Boolean>
        get() = _isApplySuccessful

    private var _isDeleteSuccessful = MutableLiveData<Boolean>()
    val isDeleteSuccessful: LiveData<Boolean>
        get() = _isDeleteSuccessful

    private var _isCancelSuccessful = MutableLiveData<Boolean>()
    val isCancelSuccessful: LiveData<Boolean>
        get() = _isCancelSuccessful

    private var _isScrapCancelSuccessful = MutableLiveData<Boolean>()
    val isScrapCancelSuccessful: LiveData<Boolean>
        get() = _isScrapCancelSuccessful

    // 모집한봉사
    fun getRecruitedList(token: String, last: Int) {
        if (last == 0) {
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
                                _recruitedResult.value = result!!
                                _recruitedNum.value = result.serviceCounter.toString()
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
        } else {
            RetrofitClient.createRetorfitClient().getRecruitListAdditional(token, last)
                .enqueue(object : Callback<RecruitedServices> {
                    override fun onResponse(
                        call: Call<RecruitedServices>,
                        response: Response<RecruitedServices>
                    ) {
                        if (response.isSuccessful) {
                            val result = response.body()
                            _recruitedResult.value = result!!
                            if (result?.serviceList!!.isNotEmpty()) {
                                for (elem in result.serviceList) {
                                    _recruitedServices.value!!.add(elem)
                                }
                            }
                            _isRecruitedSuccessful.value = true
                        } else {
                            _isRecruitedSuccessful.value = false
                        }
                    }

                    override fun onFailure(call: Call<RecruitedServices>, t: Throwable) {
                        _isRecruitedSuccessful.value = false
                    }

                })
        }

    }

    fun getAppliedList(token: String, last: Int) {
        if (last == 0) {
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
                                _appliedResult.value = result!!
                                _appliedNum.value = result.serviceCounter.toString()
                                if (result.serviceList.isNotEmpty()) {
                                    for (elem in result.serviceList) {
                                        _appliedServices.value!!.add(elem)
                                    }
                                }


                                _isAppliedServiceSuccessful.value = true
                            } else {
                                _isAppliedServiceSuccessful.value = false
                            }
                        } else {
                            Log.d("모집한 봉사 호출 실패", "${response.code()}")
                            _isAppliedServiceSuccessful.value = false
                        }
                    }

                    override fun onFailure(call: Call<RecruitedServices>, t: Throwable) {
                        Log.d("모집한 봉사 호출 실패", "${t.message}")
                        _isAppliedServiceSuccessful.value = false
                    }

                })
        } else {
            RetrofitClient.createRetorfitClient().getAppliedListAdditional(token, last)
                .enqueue(object : Callback<RecruitedServices> {
                    override fun onResponse(
                        call: Call<RecruitedServices>,
                        response: Response<RecruitedServices>
                    ) {
                        if (response.isSuccessful) {
                            val result = response.body()
                            _appliedResult.value = result!!
                            if (result?.serviceList!!.isNotEmpty()) {
                                for (elem in result.serviceList) {
                                    _appliedServices.value!!.add(elem)
                                }
                            }
                            _isAppliedServiceSuccessful.value = true
                        } else {
                            _isAppliedServiceSuccessful.value = false
                        }
                    }

                    override fun onFailure(call: Call<RecruitedServices>, t: Throwable) {
                        _isAppliedServiceSuccessful.value = false
                    }

                })
        }

    }

    fun getCompletedList(token: String, last: Int) {
        if (last == 0) {
            RetrofitClient.createRetorfitClient().getCompleteList(token)
                .enqueue(object : Callback<CompletedServices> {
                    override fun onResponse(
                        call: Call<CompletedServices>,
                        response: Response<CompletedServices>
                    ) {
                        if (response.isSuccessful) {
                            Log.d("완료한 봉사 호출 성공", "${response.body()}")
                            val result = response.body()
                            _completedResult.value = result!!
                            if (result?.serviceList != null) {
                                _completedNum.value = result.serviceCounter.toString()
                                Log.d("완료 개수", "${completedNum.value}")
                                if (result.serviceList.size != 0) {
                                    for (elem in result.serviceList) {
                                        Log.d("completed service elem", "${elem}")
                                        _completedServices.value?.add(elem)
                                        Log.d("completed services list", "${completedServices.value}")
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

                    override fun onFailure(call: Call<CompletedServices>, t: Throwable) {
                        Log.d("완료한 봉사 호출 실패", "${t.message}")
                        _isCompletedSuccessful.value = false
                    }

                })
        } else {
            RetrofitClient.createRetorfitClient().getCompleteListAdditional(token, last)
                .enqueue(object : Callback<CompletedServices> {
                    override fun onResponse(
                        call: Call<CompletedServices>,
                        response: Response<CompletedServices>
                    ) {
                        if (response.isSuccessful) {
                            Log.d("완료한 봉사 추가 호출 성공", "${response.body()}")
                            val result = response.body()
                            _completedResult.value = result!!
                            if (result?.serviceList != null) {
                                _completedNum.value = result.serviceCounter.toString()
                                Log.d("완료 개수", "${completedNum.value}")
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

                    override fun onFailure(call: Call<CompletedServices>, t: Throwable) {
                        Log.d("완료한 봉사 호출 실패", "${t.message}")
                        _isCompletedSuccessful.value = false
                    }

                })
        }
    }

    fun getScrapedList(token: String, last: Int) {
        if (last == 0) {
            RetrofitClient.createRetorfitClient().getScrapedList(token)
                .enqueue(object : Callback<ScrapedServices> {
                    override fun onResponse(
                        call: Call<ScrapedServices>,
                        response: Response<ScrapedServices>
                    ) {
                        if (response.isSuccessful) {
                            Log.d("스크랩한 봉사 호출 성공", "${response.body()}")
                            val result = response.body()
                            if (result != null) {
                                _scrapedResult.value = result!!
                                _scrapedNum.value = result.serviceCounter.toString()
                                if (result.serviceList?.isNotEmpty() == true) {
                                    for (elem in result.serviceList) {
                                        _scrapedServices.value!!.add(elem)
                                    }
                                }


                                _isScrapSuccessful.value = true
                            } else {
                                _isScrapSuccessful.value = false
                            }
                        } else {
                            Log.d("모집한 봉사 호출 실패", "${response.code()}")
                            _isScrapSuccessful.value = false
                        }
                    }

                    override fun onFailure(call: Call<ScrapedServices>, t: Throwable) {
                        Log.d("모집한 봉사 호출 실패", "${t.message}")
                        _isScrapSuccessful.value = false
                    }

                })
        } else {
            RetrofitClient.createRetorfitClient().getScrapedListAdditional(token, last)
                .enqueue(object : Callback<ScrapedServices> {
                    override fun onResponse(
                        call: Call<ScrapedServices>,
                        response: Response<ScrapedServices>
                    ) {
                        if (response.isSuccessful) {
                            val result = response.body()
                            _scrapedResult.value = result!!
                            if (result?.serviceList!!.isNotEmpty()) {
                                for (elem in result.serviceList) {
                                    _scrapedServices.value!!.add(elem)
                                }
                            }
                            _isScrapSuccessful.value = true
                        } else {
                            _isScrapSuccessful.value = false
                        }
                    }

                    override fun onFailure(call: Call<ScrapedServices>, t: Throwable) {
                        _isScrapSuccessful.value = false
                    }

                })
        }
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

    fun cancelScrap(token: String, serviceId: Int) {
        RetrofitClient.createRetorfitClient().cancelScrap(token, serviceId.toLong())
            .enqueue(object : Callback<Void> {
                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    Log.d("스크랩 취소 성공", "${response.code()}")
                    _isScrapCancelSuccessful.value = true
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("스크랩 취소 실패", "${t.message}")
                    _isScrapCancelSuccessful.value = false
                }

            })
    }
}