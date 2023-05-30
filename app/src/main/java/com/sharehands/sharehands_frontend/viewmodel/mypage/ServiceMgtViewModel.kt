package com.sharehands.sharehands_frontend.viewmodel.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.mypage.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class ServiceMgtViewModel: ViewModel() {
    var _isInitialized = MutableLiveData<Boolean>(true)

    private var _isExpired = MutableLiveData<Boolean>()
    val isExpired: LiveData<Boolean>
        get() = _isExpired

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

    private var _suggestedNum = MutableLiveData<String>()
    val suggestedNum: LiveData<String>
        get() = _suggestedNum

    private var _suggestedResult = MutableLiveData<SuggestedServices?>()
    val suggestedResult: LiveData<SuggestedServices?>
        get() = _suggestedResult

    private var _suggestedServices = MutableLiveData<ArrayList<RecruitedService?>>( ArrayList() )
    val suggestedServices: LiveData<ArrayList<RecruitedService?>>
        get() = _suggestedServices

    private var _isSuggestedSuccessful = MutableLiveData<Boolean>()
    val isSuggestedSuccessful: LiveData<Boolean>
        get() = _isSuggestedSuccessful

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

    fun getSuggestedList(token: String, last: Int) {
        if (last == 0) {
            RetrofitClient.createRetorfitClient().getSuggestedList(token)
                .enqueue(object : Callback<SuggestedServices> {
                    override fun onResponse(
                        call: Call<SuggestedServices>,
                        response: Response<SuggestedServices>
                    ) {
                        if (response.isSuccessful) {
                            Log.d("제안받은 봉사 호출 성공", "${response.body()}")
                            val result = response.body()
                            _suggestedResult.value = result!!
                            if (result?.serviceList != null) {
                                _suggestedNum.value = result.serviceCounter.toString()
                                Log.d("완료 개수", "${suggestedNum.value}")
                                if (result.serviceList.size != 0) {
                                    for (elem in result.serviceList) {
                                        Log.d("suggested service elem", "${elem}")
                                        _suggestedServices.value?.add(elem)
                                        Log.d("suggested services list", "${suggestedServices.value}")
                                    }
                                }
                                _isSuggestedSuccessful.value = true
                            } else {
                                _isSuggestedSuccessful.value = false
                            }
                        } else {
                            Log.d("제안받은 봉사 호출 실패", "${response.code()}")
                            _isSuggestedSuccessful.value = false
                        }
                    }

                    override fun onFailure(call: Call<SuggestedServices>, t: Throwable) {
                        Log.d("제안받은 봉사 호출 실패", "${t.message}")
                        _isSuggestedSuccessful.value = false
                    }

                })
        } else {
            RetrofitClient.createRetorfitClient().getSuggestedListAdditional(token, last)
                .enqueue(object : Callback<SuggestedServices> {
                    override fun onResponse(
                        call: Call<SuggestedServices>,
                        response: Response<SuggestedServices>
                    ) {
                        if (response.isSuccessful) {
                            Log.d("제안받은 봉사 추가 호출 성공", "${response.body()}")
                            val result = response.body()
                            _suggestedResult.value = result!!
                            if (result?.serviceList != null) {
                                _suggestedNum.value = result.serviceCounter.toString()
                                Log.d("완료 개수", "${suggestedNum.value}")
                                if (result.serviceList.isNotEmpty()) {
                                    for (elem in result.serviceList) {
                                        _suggestedServices.value!!.add(elem)
                                    }

                                }
                                _isSuggestedSuccessful.value = true
                            } else {
                                _isSuggestedSuccessful.value = false
                            }
                        } else {
                            Log.d("제안받은 봉사 호출 실패", "${response.code()}")
                            _isSuggestedSuccessful.value = false
                        }

                    }

                    override fun onFailure(call: Call<SuggestedServices>, t: Throwable) {
                        Log.d("제안받은 봉사 호출 실패", "${t.message}")
                        _isSuggestedSuccessful.value = false
                    }

                })
        }
    }


    /*
    이 코드에서 acceptService 함수는 네트워크 통신 결과에 따라 result 값을 반환하는데, 현재는 항상 false를 반환하고 있습니다. 이는 enqueue 메서드가 비동기로 실행되며, 네트워크 요청이 완료되기 전에 result가 반환되기 때문입니다.

이를 개선하기 위해서는 콜백 대신 코루틴과 suspend 함수를 사용하여 비동기 처리를 직접 제어해야 합니다. 다음은 개선된 코드 예시입니다:
     */
    suspend fun acceptService(token: String, serviceId: Long): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.createRetorfitClient().acceptSuggestion(token, serviceId).execute()
                if (response.isSuccessful) {
                    Log.d("봉사활동 수락 성공", "${response.code()}")
                    true
                } else {
                    Log.d("봉사활동 수락 실패", "${response.code()}")
                    false
                }
            } catch (t: Throwable) {
                Log.d("봉사활동 수락 실패", "${t.message}")
                false
            }
        }
    }

    /*
    위의 코드에서는 enqueue 대신 execute 메서드를 사용하여 동기적으로 네트워크 요청을 수행하고, response.isSuccessful을 통해 성공 여부를 확인합니다. 또한, try-catch 문을 사용하여 예외가 발생한 경우에도 실패로 처리합니다.

이렇게 개선된 코드를 사용하면 네트워크 통신의 결과에 따라 올바른 값을 반환할 수 있습니다. 하지만 주의할 점은 이 함수를 호출하는 곳에서는 코루틴 스코프 내에서 호출되어야 한다는 것입니다.
     */

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