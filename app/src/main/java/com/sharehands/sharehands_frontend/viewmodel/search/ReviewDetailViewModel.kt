package com.sharehands.sharehands_frontend.viewmodel.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.search.ReviewDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewDetailViewModel: ViewModel() {
    private var _response = MutableLiveData<ReviewDetail>()
    val response: LiveData<ReviewDetail>
        get() = _response

    fun getReviews(token: String, serviceId: Long) {
        RetrofitClient.createRetorfitClient().getReviews(token, serviceId)
            .enqueue(object : Callback<ReviewDetail> {
                override fun onResponse(
                    call: Call<ReviewDetail>,
                    response: Response<ReviewDetail>
                ) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<ReviewDetail>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun getReviewsAdditional(token: String, serviceId: Long, last: Int) {

    }
}