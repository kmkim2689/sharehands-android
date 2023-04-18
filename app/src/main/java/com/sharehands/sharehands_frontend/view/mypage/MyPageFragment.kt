package com.sharehands.sharehands_frontend.view.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.adapter.mypage.MyPageMenuRVAdapter
import com.sharehands.sharehands_frontend.adapter.mypage.MyPageServiceRVAdapter
import com.sharehands.sharehands_frontend.databinding.FragmentMyPageBinding
import com.sharehands.sharehands_frontend.view.MainActivity

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val myPageMenuRVAdapter = MyPageMenuRVAdapter(requireContext() as MainActivity)
        binding.rvEtcService.adapter = myPageMenuRVAdapter
        binding.rvEtcService.layoutManager = LinearLayoutManager(requireContext() as MainActivity)

        val myPageServiceRVAdapter = MyPageServiceRVAdapter(requireContext() as MainActivity)
        binding.rvMgtService.adapter = myPageServiceRVAdapter
        binding.rvMgtService.layoutManager = LinearLayoutManager(requireContext() as MainActivity, LinearLayoutManager.HORIZONTAL, false)
    }

}