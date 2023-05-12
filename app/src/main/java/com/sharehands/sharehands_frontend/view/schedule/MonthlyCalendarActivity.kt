package com.sharehands.sharehands_frontend.view.schedule

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.adapter.schedule.MonthlyServiceRVAdapter
import com.sharehands.sharehands_frontend.databinding.ActivityMonthlyCalendarBinding
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.schedule.calendar_decorator.*
import com.sharehands.sharehands_frontend.viewmodel.schedule.MonthlyCalendarViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log

class MonthlyCalendarActivity: AppCompatActivity() {
    lateinit var binding: ActivityMonthlyCalendarBinding
    private lateinit var viewModel: MonthlyCalendarViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_monthly_calendar)
        viewModel = ViewModelProvider(this).get(MonthlyCalendarViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val token = SharedPreferencesManager.getInstance(this).getString("token", "null")

        // adapter
        val rvMonthly = binding.rvSchedules

//        val adapter = MonthlyServiceRVAdapter(this, )

        // calendarView 초기화
        val calendar: MaterialCalendarView = binding.calendarMonthly
        // 초기화 함과 동시에 오늘 날짜를 선택하는 코드
        calendar.setSelectedDate(Calendar.getInstance())
        val timeCalendar = Calendar.getInstance()

        // 선택한 날짜 표시방식
        val selectedDayDecorate = SelectedDayDecorate(this@MonthlyCalendarActivity, calendar.selectedDate)
        calendar.addDecorator(selectedDayDecorate)

        // 토요일, 일요일 글자색상. 그리고 TodayDecorator도 해주어야 선택자의 흰색 배경에 가려지지 않는다.
        calendar.addDecorators(WeekdayDecorate(), SundayDecorate(), SaturdayDecorate(), TodayDecorator(this))
        //
        calendar.setTitleFormatter {
            val simpleDateFormat = SimpleDateFormat("yyyy년 MM월", Locale.KOREA)
            simpleDateFormat.format(timeCalendar.time)
        }

        // 초기 상태(오늘의 일정) 보여주기
        val today = calendar.selectedDate
        val todayYear = today.year.toString()
        val todayMonth = (today.month + 1).toString()
        val todayDay = today.day.toString()
        Log.d("today", "${todayYear}년 ${todayMonth}월 ${todayDay}일")

        if (token != "null") {
            viewModel.getMonthlyList(token, todayYear.toInt(), todayMonth.toInt())
            viewModel.monthlyList.observe(this) {
                if (viewModel.monthlyList.value!!.isNotEmpty()) {
                    val monthlyAdapter = MonthlyServiceRVAdapter(this, viewModel.monthlyList.value!!)
                    val rvLayoutManager = LinearLayoutManager(this)
                    binding.rvSchedules.apply {
                        adapter = monthlyAdapter
                        layoutManager = rvLayoutManager
                    }
                }
            }
        }


        // 선택된 date(날짜)가 변화될 때의 동작을 정의
        calendar.setOnDateChangedListener { widget, date, selected ->
            widget.selectionMode = MaterialCalendarView.SELECTION_MODE_SINGLE
            calendar.selectionMode = MaterialCalendarView.SELECTION_MODE_SINGLE

            // 선택한 날이 바뀌었을 때 선택한 날짜를 표시하는 것의 위치를 바꾸기 위함. 기존의 decorator를 무효화하고
            calendar.invalidateDecorators()
            calendar.removeDecorator(TodayDecorator(this@MonthlyCalendarActivity))

            // 다시 설정한다.
            val selectedDayDecorate = SelectedDayDecorate(this@MonthlyCalendarActivity, calendar.selectedDate)
            calendar.addDecorator(selectedDayDecorate)
            val year = date.year
            // 월의 경우, +1을 해주어야 함
            val month = date.month + 1
            val day = date.day
            Log.d("selected Date", "${year}년 ${month}월 ${day}일")

        }

        // 월이 바뀌었을 때
        calendar.setOnMonthChangedListener { widget, date ->
            calendar.setTitleFormatter {
                val simpleDateFormat = SimpleDateFormat("yyyy년 MM월", Locale.KOREA)
                simpleDateFormat.format(date.date)
            }
        }

        binding.tvViewDetail.setOnClickListener {
            val intent = Intent(this, DailyCalendarActivity::class.java)
            startActivity(intent)
        }

        BottomSheetBehavior.from(binding.viewScheduleMgt).apply {
            peekHeight = 600
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.btnBack.setOnClickListener {
            finish()
        }


    }
}