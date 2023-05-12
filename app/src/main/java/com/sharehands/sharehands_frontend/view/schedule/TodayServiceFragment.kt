package com.sharehands.sharehands_frontend.view.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.FragmentTodayServiceBinding

class TodayServiceFragment(val time: String, val location: String, val title: String): Fragment() {
    private lateinit var binding: FragmentTodayServiceBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_today_service, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTodayTime.text = time
        binding.tvTodayLocation.text = location
        binding.tvTodayTitle.text = title
    }
}