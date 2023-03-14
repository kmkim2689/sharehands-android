package com.sharehands.sharehands_frontend.view.schedule_mgt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sharehands.sharehands_frontend.databinding.ActivityWeeklyCalendarBinding

class WeeklyCalendarActivity: AppCompatActivity() {
    lateinit var viewBinding: ActivityWeeklyCalendarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        viewBinding = ActivityWeeklyCalendarBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
    }
}