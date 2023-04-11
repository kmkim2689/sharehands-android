package com.sharehands.sharehands_frontend.view.schedule

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.FragmentScheduleBinding
import com.sharehands.sharehands_frontend.view.MainActivity


class ScheduleFragment : Fragment() {
    lateinit var binding: FragmentScheduleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentScheduleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val monthlyCalendarIntent = Intent(context as MainActivity, MonthlyCalendarActivity::class.java)
        binding.ivGotoCalendar.setOnClickListener {
            startActivity(monthlyCalendarIntent)
        }

        binding.tvServiceToday.setOnClickListener {
            startActivity(monthlyCalendarIntent)
        }

        binding.ivServiceToday.setOnClickListener {
            startActivity(monthlyCalendarIntent)
        }

    }

}