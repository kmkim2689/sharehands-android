package com.sharehands.sharehands_frontend.view.mypage

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.adapter.mypage.AppliedServiceRVAdapter
import com.sharehands.sharehands_frontend.adapter.mypage.RecruitedServiceRVAdapter
import com.sharehands.sharehands_frontend.databinding.ActivityAppliedServiceBinding
import com.sharehands.sharehands_frontend.databinding.ActivityRecruitedServiceBinding
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.viewmodel.mypage.ServiceMgtViewModel

class AppliedServiceActivity: AppCompatActivity() {
    private lateinit var viewModel: ServiceMgtViewModel
    private lateinit var adapter: AppliedServiceRVAdapter
    private lateinit var layoutManager: LinearLayoutManager
    var page = 1
    var isLoading = false
    var pageLen = 5
    var last = -1
    private lateinit var binding: ActivityAppliedServiceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applied_service)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_applied_service)
        viewModel = ViewModelProvider(this).get(ServiceMgtViewModel::class.java)

        val token = SharedPreferencesManager.getInstance(this).getString("token", "null")
        val recyclerView = binding.rvResultApplied
//        adapter = AppliedServiceRVAdapter(this, viewModel, viewModel.recruitedServices.value)
//        layoutManager = LinearLayoutManager(this)
//        recyclerView.layoutManager = layoutManager
//        recyclerView.adapter = adapter

        getServices(token, viewModel, 0)

        binding.ivGoBack.setOnClickListener {
            finish()
        }

        binding.rvResultApplied.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (pageLen == 5 && page > 1 && last != viewModel.appliedResult.value!!.lastWorkId.toInt()) {
                    if (dy > 0) {
                        if (!isLoading) {
                            page++
                            last = viewModel.appliedResult.value!!.lastWorkId.toInt()
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
            binding.progressApplied.visibility = View.VISIBLE
            if (page == 1) {
                viewModel.getAppliedList(token, 0)
                viewModel.isAppliedServiceSuccessful.observe(this) {
                    if (viewModel.isAppliedServiceSuccessful.value == true) {
                        page++
                        pageLen = viewModel.appliedResult.value?.serviceList!!.size
                        Log.d("pageLen", "${viewModel.appliedResult.value?.serviceList!!.size}")
                        Log.d("pageLen after", "${pageLen}")
                        val result = viewModel.appliedServices
                        val total = viewModel.appliedNum.value
                        binding.tvTotalApplied.text = "총 ${total}개의 봉사에 지원했습니다."
                        Handler().postDelayed({
                            if (::adapter.isInitialized) {
                                adapter.notifyDataSetChanged()
                            } else {
                                adapter = AppliedServiceRVAdapter(this@AppliedServiceActivity, viewModel, viewModel.appliedServices.value)
                                layoutManager = LinearLayoutManager(this@AppliedServiceActivity)
                                binding.rvResultApplied.adapter = adapter
                                binding.rvResultApplied.layoutManager = layoutManager
                            }
                            isLoading = false
                            binding.progressApplied.visibility = View.GONE
                        }, 500)
                    } else {
                        isLoading = false
                        binding.progressApplied.visibility = View.GONE
                    }

                }
            } else {
                Log.d("lastId", "${last}")
                viewModel.getAppliedList(token, last)
                viewModel.isAppliedServiceSuccessful.observe(this) {
                    Log.d("pageLen before", "${viewModel.appliedResult.value?.serviceList!!.size}")
                    pageLen = viewModel.appliedResult.value?.serviceList!!.size
                    Log.d("pageLen after", "${pageLen}")
                    Handler().postDelayed({
                        if (::adapter.isInitialized) {
                            adapter.notifyDataSetChanged()
                        } else {
                            adapter = AppliedServiceRVAdapter(this@AppliedServiceActivity, viewModel, viewModel.appliedServices.value)
                            layoutManager = LinearLayoutManager(this@AppliedServiceActivity)
                            binding.rvResultApplied.adapter = adapter
                            binding.rvResultApplied.layoutManager = layoutManager
                        }
                        isLoading = false
                        binding.progressApplied.visibility = View.GONE
                    }, 500)
                }
            }
        }

    }
}