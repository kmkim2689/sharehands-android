package com.sharehands.sharehands_frontend.adapter.schedule

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sharehands.sharehands_frontend.network.schedule.TodayService
import com.sharehands.sharehands_frontend.view.schedule.ScheduleFragment
import com.sharehands.sharehands_frontend.view.schedule.TodayServiceFragment

class TodayServiceVPAdapter(fragmentActivity: FragmentActivity, private val servicesList: List<TodayService>)
    : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return servicesList.size
    }

    override fun createFragment(position: Int): Fragment {
        if (position < 0 || position >= servicesList!!.size) {
            throw IllegalArgumentException("Invalid position $position")
        }

        return TodayServiceFragment(servicesList[position].time!!, servicesList[position].location!!,
            servicesList[position].workName!!)
    }


}