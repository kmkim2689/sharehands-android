package com.sharehands.sharehands_frontend.view.schedule.calendar_decorator

import android.graphics.Color
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import com.sharehands.sharehands_frontend.model.schedule.MonthlyServiceDate
import com.sharehands.sharehands_frontend.view.schedule.MonthlyCalendarActivity

class EventDecorator(private val dates: ArrayList<MonthlyServiceDate>, context: MonthlyCalendarActivity): DayViewDecorator {
    val color = Color.RED


    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day != null && dates.any {
            it.year == day.year && it.month == (day.month + 1) && it.day == day.day
        }

    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(5f, color))
    }
}