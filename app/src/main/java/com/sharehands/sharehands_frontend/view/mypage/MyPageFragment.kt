package com.sharehands.sharehands_frontend.view.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.FragmentMyPageBinding

class MyPageFragment : Fragment() {
    lateinit var binding: FragmentMyPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyPageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}