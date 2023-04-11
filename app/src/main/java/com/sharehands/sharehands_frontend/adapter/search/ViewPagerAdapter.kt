package com.sharehands.sharehands_frontend.adapter.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sharehands.sharehands_frontend.view.search.*

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle){
    override fun getItemCount(): Int {
        return 6
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SearchAllFragment()
            1 -> SearchEducationFragment()
            2 -> SearchCultureFragment()
            3 -> SearchHealthFragment()
            4 -> SearchEnvironmentFragment()
            5 -> SearchEtcFragment()
            else -> throw IllegalStateException("Invalid position $position")
        }
    }

}