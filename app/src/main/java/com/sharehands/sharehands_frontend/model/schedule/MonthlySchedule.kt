package com.sharehands.sharehands_frontend.model.schedule

data class MonthlySchedule(
    val month: Int?,
    val scheduleList: List<Schedule>
)

data class Schedule(
    val scheduleMonth: Int?,
    val scheduleDay: Int?,
    val scheduleName: String?,
    val dDay: Int?,
    val scheduleLocation: String?,
    // 시작시간과 종료시간을 따로 할 것인지?
    val time: String?
)