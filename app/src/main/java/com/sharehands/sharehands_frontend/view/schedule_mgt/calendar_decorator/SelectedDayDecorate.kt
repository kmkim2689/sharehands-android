package com.sharehands.sharehands_frontend.view.schedule_mgt.calendar_decorator

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.sharehands.sharehands_frontend.R
import java.util.*

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