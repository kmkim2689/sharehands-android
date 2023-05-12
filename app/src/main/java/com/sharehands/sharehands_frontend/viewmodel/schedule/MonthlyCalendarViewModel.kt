package com.sharehands.sharehands_frontend.viewmodel.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sharehands.sharehands_frontend.model.schedule.MonthlyServiceDate
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.schedule.MonthlyService
import com.sharehands.sharehands_frontend.network.schedule.MonthlyServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MonthlyCalendarViewModel: ViewModel() {
    private var _monthlyList = MutableLiveData<List<MonthlyService>>()
    val monthlyList: LiveData<List<MonthlyService>>
        get() = _monthlyList

    private var _monthlyDates = MutableLiveData<ArrayList<MonthlyServiceDate>>(ArrayList())
    val monthlyDates: LiveData<ArrayList<MonthlyServiceDate>>
        get() = _monthlyDates

    fun getMonthlyList(token: String, year: Int, month: Int) {
        RetrofitClient.createRetorfitClient().getMonthlyService(token, year, month)
            .enqueue(object : Callback<MonthlyServices> {
                override fun onResponse(
                    call: Call<MonthlyServices>,
                    response: Response<MonthlyServices>
                ) {
                    val result = response.body()
                    if (result == null) {

                    } else {

                        for (elem in result.workList) {
                            _monthlyDates.value?.add(MonthlyServiceDate(elem.year, elem.month, elem.day))
                        }
                        _monthlyList.value = result.workList

                    }
                }

                override fun onFailure(call: Call<MonthlyServices>, t: Throwable) {
                }

            })
    }
}