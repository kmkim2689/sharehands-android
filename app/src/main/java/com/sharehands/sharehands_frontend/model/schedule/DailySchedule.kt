package com.sharehands.sharehands_frontend.model.schedule

data class DailySchedule(
    val month: Int?,
    val day: Int?,
    val scheduleList: List<DaySchedule>
)

data class DaySchedule(
    val volunteerId: Int?,
    val title: String?,
    val user: String?,
    val location: String?
)
