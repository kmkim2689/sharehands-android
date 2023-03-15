package com.sharehands.sharehands_frontend

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kakao.sdk.common.util.Utility
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_SINGLE
import com.prolificinteractive.materialcalendarview.MaterialCalendarView.SelectionMode
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import com.sharehands.sharehands_frontend.databinding.ActivityMainBinding
import com.sharehands.sharehands_frontend.view.schedule_mgt.calendar_decorator.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        val timeCalendar = Calendar.getInstance()

        Log.d(ContentValues.TAG, "keyhash:${Utility.getKeyHash(this)}")

        val monthlyCalendar: MaterialCalendarView = viewBinding.calendarMonthly
        // 설정된 날짜를 오늘로 설정

        monthlyCalendar.selectionMode = SELECTION_MODE_SINGLE


        monthlyCalendar.addDecorators(WeekdayDecorate(), SundayDecorate(), SaturdayDecorate(), TodayDecorator(this))

        // 처음 시작했을 때 년, 월을 한글로 표기하기 위함
        monthlyCalendar.setTitleFormatter(TitleFormatter {
            val simpleDateFormat = SimpleDateFormat("yyyy년 MM월", Locale.KOREA)
            simpleDateFormat.format(timeCalendar.time)

        })

        // 월을 이동했을 때 년, 월을 한글로 표기하기 위함.
        monthlyCalendar.setOnMonthChangedListener { widget, date ->
            monthlyCalendar.setTitleFormatter {
                val simpleDateFormat = SimpleDateFormat("yyyy년 MM월", Locale.KOREA)
                simpleDateFormat.format(date.date)
            }
        }

        // 요일을 한글로 표기하기 위하여 필요
        monthlyCalendar.setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays)))


        monthlyCalendar.setOnDateChangedListener { widget, date, selected ->
            widget.selectionMode = SELECTION_MODE_SINGLE
            monthlyCalendar.selectionMode = SELECTION_MODE_SINGLE
            Log.d("selected", "${selected}")
            monthlyCalendar.invalidateDecorators()
            monthlyCalendar.removeDecorator(TodayDecorator(this))
            Log.d("changedDate", "${date}")
            var dateArrSplit1 = date.toString().split("{").get(1)
            var dateArrSplit2 = dateArrSplit1.split("}").get(0)
            var dateArrSplit3 = dateArrSplit2.split("-")
            var changedYear = dateArrSplit3?.get(0)?.toInt()
            // 주의 !!! 달은 0부터 시작한다. 따라서 month에 1을 더하여야 함.
            var changedMonth =  dateArrSplit3?.get(1)?.toInt()?.plus(1)
            var changedDay = dateArrSplit3?.get(2)?.toInt()
            Log.d("changed date", "${changedYear}, ${changedMonth}, ${changedDay}")

            val selectedDayDecorate = SelectedDayDecorate(this@MainActivity, monthlyCalendar.selectedDate)
            monthlyCalendar.addDecorator(selectedDayDecorate)

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