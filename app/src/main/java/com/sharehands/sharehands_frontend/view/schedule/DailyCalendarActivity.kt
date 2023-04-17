package com.sharehands.sharehands_frontend.view.schedule

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.tlaabs.timetableview.Schedule
import com.github.tlaabs.timetableview.Time
import com.github.tlaabs.timetableview.TimetableView
import com.github.tlaabs.timetableview.TimetableView.OnStickerSelectedListener
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivityDailyCalendarBinding
import com.sharehands.sharehands_frontend.view.schedule.calendar_decorator.*
import com.sharehands.sharehands_frontend.view.search.ServiceDetailActivity
import java.text.SimpleDateFormat
import java.util.*

class DailyCalendarActivity: AppCompatActivity() {

    private lateinit var binding: ActivityDailyCalendarBinding

    private val day = arrayOf("x월 x일 봉사 계획")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_calendar)
        val binding = DataBindingUtil.setContentView<ActivityDailyCalendarBinding>(this, R.layout.activity_daily_calendar)

        val timeCalendar = Calendar.getInstance()
        val weeklyCalendar = binding.calendarWeekly
        weeklyCalendar.setSelectedDate(Calendar.getInstance())


        weeklyCalendar.addDecorators(WeekdayDecorate(), SundayDecorate(), SaturdayDecorate(), TodayDecorator(this))
        weeklyCalendar.setTitleFormatter {
            val simpleDateFormat = SimpleDateFormat("MM월 WW째주", Locale.KOREA)
            simpleDateFormat.format(timeCalendar.time)
        }

        // timetable 설정
        val timeTable: TimetableView = binding.viewTimeTable
        val schedules = ArrayList<Schedule>()

        // add schedule 시범
        // 1. Schedule 객체를 생성한다.
        val schedule1 = Schedule()
        schedule1.apply {
            classTitle = "봉사활동 뭐시기"
            classPlace = "한국외국어대학교"
            professorName = "닉네임"
            startTime = Time(9, 0)
            endTime = Time(13, 0)
        }
        schedules.add(schedule1)
        timeTable.add(schedules)

        // 클릭 이벤트
        timeTable.setOnStickerSelectEventListener(object : OnStickerSelectedListener {
            override fun OnStickerSelected(idx: Int, schedules: ArrayList<Schedule>?) {
                // 특정 인덱스의 스티커가 선택되었을 때... 여기서 인덱스란 schedules ArrayList를 이름.
                // TODO schedules arraylist 뿐만 아니라,
                // TODO schedule의 아이디를 담는 arraylist도 별도로 만들어 관리하는 것이 필요할듯

                val intent = Intent(this@DailyCalendarActivity, ServiceDetailActivity::class.java)
                intent.putExtra("id", "아이디")
                startActivity(intent)
            }

        })

        weeklyCalendar.setOnMonthChangedListener { widget, date ->
            weeklyCalendar.setTitleFormatter {
                val simpleDateFormat = SimpleDateFormat("MM월 WW째주", Locale.KOREA)
                simpleDateFormat.format(date.date)
            }
        }
        weeklyCalendar.setOnDateChangedListener { widget, date, selected ->
            weeklyCalendar.setTitleFormatter {
                val simpleDateFormat = SimpleDateFormat("MM월 WW째주", Locale.KOREA)
                simpleDateFormat.format(date.date)
            }
            widget.selectionMode = MaterialCalendarView.SELECTION_MODE_SINGLE
            weeklyCalendar.selectionMode = MaterialCalendarView.SELECTION_MODE_SINGLE
            Log.d("selected", "${selected}")
            weeklyCalendar.invalidateDecorators()
            weeklyCalendar.removeDecorator(TodayDecorator(this@DailyCalendarActivity))
            Log.d("changedDate", "${date}")
            var dateArrSplit1 = date.toString().split("{").get(1)
            var dateArrSplit2 = dateArrSplit1.split("}").get(0)
            var dateArrSplit3 = dateArrSplit2.split("-")
            var changedYear = dateArrSplit3?.get(0)?.toInt()
            // 주의 !!! 달은 0부터 시작한다. 따라서 month에 1을 더하여야 함.
            var changedMonth =  dateArrSplit3?.get(1)?.toInt()?.plus(1)
            var changedDay = dateArrSplit3?.get(2)?.toInt()
            Log.d("changed date", "${changedYear}, ${changedMonth}, ${changedDay}")

            val selectedDayDecorate = SelectedDayDecorate(this@DailyCalendarActivity, weeklyCalendar.selectedDate)
            weeklyCalendar.addDecorator(selectedDayDecorate)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
    }

}