package com.sharehands.sharehands_frontend.viewmodel.schedule

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.schedule.DailyService
import com.sharehands.sharehands_frontend.network.schedule.DailyServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DailyCalendarViewModel: ViewModel() {
    private var _dailyResult = MutableLiveData<DailyServices>()
    val dailyResult: LiveData<DailyServices>
        get() = _dailyResult

    private var _dailyList = MutableLiveData<List<DailyService>>(ArrayList())
    val dailyList: LiveData<List<DailyService>>
        get() = _dailyList

    private var _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean>
        get() = _isSuccessful

    fun getDailyServices(token: String, year: Int, month: Int, day: Int) {
        RetrofitClient.createRetorfitClient().getDailyService(token, year, month, day)
            .enqueue(object : Callback<DailyServices> {
                override fun onResponse(
                    call: Call<DailyServices>,
                    response: Response<DailyServices>
                ) {
                    val result = response.body()
                    if (result != null) {
                        _dailyList.value = result.workList
                    }
                }

                override fun onFailure(call: Call<DailyServices>, t: Throwable) {
                    Log.d("일별계획 불러오기 실패", "${t.message}")
                }

            })

    }
}