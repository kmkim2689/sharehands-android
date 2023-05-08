package com.sharehands.sharehands_frontend.adapter.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sharehands.sharehands_frontend.view.search.ServiceImageFragment
import com.sharehands.sharehands_frontend.viewmodel.search.ServiceDetailViewModel

class ServiceImageVPAdapter(fragmentActivity: FragmentActivity, private val imageUrls: List<String>?)
    :FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return imageUrls?.size!!
    }

    override fun createFragment(position: Int): Fragment {
        if (position < 0 || position >= imageUrls!!.size) {
            throw IllegalArgumentException("Invalid position $position")
        }

        return ServiceImageFragment(imageUrls[position]!!)
    }



}