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
import androidx.recyclerview.widget.RecyclerView
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
    var isLoading = false
    var pageLen = 5
    var last = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recruited_service)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recruited_service)

        viewModel = ViewModelProvider(this).get(ServiceMgtViewModel::class.java)
        val token = SharedPreferencesManager.getInstance(this).getString("token", "null")
        val recyclerView = binding.rvResultRecruited
//
//        layoutManager = LinearLayoutManager(this)
//        adapter = RecruitedServiceRVAdapter(this, viewModel, viewModel.recruitedServices.value)
//        recyclerView.layoutManager = layoutManager
//        recyclerView.adapter = adapter

        getServices(token, viewModel, 0)

        binding.ivGoBack.setOnClickListener {
            finish()
        }

        binding.rvResultRecruited.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (pageLen == 5 && page > 1 && last != viewModel.recruitedResult.value!!.lastWorkId.toInt()) {
                    if (dy > 0) {
                        if (!isLoading) {
                            page++
                            last = viewModel.recruitedResult.value!!.lastWorkId.toInt()
                            getServices(token, viewModel, last)
                        }
                    }
                }
            }
        })
    }


    override fun onResume() {
        super.onResume()
    }

    fun getServices(token: String, viewModel: ServiceMgtViewModel, last: Int) {
        if (token != "null") {
            isLoading = true
            binding.progressRecruited.visibility = View.VISIBLE
            if (page == 1) {
                viewModel.getRecruitedList(token, 0)
                viewModel.isRecruitedSuccessful.observe(this) {
                    if (viewModel.isRecruitedSuccessful.value == true) {
                        page++
                        pageLen = viewModel.recruitedResult.value?.serviceList!!.size
                        Log.d("pageLen", "${viewModel.recruitedResult.value?.serviceList!!.size}")
                        Log.d("pageLen after", "${pageLen}")
                        val result = viewModel.recruitedServices
                        val total = viewModel.recruitedNum.value
                        binding.tvTotalRecruited.text = "총 ${total}개의 봉사를 모집했습니다."
                        Handler().postDelayed({
                            if (::adapter.isInitialized) {
                                adapter.notifyDataSetChanged()
                            } else {
                                adapter = RecruitedServiceRVAdapter(this@RecruitedServiceActivity, viewModel, viewModel.recruitedServices.value)
                                layoutManager = LinearLayoutManager(this@RecruitedServiceActivity)
                                binding.rvResultRecruited.adapter = adapter
                                binding.rvResultRecruited.layoutManager = layoutManager
                            }
                            isLoading = false
                            binding.progressRecruited.visibility = View.GONE
                        }, 500)
                    } else {
                        isLoading = false
                        binding.progressRecruited.visibility = View.GONE
                        binding.tvTotalRecruited.text = "총 0개의 봉사를 모집했습니다."
                    }

                }
            } else {
                Log.d("lastId", "${last}")
                viewModel.getRecruitedList(token, last)
                viewModel.isRecruitedSuccessful.observe(this) {
                    Log.d("pageLen before", "${viewModel.recruitedResult.value?.serviceList!!.size}")
                    pageLen = viewModel.recruitedResult.value?.serviceList!!.size
                    Log.d("pageLen after", "${pageLen}")
                    Handler().postDelayed({
                        if (::adapter.isInitialized) {
                            adapter.notifyDataSetChanged()
                        } else {
                            adapter = RecruitedServiceRVAdapter(this@RecruitedServiceActivity, viewModel, viewModel.recruitedServices.value)
                            layoutManager = LinearLayoutManager(this@RecruitedServiceActivity)
                            binding.rvResultRecruited.adapter = adapter
                            binding.rvResultRecruited.layoutManager = layoutManager
                        }
                        isLoading = false
                        binding.progressRecruited.visibility = View.GONE
                    }, 500)
                }
            }
        }

    }

}