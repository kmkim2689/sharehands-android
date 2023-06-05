package com.sharehands.sharehands_frontend.viewmodel.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.search.Review
import com.sharehands.sharehands_frontend.network.search.ReviewDetail
import com.sharehands.sharehands_frontend.network.search.ReviewDetailItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewDetailViewModel: ViewModel() {
    private var _response = MutableLiveData<ArrayList<ReviewDetailItem>>()
    val response: LiveData<ArrayList<ReviewDetailItem>>
        get() = _response

    // 한번
    private var _result = MutableLiveData<ReviewDetail>()
    val result: LiveData<ReviewDetail>
        get() = _result

    // 누적
    var _reviewList = MutableLiveData<ArrayList<ReviewDetailItem>>(arrayListOf())
    val reviewList: LiveData<ArrayList<ReviewDetailItem>>
        get() = _reviewList

    private var _isInitialSuccessful = MutableLiveData<Boolean>()
    val isInitialSuccessful: LiveData<Boolean>
        get() = _isInitialSuccessful

    private var _isAdditionalSuccessful = MutableLiveData<Boolean>()
    val isAdditionalSuccessful: LiveData<Boolean>
        get() = _isAdditionalSuccessful

    fun getReviews(token: String, serviceId: Long) {
        RetrofitClient.createRetorfitClient().getReviews(token, serviceId)
            .enqueue(object : Callback<ReviewDetail> {
                override fun onResponse(
                    call: Call<ReviewDetail>,
                    response: Response<ReviewDetail>
                ) {
                    val result = response.body()
                    if (result != null) {
                        Log.d("review list initial", "${result.reviewLists}")
                        val reviewList = result.reviewLists
                        for (elem in reviewList) {
                            _reviewList.value?.add(elem)
                        }
                        _result.value = result!!
                        _response.value = _reviewList.value
                        Log.d("reviewList", "${_reviewList.value}")
                        _isInitialSuccessful.value = true
                    } else {
                        _isInitialSuccessful.value = false
                    }
                }

                override fun onFailure(call: Call<ReviewDetail>, t: Throwable) {
                    Log.d("초기 리뷰 불러오기 실패", "${t.message}")
                    _isInitialSuccessful.value = false
                }

            })
    }

    fun getReviewsAdditional(token: String, serviceId: Long, last: Int) {
        RetrofitClient.createRetorfitClient().getReviewsAdditional(token, serviceId, last)
            .enqueue(object : Callback<ReviewDetail> {
                override fun onResponse(
                    call: Call<ReviewDetail>,
                    response: Response<ReviewDetail>
                ) {
                    val result = response.body()
                    if (result != null) {
                        Log.d("review list additional", "${result.reviewLists}")
                        val reviewList = result.reviewLists
                        for (elem in reviewList) {
                            _reviewList.value?.add(elem)
                        }
                        Log.d("reviewList", "${_reviewList.value}")
                        _response.value = _reviewList.value
                        _isAdditionalSuccessful.value = true
                    } else {
                        _isAdditionalSuccessful.value = false
                    }
                }

                override fun onFailure(call: Call<ReviewDetail>, t: Throwable) {
                    Log.d("추가 리뷰 불러오기 실패", "${t.message}")
                    _isAdditionalSuccessful.value = false
                }

            })
    }
}