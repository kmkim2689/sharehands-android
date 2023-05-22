package com.sharehands.sharehands_frontend.adapter.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sharehands.sharehands_frontend.network.home.SuggestedItem
import com.sharehands.sharehands_frontend.view.home.SuggestedServiceFragment

class SuggestionVPAdapter(fragmentActivity: FragmentActivity, private val serviceList: List<SuggestedItem>)
    :FragmentStateAdapter(fragmentActivity){
    override fun getItemCount(): Int {
        return serviceList.size
    }

    override fun createFragment(position: Int): Fragment {
        if (position < 0 || position >= serviceList.size) {
            throw IllegalArgumentException("invalid position $position")
        }

        return SuggestedServiceFragment(serviceList[position])
    }

}