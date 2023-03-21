package com.sharehands.sharehands_frontend.view.schedule.calendar_decorator

import android.content.Context
import android.util.Log
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.sharehands.sharehands_frontend.R

class TodayDecorator(context: Context): DayViewDecorator {
    private var today = CalendarDay.today()
    private val drawable = context.resources.getDrawable(R.drawable.calendar_date_selector)
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day?.equals(today)!!

    }

    override fun decorate(view: DayViewFacade?) {
        view?.setSelectionDrawable(drawable)
        Log.d("today", "decorated")
    }
}