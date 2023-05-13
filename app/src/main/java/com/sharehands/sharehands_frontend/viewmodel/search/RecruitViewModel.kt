package com.sharehands.sharehands_frontend.viewmodel.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.search.ApplicantsData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class RecruitViewModel: ViewModel() {
    private val _response = MutableLiveData<ApplicantsData>()
    val response: LiveData<ApplicantsData>
        get() = _response

    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean>
        get() = _isSuccessful

    private val _isSuggestSuccessful = MutableLiveData<Boolean>()
    val isSuggestSuccessful: LiveData<Boolean>
        get() = _isSuggestSuccessful

    private val _isSuggestCancelSuccessful = MutableLiveData<Boolean>()
    val isSuggestCancelSuccessful: LiveData<Boolean>
        get() = _isSuggestCancelSuccessful

    private val _isAllowSuccessful = MutableLiveData<Boolean>()
    val isAllowSuccessful: LiveData<Boolean>
        get() = _isAllowSuccessful



    fun getApplicantsData(token: String, serviceId: Long) {
        RetrofitClient.createRetorfitClient().getApplicants(token, serviceId)
            .enqueue(object : Callback<ApplicantsData> {
                override fun onResponse(
                    call: Call<ApplicantsData>,
                    response: Response<ApplicantsData>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            Log.d("지원자 불러오기 성공", "${response.body()}")
                            _response.value = response.body()
                            _isSuccessful.value = true
                        } else {
                            Log.d("지원자 불러오기 실패", "${response.code()}")
                            _isSuccessful.value = false
                        }

                    } else {
                        Log.d("지원자 불러오기 실패", "${response.code()}")
                        _isSuccessful.value = false
                    }
                }

                override fun onFailure(call: Call<ApplicantsData>, t: Throwable) {
                    Log.d("지원자 불러오기 실패", "${t.message}")
                    _isSuccessful.value = false
                }

            })
    }

    fun allowReview(token: String, userId: Long, workId: Long) {
        RetrofitClient.createRetorfitClient().allowReview(token, userId, workId)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.code() == 200) {
                        Log.d("리뷰 허용 성공", "${response.code()}")
                        _isAllowSuccessful.value = true
                    } else {
                        Log.d("리뷰 허용 실패", "${response.code()}")
                        _isAllowSuccessful.value = false
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("리뷰 허용 실패", "${t.message}")
                    _isAllowSuccessful.value = false
                }

            })
    }

    fun suggest(token: String, userId: Long, workId: Long) {
        RetrofitClient.createRetorfitClient().suggest(token, userId, workId)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.code() == 200) {
                        Log.d("제안 신청 성공", "${response.code()}")
                        _isSuggestSuccessful.value = true
                    } else {
                        Log.d("제안 신청 실패", "${response.code()}")
                        _isSuggestSuccessful.value = false
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("제안 신청 실패", "${t.message}")
                    _isSuggestSuccessful.value = false
                }

            })
    }

    fun cancelSuggest(token: String, userId: Long, workId: Long) {
        RetrofitClient.createRetorfitClient().cancelSuggest(token, userId, workId)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.code() == 200) {
                        Log.d("제안 취소 성공", "${response.code()}")
                        _isSuggestCancelSuccessful.value = true
                    } else {
                        Log.d("제안 취소 실패", "${response.code()}")
                        _isSuggestCancelSuccessful.value = false
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("제안 취소 실패", "${t.message}")
                    _isSuggestCancelSuccessful.value = false
                }

            })
    }
}