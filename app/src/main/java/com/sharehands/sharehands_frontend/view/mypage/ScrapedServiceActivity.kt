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
import com.sharehands.sharehands_frontend.adapter.mypage.ScrapedServiceRVAdapter
import com.sharehands.sharehands_frontend.databinding.ActivityScrapedServiceBinding
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.viewmodel.mypage.ServiceMgtViewModel

class ScrapedServiceActivity: AppCompatActivity() {
    private lateinit var binding: ActivityScrapedServiceBinding
    private lateinit var viewModel: ServiceMgtViewModel
    private lateinit var adapter: ScrapedServiceRVAdapter
    private lateinit var layoutManager: LinearLayoutManager
    var page = 1
    var isLoading = false
    var pageLen = 5
    var last = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scraped_service)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scraped_service)

        viewModel = ViewModelProvider(this).get(ServiceMgtViewModel::class.java)
        val token = SharedPreferencesManager.getInstance(this).getString("token", "null")
        val recyclerView = binding.rvResultScraped

//        layoutManager = LinearLayoutManager(this)
//        recyclerView.layoutManager = layoutManager
//
//        adapter = ScrapedServiceRVAdapter(this, viewModel, viewModel.scrapedServices.value)
//        binding.rvResultScraped.adapter = adapter

        getServices(token, viewModel, 0)

        binding.ivGoBack.setOnClickListener {
            finish()
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (pageLen == 5 && page > 1 && last != viewModel.scrapedResult.value!!.lastApplyId?.toInt()) {
                    if (dy > 0) {
                        if (!isLoading) {
                            page++
                            last = viewModel.scrapedResult.value!!.lastApplyId?.toInt()!!
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
            binding.progressScraped.visibility = View.VISIBLE
            if (page == 1) {
                viewModel.getScrapedList(token, 0)
                viewModel.isScrapSuccessful.observe(this) {
                    if (viewModel.isScrapSuccessful.value == true) {
                        page++
                        pageLen = viewModel.scrapedResult.value?.serviceList!!.size
                        Log.d("pageLen", "${viewModel.scrapedResult.value?.serviceList!!.size}")
                        Log.d("pageLen after", "${pageLen}")
                        val result = viewModel.scrapedServices
                        val total = viewModel.scrapedNum.value
                        binding.tvTotalScraped.text = "총 ${total}개의 봉사를 스크랩하였습니다."
                        Handler().postDelayed({
                            if (::adapter.isInitialized) {
                                adapter.notifyDataSetChanged()
                            } else {
                                adapter = ScrapedServiceRVAdapter(this@ScrapedServiceActivity, viewModel, viewModel.scrapedServices.value)
                                layoutManager = LinearLayoutManager(this@ScrapedServiceActivity)
                                binding.rvResultScraped.adapter = adapter
                                binding.rvResultScraped.layoutManager = layoutManager
                            }
                            isLoading = false
                            binding.progressScraped.visibility = View.GONE
                        }, 500)
                    } else {
                        isLoading = false
                        binding.progressScraped.visibility = View.GONE
                        binding.tvTotalScraped.text = "총 0개의 봉사를 스크랩하였습니다."
                    }

                }
            } else {
                Log.d("lastId", "${last}")
                viewModel.getScrapedList(token, last)
                viewModel.isScrapSuccessful.observe(this) {
                    Log.d("pageLen before", "${viewModel.scrapedResult.value?.serviceList!!.size}")
                    pageLen = viewModel.scrapedResult.value?.serviceList!!.size
                    Log.d("pageLen after", "${pageLen}")
                    Handler().postDelayed({
                        if (::adapter.isInitialized) {
                            adapter.notifyDataSetChanged()
                        } else {
                            adapter = ScrapedServiceRVAdapter(this@ScrapedServiceActivity, viewModel, viewModel.scrapedServices.value)
                            layoutManager = LinearLayoutManager(this@ScrapedServiceActivity)
                            binding.rvResultScraped.adapter = adapter
                            binding.rvResultScraped.layoutManager = layoutManager
                        }
                        isLoading = false
                        binding.progressScraped.visibility = View.GONE
                    }, 500)
                }
            }
        }
    }

}