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
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.adapter.search.ViewPagerAdapter
import com.sharehands.sharehands_frontend.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager: ViewPager2 = binding.viewpagerService
        val tabLayout = binding.layoutServiceTab
        val orderSpinner = binding.spinnerServiceCategory

        // ViewPager 어댑터를 설정하는 코드
        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    // 탭의 이름을 설정
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
                    tab.text = "기타"
                }

            }
        }.attach()

        val spinnerAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.service_order,
            android.R.layout.simple_spinner_item
        )

        // 드롭다운 시 레이아웃 설정
        spinnerAdapter.setDropDownViewResource(androidx.transition.R.layout.support_simple_spinner_dropdown_item)
        // address(spinner 뷰)에 만들어놓은 adapter를 할당한다.
        orderSpinner.adapter = spinnerAdapter
        orderSpinner.dropDownVerticalOffset = 120

        var orderBy = "최신순"

        orderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                orderBy = when (position) {
                    0 -> {
                        "최신순"
                    }
                    1 -> {
                        "좋아요순"
                    }
                    else -> {
                        "스크랩순"
                    }
                }
                Log.d("선택된 정렬 순서", orderBy)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무것도 선택되지 않는 경우는 발생하지 않으므로 비워둠
            }
        }

        binding.btnWriteService.setOnClickListener {
            val intent = Intent(requireContext(), ServiceWriteActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()


    }

}