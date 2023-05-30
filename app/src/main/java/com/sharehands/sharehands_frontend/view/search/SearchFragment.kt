package com.sharehands.sharehands_frontend.view.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.adapter.search.ViewPagerAdapter
import com.sharehands.sharehands_frontend.databinding.FragmentSearchBinding
import com.sharehands.sharehands_frontend.view.MainActivity
import com.sharehands.sharehands_frontend.viewmodel.search.ServiceSearchViewModel

class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding
    lateinit var viewModel: ServiceSearchViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(ServiceSearchViewModel::class.java)
        binding.lifecycleOwner = MainActivity()
        binding.viewModel = viewModel
        binding.nestedScrollView.isFillViewport = true
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()

        // 글쓰기 완료 시 바로 반영 하려면 onResume에 넣음
        val viewPager: ViewPager2 = binding.viewpagerService
        val tabLayout = binding.layoutServiceTab


        // ViewPager 어댑터 설정
        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    // 탭 이름 설정
                    tab.text = "전체"
                }
                1 -> {
                    tab.text = "교육"
                }
                2 -> {
                    tab.text = "문화"
                }
                3 -> {
                    tab.text = "보건"
                }
                4 -> {
                    tab.text = "환경"
                }
                5 -> {
                    tab.text = "기술"
                }
                6 -> {
                    tab.text= "기타"
                }

            }
        }.attach()

        binding.btnWriteService.setOnClickListener {
            val intent = Intent(requireContext(), ServiceWriteActivity::class.java)
            startActivity(intent)
        }

        binding.ivSearchService.setOnClickListener {
            val isSuccess = viewModel.searchKeyword()
            if (isSuccess) {
                // 화면 이동하기
                val intent = Intent(requireContext(), SearchResultActivity::class.java)
                intent.putExtra("keyword", viewModel.searchKeyword.value)
                Log.d("봉사활동 search keyword", viewModel.searchKeyword.value.toString())
                startActivity(intent)
            } else {
                // 스낵바 띄우기
                Snackbar.make(requireView(), "2글자 이상 입력해주세요.", Snackbar.LENGTH_SHORT).show()
            }
        }

    }

}