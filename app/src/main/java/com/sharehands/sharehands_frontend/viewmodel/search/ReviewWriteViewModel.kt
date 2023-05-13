package com.sharehands.sharehands_frontend.viewmodel.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.search.ReviewUpload
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewWriteViewModel: ViewModel() {
    private var _achievement = MutableLiveData<Double>(0.0)
    val achievement: LiveData<Double>
        get() = _achievement

    private var _traffic = MutableLiveData<Double>(0.0)
    val traffic: LiveData<Double>
        get() = _traffic

    private var _system = MutableLiveData<Double>(0.0)
    val system: LiveData<Double>
        get() = _system

    private var _content = MutableLiveData<String>("")
    val content: LiveData<String>
        get() = _content

    private var _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean>
        get() = _isSuccessful

    fun onAchievementChanged(rating: Double) {
        _achievement.value = rating
    }

    fun onTrafficChanged(rating: Double) {
        _traffic.value = rating
    }

    fun onSystemChanged(rating: Double) {
        _system.value = rating
    }

//    fun onContentChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//        _content.value = s.toString()
//        Log.d("content", "${content}")
//    }

    fun uploadReview(token: String, serviceId: Long, content: String) {
        val body = ReviewUpload(achievement.value!!, traffic.value!!, system.value!!, content)
        RetrofitClient.createRetorfitClient().uploadReview(token, serviceId, body)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.code() == 200) {
                        Log.d("리뷰 업로드 성공", "${response.code()}")
                        _isSuccessful.value = true
                    } else {
                        Log.d("리뷰 업로드 실패", "${response.code()}")
                        _isSuccessful.value = false
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("리뷰 업로드 실패", "${t.message}")
                    _isSuccessful.value = false
                }

            })
    }
}