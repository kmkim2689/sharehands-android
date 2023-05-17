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
import com.sharehands.sharehands_frontend.adapter.mypage.CompletedServiceRVAdapter
import com.sharehands.sharehands_frontend.adapter.mypage.SuggestedServiceRVAdapter
import com.sharehands.sharehands_frontend.databinding.ActivitySuggestedServiceBinding
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.viewmodel.mypage.ServiceMgtViewModel

class SuggestedServiceActivity: AppCompatActivity() {
    lateinit var binding: ActivitySuggestedServiceBinding
    private lateinit var viewModel: ServiceMgtViewModel
    private lateinit var adapter: SuggestedServiceRVAdapter
    private lateinit var layoutManager: LinearLayoutManager
    var page = 1
    var isLoading = false
    var pageLen = 5
    var last = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suggested_service)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_suggested_service)
        viewModel = ViewModelProvider(this).get(ServiceMgtViewModel::class.java)
        val token = SharedPreferencesManager.getInstance(this).getString("token", "null")
        val recyclerView = binding.rvResultSuggested

//        layoutManager = LinearLayoutManager(this)
//        recyclerView.layoutManager = layoutManager

        getServices(token, viewModel, 0)

        binding.ivGoBack.setOnClickListener {
            finish()
        }

        binding.rvResultSuggested.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (pageLen == 5 && page > 1 && last != viewModel.suggestedResult.value!!.lastApplyId?.toInt()) {
                    if (dy > 0) {
                        if (!isLoading) {
                            page++
                            last = viewModel.suggestedResult.value!!.lastApplyId?.toInt()!!
                            getServices(token, viewModel, last)
                        }
                    }
                }
            }
        })
    }


    fun getServices(token: String, viewModel: ServiceMgtViewModel, last: Int) {
        if (token != "null") {
            isLoading = true
            binding.progressSuggested.visibility = View.VISIBLE
            if (page == 1) {
                viewModel.getSuggestedList(token, 0)
                viewModel.isSuggestedSuccessful.observe(this) {
                    if (viewModel.isSuggestedSuccessful.value == true) {
                        page++
                        pageLen = viewModel.suggestedResult.value?.serviceList!!.size
                        Log.d("pageLen", "${viewModel.suggestedResult.value?.serviceList!!.size}")
                        Log.d("pageLen after", "${pageLen}")
                        val result = viewModel.suggestedServices
                        val total = viewModel.suggestedNum.value
                        binding.tvTotalSuggested.text = "총 ${total}개의 봉사를 제안받았습니다."
                        Handler().postDelayed({
                            if (::adapter.isInitialized) {
                                adapter.notifyDataSetChanged()
                            } else {
                                adapter = SuggestedServiceRVAdapter(this@SuggestedServiceActivity, viewModel, viewModel.suggestedServices.value)
                                layoutManager = LinearLayoutManager(this@SuggestedServiceActivity)
                                binding.rvResultSuggested.adapter = adapter
                                binding.rvResultSuggested.layoutManager = layoutManager
                            }
                            isLoading = false
                            binding.progressSuggested.visibility = View.GONE
                        }, 500)
                    } else {
                        isLoading = false
                        binding.progressSuggested.visibility = View.GONE
                        binding.tvTotalSuggested.text = "총 0개의 봉사를 제안받았습니다."
                    }

                }
            } else {
                Log.d("lastId", "${last}")
                viewModel.getSuggestedList(token, last)
                viewModel.isSuggestedSuccessful.observe(this) {
                    Log.d("pageLen before", "${viewModel.suggestedResult.value?.serviceList!!.size}")
                    pageLen = viewModel.suggestedResult.value?.serviceList!!.size
                    Log.d("pageLen after", "${pageLen}")
                    Handler().postDelayed({
                        if (::adapter.isInitialized) {
                            adapter.notifyDataSetChanged()
                        } else {
                            adapter = SuggestedServiceRVAdapter(this@SuggestedServiceActivity, viewModel, viewModel.suggestedServices.value)
                            layoutManager = LinearLayoutManager(this@SuggestedServiceActivity)
                            binding.rvResultSuggested.adapter = adapter
                            binding.rvResultSuggested.layoutManager = layoutManager
                        }
                        isLoading = false
                        binding.progressSuggested.visibility = View.GONE
                    }, 500)
                }
            }
        }

    }

}