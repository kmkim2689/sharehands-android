package com.sharehands.sharehands_frontend.view.mypage

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.adapter.mypage.RecruitedServiceRVAdapter
import com.sharehands.sharehands_frontend.adapter.search.SearchKeywordRVAdapter
import com.sharehands.sharehands_frontend.databinding.ActivityRecruitedServiceBinding
import com.sharehands.sharehands_frontend.databinding.ActivitySearchResultBinding
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.viewmodel.mypage.ServiceMgtViewModel
import com.sharehands.sharehands_frontend.viewmodel.search.SearchKeywordViewModel

class RecruitedServiceActivity: AppCompatActivity() {
    private lateinit var binding: ActivityRecruitedServiceBinding
    private lateinit var viewModel: ServiceMgtViewModel
    private lateinit var adapter: RecruitedServiceRVAdapter
    private lateinit var layoutManager: LinearLayoutManager
    var page = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recruited_service)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recruited_service)

        val viewModel = ViewModelProvider(this).get(ServiceMgtViewModel::class.java)
        val token = SharedPreferencesManager.getInstance(this).getString("token", "null")
        val recyclerView = binding.rvResultRecruited


        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
//        recyclerView.adapter = adapter

        getServices(token, viewModel)
    }


    override fun onResume() {
        super.onResume()

    }

    fun getServices(token: String, viewModel: ServiceMgtViewModel) {
//        isLoading = true
        // 당장 데이터 가져오는 것을 요청한 직후에는 프로그레스 바가 보여야 함
        binding.progressRecruited.visibility = View.VISIBLE
        // TODO 뷰모델로 네트워크 요청. 네트워크 통신을 통해 가져온 페이지
        // TODO 만약 GET 성공하면, (result.isSuccessful) 핸들러 실행
        viewModel.getRecruitedList(token)
        Log.d("봉사활동 서비스 불러오기 성공 여부", "${viewModel.isRecruitedSuccessful.value}")

        viewModel.isRecruitedSuccessful.observe(this) {
            binding.tvTotalRecruited.text = "총 ${viewModel.recruitedNum.value}개의 봉사를 모집했습니다."
            if (viewModel.isRecruitedSuccessful.value == true) {
                Handler().postDelayed({
                    if (::adapter.isInitialized) {
//                        adapter = ServicesSearchRVAdapter(contextActivity as MainActivity, viewModel, viewModel.servicesList.value)
//                        binding.rvResultAll.adapter = adapter
//                        binding.rvResultAll.layoutManager = layoutManager
                        adapter.notifyDataSetChanged()
//                        page++
                        Log.d("봉사활동 서비스 목록", viewModel.recruitedServices.value.toString())

                    } else {
                        adapter = RecruitedServiceRVAdapter(this, viewModel, viewModel.recruitedServices.value)
                        binding.rvResultRecruited.adapter = adapter
                        binding.rvResultRecruited.layoutManager = layoutManager
                    }
//                    isLoading = false
                    binding.progressRecruited.visibility = View.GONE
                }, 500)
            } else {
                Log.d("네트워크 통신 이뤄지지 않음, 네트워크 통신 성공 여부", "${viewModel.isRecruitedSuccessful.value}")
            }
        }

    }

}