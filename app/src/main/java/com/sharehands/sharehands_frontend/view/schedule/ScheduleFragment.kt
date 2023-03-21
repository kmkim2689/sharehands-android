package com.sharehands.sharehands_frontend.view.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.FragmentScheduleBinding


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

}