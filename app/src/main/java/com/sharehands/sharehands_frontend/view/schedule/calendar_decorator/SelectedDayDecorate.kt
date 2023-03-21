package com.sharehands.sharehands_frontend.view.schedule.calendar_decorator

import android.content.Context
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.sharehands.sharehands_frontend.R

class SelectedDayDecorate(context: Context, date: CalendarDay): DayViewDecorator {
    var dateSelected = date
    val drawable = context.resources.getDrawable(R.drawable.calendar_date_selector)
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day?.equals(dateSelected)!!
    }

    override fun decorate(view: DayViewFacade?) {
        view?.setSelectionDrawable(drawable)
    }
}