package com.sharehands.sharehands_frontend.view.mypage

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
import com.sharehands.sharehands_frontend.adapter.mypage.AppliedServiceRVAdapter
import com.sharehands.sharehands_frontend.adapter.mypage.CompletedServiceRVAdapter
import com.sharehands.sharehands_frontend.databinding.ActivityParticipatedServiceBinding
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.viewmodel.mypage.ServiceMgtViewModel

// TODO 무한스크롤 구현하기
class ParticipatedServiceActivity: AppCompatActivity() {
    private lateinit var binding: ActivityParticipatedServiceBinding
    private lateinit var viewModel: ServiceMgtViewModel
    private lateinit var adapter: CompletedServiceRVAdapter
    private lateinit var layoutManager: LinearLayoutManager
    var page = 1
    var isLoading = false
    var pageLen = 5
    var last = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_participated_service)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_participated_service)
        viewModel = ViewModelProvider(this).get(ServiceMgtViewModel::class.java)
        val token = SharedPreferencesManager.getInstance(this).getString("token", "null")
        val recyclerView = binding.rvResultParticipated

//        layoutManager = LinearLayoutManager(this)
//        recyclerView.layoutManager = layoutManager

        getServices(token, viewModel, 0)

        binding.ivGoBack.setOnClickListener {
            finish()
        }

        binding.rvResultParticipated.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (pageLen == 5 && page > 1 && last != viewModel.completedResult.value!!.lastApplyId?.toInt()) {
                    if (dy > 0) {
                        if (!isLoading) {
                            page++
                            last = viewModel.completedResult.value!!.lastApplyId?.toInt()!!
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
            binding.progressParticipated.visibility = View.VISIBLE
            if (page == 1) {
                viewModel.getCompletedList(token, 0)
                viewModel.isCompletedSuccessful.observe(this) {
                    if (viewModel.isCompletedSuccessful.value == true) {
                        page++
                        pageLen = viewModel.completedResult.value?.serviceList!!.size
                        Log.d("pageLen", "${viewModel.completedResult.value?.serviceList!!.size}")
                        Log.d("pageLen after", "${pageLen}")
                        val result = viewModel.completedServices
                        val total = viewModel.completedNum.value
                        binding.tvTotalParticipated.text = "총 ${total}개의 봉사를 완료했습니다."
                        Handler().postDelayed({
                            if (::adapter.isInitialized) {
                                adapter.notifyDataSetChanged()
                            } else {
                                adapter = CompletedServiceRVAdapter(this@ParticipatedServiceActivity, viewModel, viewModel.completedServices.value)
                                layoutManager = LinearLayoutManager(this@ParticipatedServiceActivity)
                                binding.rvResultParticipated.adapter = adapter
                                binding.rvResultParticipated.layoutManager = layoutManager
                            }
                            isLoading = false
                            binding.progressParticipated.visibility = View.GONE
                        }, 500)
                    } else {
                        isLoading = false
                        binding.progressParticipated.visibility = View.GONE
                        binding.tvTotalParticipated.text = "총 0개의 봉사를 완료했습니다."
                    }

                }
            } else {
                Log.d("lastId", "${last}")
                viewModel.getCompletedList(token, last)
                viewModel.isCompletedSuccessful.observe(this) {
                    Log.d("pageLen before", "${viewModel.completedResult.value?.serviceList!!.size}")
                    pageLen = viewModel.completedResult.value?.serviceList!!.size
                    Log.d("pageLen after", "${pageLen}")
                    Handler().postDelayed({
                        if (::adapter.isInitialized) {
                            adapter.notifyDataSetChanged()
                        } else {
                            adapter = CompletedServiceRVAdapter(this@ParticipatedServiceActivity, viewModel, viewModel.completedServices.value)
                            layoutManager = LinearLayoutManager(this@ParticipatedServiceActivity)
                            binding.rvResultParticipated.adapter = adapter
                            binding.rvResultParticipated.layoutManager = layoutManager
                        }
                        isLoading = false
                        binding.progressParticipated.visibility = View.GONE
                    }, 500)
                }
            }
        }

    }

}