package com.sharehands.sharehands_frontend.viewmodel.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.search.SearchResult
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchKeywordViewModel: ViewModel() {

    private var _keywordStr = MutableLiveData<String>()
    val keywordStr: LiveData<String>
        get() = _keywordStr

    fun searchKeyword(token: String, keyword: String) {
        _keywordStr.value = keyword
        Log.d("search keyword", "${keywordStr.value}")
        if (token != "null") {
            RetrofitClient.createRetorfitClient().getSearchResult(token, keyword)
                .enqueue(object : Callback<SearchResult> {
                    override fun onResponse(
                        call: Call<SearchResult>,
                        response: Response<SearchResult>
                    ) {

                    }

                    override fun onFailure(call: Call<SearchResult>, t: Throwable) {

                    }

                })
        }

    }
}