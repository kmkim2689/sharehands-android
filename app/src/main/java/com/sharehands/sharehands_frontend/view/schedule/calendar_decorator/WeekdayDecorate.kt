package com.sharehands.sharehands_frontend.view.schedule.calendar_decorator

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.*

class WeekdayDecorate: DayViewDecorator {
    val calendar = Calendar.getInstance()

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        day?.copyTo(calendar)
        var weekday = calendar.get(Calendar.DAY_OF_WEEK)
        return ((weekday != Calendar.SUNDAY) && (weekday != Calendar.SATURDAY))
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(ForegroundColorSpan(Color.BLACK))
    }
}