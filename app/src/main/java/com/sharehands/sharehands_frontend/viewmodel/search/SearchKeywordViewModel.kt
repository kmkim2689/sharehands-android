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

    private var _serviceSum = MutableLiveData<Int>()
    val serviceSum: LiveData<Int>
        get() = _serviceSum

    private var _servicesList = MutableLiveData<ArrayList<ServiceList>>( ArrayList<ServiceList>() )
    val servicesList: LiveData<ArrayList<ServiceList>>
        get() = _servicesList

    private var _keywordStr = MutableLiveData<String>()
    val keywordStr: LiveData<String>
        get() = _keywordStr

    fun searchKeyword(token: String, keyword: String): Boolean {
        var isSuccessful = false
        _keywordStr.value = keyword
        Log.d("검색 keyword", "${keywordStr.value}")
        if (token != "null") {
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
                                _serviceSum.value = result.workCounter.toInt()
                                for (elem in result.serviceList) {
                                    _servicesList.value!!.add(elem)
                                }
                                Log.d("봉사활동 검색 목록 GET 성공", "${result}")
                                Log.d("봉사활동 전체 목록", "${servicesList.value}")
                                isSuccessful = true
                            } else {
                                Log.d("봉사활동 검색 목록 GET 실패", "데이터 비어있음")
                            }
                        } else {
                            Log.d("봉사활동 검색 목록 GET 실패", "${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                        Log.d("봉사활동 검색 목록 GET 실패", "${t.message}")
                    }

                })

        }
        return isSuccessful
    }
}