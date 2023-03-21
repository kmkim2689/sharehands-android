package com.sharehands.sharehands_frontend.view.schedule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sharehands.sharehands_frontend.databinding.ActivityMonthlyCalendarBinding

class MonthlyCalendarActivity: AppCompatActivity() {
    lateinit var viewBinding: ActivityMonthlyCalendarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        viewBinding = ActivityMonthlyCalendarBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
    }
}