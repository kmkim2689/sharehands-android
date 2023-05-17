package com.sharehands.sharehands_frontend.view.schedule

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.github.tlaabs.timetableview.Schedule
import com.github.tlaabs.timetableview.Time
import com.github.tlaabs.timetableview.TimetableView
import com.github.tlaabs.timetableview.TimetableView.OnStickerSelectedListener
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivityDailyCalendarBinding
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.schedule.DailyServices
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.schedule.calendar_decorator.*
import com.sharehands.sharehands_frontend.view.search.ServiceDetailActivity
import com.sharehands.sharehands_frontend.viewmodel.schedule.DailyCalendarViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class DailyCalendarActivity: AppCompatActivity() {

    private lateinit var binding: ActivityDailyCalendarBinding

    private val day = arrayOf("x월 x일 봉사 계획")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_calendar)
        val binding = DataBindingUtil.setContentView<ActivityDailyCalendarBinding>(this, R.layout.activity_daily_calendar)

        val viewModel = ViewModelProvider(this).get(DailyCalendarViewModel::class.java)
        val token = SharedPreferencesManager.getInstance(this).getString("token", "null")

        val timeCalendar = Calendar.getInstance()
        val weeklyCalendar = binding.calendarWeekly
        weeklyCalendar.setSelectedDate(Calendar.getInstance())

        val idList = ArrayList<Int>()

        weeklyCalendar.addDecorators(WeekdayDecorate(), SundayDecorate(), SaturdayDecorate(), TodayDecorator(this))
        weeklyCalendar.setTitleFormatter {
            val simpleDateFormat = SimpleDateFormat("yyyy년 MM월", Locale.KOREA)
            simpleDateFormat.format(timeCalendar.time)
        }

        val today = weeklyCalendar.selectedDate
        val todayYear = today.year
        val todayMonth = today.month + 1
        val todayDay = today.day

        binding.tvScheduleTitle.text = "${todayMonth}월 ${todayDay}일"

        viewModel.getDailyServices(token, todayYear, todayMonth, todayDay)
        viewModel.dailyList.observe(this) {
            val timeTable: TimetableView = binding.viewTimeTable

            timeTable.setBackgroundColor(resources.getColor(R.color.item_color))
            val schedules = ArrayList<Schedule>()
            for (elem in viewModel.dailyList.value!!) {
                idList.add(elem.workId.toInt())
                val schedule = Schedule()
                val start = elem.startTime.split(" : ")
                val end = elem.endTime.split(" : ")
                schedule.apply {
                    classTitle = elem.workName
                    classPlace = elem.location
                    professorName = elem.nickname
                    if (start.size == 2 && end.size == 2) {
                        startTime = Time(start[0].toInt(), start[1].toInt())
                        endTime = Time(end[0].toInt(), end[1].toInt())
                    }
                }
                schedules.add(schedule)
                timeTable.add(schedules)
            }

            // 클릭 이벤트
            timeTable.setOnStickerSelectEventListener(object : OnStickerSelectedListener {
                override fun OnStickerSelected(idx: Int, schedules: ArrayList<Schedule>?) {
                    Log.d("schedules", "${schedules}")
                    Log.d("schedules idx elem", "${idx}")
                    // 특정 인덱스의 스티커가 선택되었을 때... 여기서 인덱스란 schedules ArrayList를 이름.
                    // TODO schedules arraylist 뿐만 아니라,
                    // TODO schedule의 아이디를 담는 arraylist도 별도로 만들어 관리하는 것이 필요할듯

                    val intent = Intent(this@DailyCalendarActivity, ServiceDetailActivity::class.java)
                    // TODO 서비스 아이디 넘겨주는 것 바꿔놓기
                    intent.putExtra("serviceId", idList[idx])
                    startActivity(intent)
                }

            })

        }

        // timetable 설정


        // add schedule 시범
        // 1. Schedule 객체를 생성한다.
//        val schedule1 = Schedule()
//        schedule1.apply {
//            classTitle = "봉사활동 뭐시기"
//            classPlace = "한국외국어대학교"
//            professorName = "닉네임"
//            startTime = Time(9, 0)
//            endTime = Time(13, 0)
//        }




        weeklyCalendar.setOnMonthChangedListener { widget, date ->
            weeklyCalendar.setTitleFormatter {
                val simpleDateFormat = SimpleDateFormat("yyyy년 MM월", Locale.KOREA)
                simpleDateFormat.format(date.date)
            }
        }

        weeklyCalendar.setOnDateChangedListener { widget, date, selected ->
            weeklyCalendar.setTitleFormatter {
                val simpleDateFormat = SimpleDateFormat("yyyy년 MM월", Locale.KOREA)
                simpleDateFormat.format(date.date)
            }

            // 아이디 리스트 비우기
            idList.clear()

            widget.selectionMode = MaterialCalendarView.SELECTION_MODE_SINGLE
            weeklyCalendar.selectionMode = MaterialCalendarView.SELECTION_MODE_SINGLE
            Log.d("selected", "${selected}")
            weeklyCalendar.invalidateDecorators()
            weeklyCalendar.removeDecorator(TodayDecorator(this@DailyCalendarActivity))
            Log.d("changedDate", "${date}")

            var changedYear = date.year
            // 주의 !!! 달은 0부터 시작한다. 따라서 month에 1을 더하여야 함.
            var changedMonth =  date.month + 1
            var changedDay = date.day
            Log.d("changed", "${changedYear}, ${changedMonth}, ${changedDay}")

            binding.tvScheduleTitle.text = "${changedMonth}월 ${changedDay}일"
            val selectedDayDecorate = SelectedDayDecorate(this@DailyCalendarActivity, weeklyCalendar.selectedDate)
            weeklyCalendar.addDecorator(selectedDayDecorate)

            viewModel.getDailyServices(token, changedYear, changedMonth, changedDay)
            viewModel.dailyList.observe(this) {
                val timeTable: TimetableView = binding.viewTimeTable
                val schedules = ArrayList<Schedule>()
                timeTable.removeAll()
                for (elem in viewModel.dailyList.value!!) {
                    idList.add(elem.workId.toInt())
                    val schedule = Schedule()
                    val start = elem.startTime.split(" : ")
                    val end = elem.endTime.split(" : ")
                    schedule.apply {
                        classTitle = elem.workName
                        classPlace = elem.location
                        professorName = elem.nickname
                        if (start.size == 2 && end.size == 2) {
                            startTime = Time(start[0].toInt(), start[1].toInt())
                            endTime = Time(end[0].toInt(), end[1].toInt())
                        }
                    }
                    schedules.add(schedule)
                    timeTable.add(schedules)
                }

                // 클릭 이벤트
                timeTable.setOnStickerSelectEventListener(object : OnStickerSelectedListener {
                    override fun OnStickerSelected(idx: Int, schedules: ArrayList<Schedule>?) {
                        Log.d("schedules", "${schedules.toString()}")
                        Log.d("schedules idx elem", "${idx}")
                        // 특정 인덱스의 스티커가 선택되었을 때... 여기서 인덱스란 schedules ArrayList를 이름.
                        // TODO schedules arraylist 뿐만 아니라,
                        // TODO schedule의 아이디를 담는 arraylist도 별도로 만들어 관리하는 것이 필요할듯

                        val intent = Intent(this@DailyCalendarActivity, ServiceDetailActivity::class.java)
                        // TODO 서비스 아이디 넘겨주는 것 바꿔놓기
                        intent.putExtra("serviceId", idList[idx])
                        startActivity(intent)
                    }

                })

            }

        }

        binding.btnBack.setOnClickListener {
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
    }

}