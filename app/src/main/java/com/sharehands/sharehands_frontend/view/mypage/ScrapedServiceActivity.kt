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
//    private lateinit var adapter:
    private lateinit var layoutManager: LinearLayoutManager
    private var page = 1
    private var last = 0
    private var isLoading = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scraped_service)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scraped_service)

        viewModel = ViewModelProvider(this).get(ServiceMgtViewModel::class.java)
        val token = SharedPreferencesManager.getInstance(this).getString("token", "null")
        val recyclerView = binding.rvResultScraped

        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        adapter = ScrapedServiceRVAdapter(this, viewModel, viewModel.scrapedServices.value)
        binding.rvResultScraped.adapter = adapter

        getServices(token, viewModel, last)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 80) {
                    // 스크롤 내렸을 때 플로팅 버튼 사라지게 하기

//                    layoutManager = LinearLayoutManager(requireContext())
//                    val visibleItemCount = layoutManager.childCount
//                    val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                    val total = adapter.itemCount
//                    Log.d("total", "${total}")

                    if (page == 1) {
                        viewModel.isScrapSuccessful.observe(this@ScrapedServiceActivity) {
                            if (!isLoading) {
                                Log.d("total cnt", "${total}")
                                if ((viewModel.scrapedResult.value?.serviceCounter!! / 10).toInt() + 1 > page &&
                                    viewModel.currScrapedNum.value!! < viewModel.scrapedResult.value?.serviceCounter!!.toInt()) {
                                    Log.d("page cnt", "${(viewModel.scrapedResult.value?.serviceCounter!! / 10).toInt()}")
                                    Log.d("page before cnt", "${page}")
                                    page++
                                    getServices(token, viewModel, last)
                                    Log.d("page after cnt", "${page}")

                                } else {
                                    viewModel._isInitialized.value = false
                                }
                            }

                        }
                    } else if (page < (viewModel.scrapedResult.value?.serviceCounter!! / 10).toInt() + 1) {
                        viewModel.isScrapSuccessful.observe(this@ScrapedServiceActivity) {
                            if (!isLoading) {
                                Log.d("current cnt", "${viewModel.currScrapedNum.value}")
                                Log.d("total cnt", "${total}")
                                if ((viewModel.scrapedResult.value?.serviceCounter!! / 10).toInt() + 1 > page &&
                                    viewModel.currScrapedNum.value!! < viewModel.scrapedResult.value?.serviceCounter!!.toInt()) {
                                    Log.d("page cnt", "${(viewModel.scrapedResult.value?.serviceCounter!! / 10).toInt()}")
                                    Log.d("page before cnt", "${page}")
                                    page++
                                    getServices(token, viewModel, last)
                                    Log.d("page after cnt", "${page}")

                                }
                            }

                        }
                    }

                }

            }
        })

    }

    fun getServices(token: String, viewModel: ServiceMgtViewModel, lastId: Int) {
        isLoading = true
        // 당장 데이터 가져오는 것을 요청한 직후에는 프로그레스 바가 보여야 함
        binding.progressScraped.visibility = View.VISIBLE
        // TODO 뷰모델로 네트워크 요청. 네트워크 통신을 통해 가져온 페이지
        // TODO 만약 GET 성공하면, (result.isSuccessful) 핸들러 실행
        viewModel.getScrapedList(token, last)
        Log.d("봉사활동 서비스 불러오기 성공 여부", "${viewModel.isScrapSuccessful.value}")

        viewModel.isScrapSuccessful.observe(this) {
            binding.tvTotalScraped.text = "총 ${viewModel.scrapedNum.value}개의 봉사를 완료하였습니다."
            if (viewModel.isScrapSuccessful.value == true) {
                Handler().postDelayed({
                    if (::adapter.isInitialized) {
                        adapter = ScrapedServiceRVAdapter(this, viewModel, viewModel.scrapedServices.value)
                        binding.rvResultScraped.adapter = adapter
                        binding.rvResultScraped.layoutManager = layoutManager
                        adapter.notifyDataSetChanged()
                        page++
                        last = viewModel.scrapedResult.value?.lastApplyId!!.toInt()
                        Log.d("봉사활동 서비스 목록", viewModel.scrapedServices.value.toString())
                    } else {
                        adapter = ScrapedServiceRVAdapter(this, viewModel, viewModel.scrapedServices.value)
                        binding.rvResultScraped.adapter = adapter
                        binding.rvResultScraped.layoutManager = layoutManager
                    }
                    isLoading = false
                    binding.progressScraped.visibility = View.GONE
                }, 500)
            } else {
                Log.d("네트워크 통신 이뤄지지 않음, 네트워크 통신 성공 여부", "${viewModel.isScrapSuccessful.value}")
            }
        }
    }

}