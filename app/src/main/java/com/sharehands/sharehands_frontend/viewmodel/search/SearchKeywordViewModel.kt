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
import org.apache.commons.lang3.mutable.Mutable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchKeywordViewModel: ViewModel() {

    private var _serviceSum = MutableLiveData<String>()
    val serviceSum: LiveData<String>
        get() = _serviceSum

    private var _servicesList = MutableLiveData<ArrayList<ServiceList>>( ArrayList<ServiceList>() )
    val servicesList: LiveData<ArrayList<ServiceList>>
        get() = _servicesList

    private var _keywordStr = MutableLiveData<String>()
    val keywordStr: LiveData<String>
        get() = _keywordStr

    private var _searchedKeyword = MutableLiveData<String>()
    val searchedKeyword: LiveData<String>
        get() = _searchedKeyword

    private var _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean>
        get() = _isSuccessful

    private var _isScrollSuccessful = MutableLiveData<Boolean>()
    val isScrollSuccessful: LiveData<Boolean>
        get() = _isScrollSuccessful

    private var _isReSearchSuccessful = MutableLiveData<Boolean>()
    val isResearchSuccessful: LiveData<Boolean>
        get() = _isReSearchSuccessful

    private var _isApplySuccessful = MutableLiveData<Boolean>()
    val isApplySuccessful: LiveData<Boolean>
        get() = _isApplySuccessful

    private var _isCancelSuccessful = MutableLiveData<Boolean>()
    val isCancelSuccessful: LiveData<Boolean>
        get() = _isCancelSuccessful

    private var _searchResult = MutableLiveData<SearchResult?>()
    val searchResult: LiveData<SearchResult?>
        get() = _searchResult


    // 검색 키워드
    fun onSearchKeywordChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        Log.d("changed text", "${s}")
        Log.d("keyword text count", "${count}")
        if (s.length == 0) {
            _searchedKeyword.value = ""
        } else {
            _searchedKeyword.value = s.toString()
        }
    }

    fun searchKeyword(token: String, keyword: String, page: Int) {
        _keywordStr.value = keyword
        Log.d("검색 keyword", "${keywordStr.value}")
        if (token != "null") {
            if (page == 1) {
                RetrofitClient.createRetorfitClient().getSearchResult(token, keyword)
                    .enqueue(object : Callback<SearchResult> {
                        override fun onResponse(
                            call: Call<SearchResult>,
                            response: Response<SearchResult>
                        ) {
                            Log.d("봉사활동 목록 검색 추가 GET response code", "${response.code()}")
                            if (response.code() == 200) {
                                val result = response.body()
                                if (result != null) {
                                    _searchResult.value = result
                                    _serviceSum.value = result.workCounter.toString()
                                    for (elem in result.serviceList) {
                                        _servicesList.value!!.add(elem)
                                    }
                                    Log.d("봉사활동 검색 목록 GET 성공", "${result}")
                                    Log.d("봉사활동 전체 목록", "${servicesList.value}")
                                    _isSuccessful.value = true
                                } else {
                                    Log.d("봉사활동 검색 목록 GET 실패", "데이터 비어있음")
                                    _isSuccessful.value = true
                                }
                            } else {
                                Log.d("봉사활동 검색 목록 GET 실패", "${response.code()}")
                                _isSuccessful.value = false
                            }
                        }

                        override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                            Log.d("봉사활동 검색 목록 GET 실패", "${t.message}")
                            _isSuccessful.value = false
                        }

                    })
            } else {
                Log.d("last id", "${servicesList.value?.get(servicesList.value!!.size - 1)!!.workId.toInt()}")
                RetrofitClient.createRetorfitClient().getSearchResultAdditional(
                    token,
                    keyword,
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
                                Log.d("service cnt scrolled", "${servicesList.value!!.size}")
                                Log.d("service scroll last", "${servicesList.value!!.last().workId}")
                                _serviceSum.value = result.workCounter.toString()
                                for (elem in result.serviceList) {
                                    _servicesList.value!!.add(elem)
                                }
                                Log.d("service count ${page}", "${result.serviceList.size}")
                                Log.d("봉사활동 목록 GET 성공", "${result}")
                                Log.d("봉사활동 전체 목록", "${servicesList.value}")
                                _isScrollSuccessful.value = true
                            } else {
                                Log.d("봉사뢀동 목록 GET 실패", "데이터 비어있음")
                                _isScrollSuccessful.value = false
                            }
                        } else {
                            Log.d("봉사활동 목록 GET 실패", "${response.code()}")
                            _isScrollSuccessful.value = false
                        }
                    }

                    override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                        Log.d("봉사활동 추가 목록 GET 실패", "${t.message}")
                        _isScrollSuccessful.value = false
                    }

                })
            }


        }  else {
            Log.d("봉사활동 목록 GET 실패", "no token")
            _isSuccessful.value = false
        }
    }

    fun applyService(token: String, serviceId: Int) {
        RetrofitClient.createRetorfitClient().applyService(token, serviceId.toLong())
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("지원 성공", "${response.code()}")
                        _isApplySuccessful.value = true
                    } else {
                        Log.d("지원 실패", "${response.code()}")
                        _isApplySuccessful.value = false
                    }

                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("지원 실패", "${t.message}")
                    _isApplySuccessful.value = false
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
}