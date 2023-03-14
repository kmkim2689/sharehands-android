package com.sharehands.sharehands_frontend

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kakao.sdk.common.util.Utility
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.sharehands.sharehands_frontend.databinding.ActivityMainBinding
import com.sharehands.sharehands_frontend.view.schedule_mgt.calendar_decorator.SaturdayDecorate
import com.sharehands.sharehands_frontend.view.schedule_mgt.calendar_decorator.SundayDecorate

class MainActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        Log.d(ContentValues.TAG, "keyhash:${Utility.getKeyHash(this)}")

        val monthlyCalendar: MaterialCalendarView = viewBinding.calendarMonthly
        // 설정된 날짜를 오늘로 설정

        monthlyCalendar.addDecorators(SundayDecorate(), SaturdayDecorate())

        monthlyCalendar.setOnDateChangedListener { widget, date, selected ->
            Log.d("changedDate", "${date}")
            var dateArrSplit1 = date.toString().split("{").get(1)
            var dateArrSplit2 = dateArrSplit1.split("}").get(0)
            var dateArrSplit3 = dateArrSplit2.split("-")
            var changedYear = dateArrSplit3?.get(0)?.toInt()
            // 주의 !!! 달은 0부터 시작한다. 따라서 month에 1을 더하여야 함.
            var changedMonth =  dateArrSplit3?.get(1)?.toInt()?.plus(1)
            var changedDay = dateArrSplit3?.get(2)?.toInt()
            Log.d("changed date", "${changedYear}, ${changedMonth}, ${changedDay}")
        }

        monthlyCalendar.selectedDate = CalendarDay.today()
        var selectedYear = monthlyCalendar.selectedDate.year
        var selectedMonth = monthlyCalendar.selectedDate.month
        var selectedDate = monthlyCalendar.selectedDate.date
        var selectedDay = monthlyCalendar.selectedDate.day

        Log.d("selectedYear", "${selectedYear}")
        Log.d("selectedMonth", "${selectedMonth}")
        Log.d("selectedDay", "${selectedDay}")
        Log.d("selectedWeekday", "${selectedDay}")

        var dateArr: List<String> = selectedDate.toString().split(" ")
        Log.d("dateArr", "${dateArr}")

    }
}