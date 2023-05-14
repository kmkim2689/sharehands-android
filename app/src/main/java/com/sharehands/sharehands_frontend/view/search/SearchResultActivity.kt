package com.sharehands.sharehands_frontend.view.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.adapter.search.SearchKeywordRVAdapter
import com.sharehands.sharehands_frontend.adapter.search.ServicesSearchRVAdapter
import com.sharehands.sharehands_frontend.databinding.ActivitySearchResultBinding
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.MainActivity
import com.sharehands.sharehands_frontend.view.signin.SocialLoginActivity
import com.sharehands.sharehands_frontend.viewmodel.search.SearchKeywordViewModel

class SearchResultActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySearchResultBinding
    private lateinit var viewModel: SearchKeywordViewModel
    private lateinit var adapter: SearchKeywordRVAdapter
    private lateinit var layoutManager: LinearLayoutManager
    var page = 1
    // 네트워크 통신중 여부
    var isLoading = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_result)
        viewModel = ViewModelProvider(this).get(SearchKeywordViewModel::class.java)
        binding.lifecycleOwner = this

        val recyclerView = binding.rvSearchResult
        val adapter = SearchKeywordRVAdapter(this, viewModel, viewModel.servicesList.value)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter


        val token = SharedPreferencesManager.getInstance(this).getString("token", "null")
        if (token == "null") {
            showSnackbar("서비스 이용을 위하여 로그인하세요.")
        } else {
            var resultKeyword = intent.getStringExtra("keyword")

            if (resultKeyword != null && resultKeyword.length >= 2) {
                binding.editSearch.setText("$resultKeyword")
//                viewModel.searchKeyword(token, resultKeyword, page)
//                viewModel.isSuccessful.observe(this) {
//                    if (viewModel.isSuccessful.value == true) {
//                        Log.d("봉사활동 검색 결과 성공 여부", "${viewModel.isSuccessful.value}")
//
//
//                    }
//                }
                getServices(token, this, resultKeyword)
                viewModel.searchResult.observe(this) {
                    binding.tvTotalResult.text = "총 ${viewModel.searchResult.value?.workCounter}건의 봉사가 있습니다."
                }

                recyclerView.adapter = adapter

                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
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
                                viewModel.isSuccessful.observe(this@SearchResultActivity) {
                                    if (!isLoading) {
                                        Log.d("total cnt", "${total}")
                                        if ((viewModel.searchResult.value?.workCounter!! / 10).toInt() + 1 > page &&
                                            viewModel.servicesList.value!!.size < viewModel.searchResult.value?.workCounter!!.toInt()) {
                                            Log.d("page cnt", "${(viewModel.searchResult.value?.workCounter!! / 10).toInt()}")
                                            Log.d("page before cnt", "${page}")
                                            page++
                                            getServices(token, this@SearchResultActivity, resultKeyword)
                                            Log.d("page after cnt", "${page}")

                                        }
                                    }

                                }
                            } else if (page < (viewModel.searchResult.value?.workCounter!! / 10).toInt() + 1) {
                                viewModel.isScrollSuccessful.observe(this@SearchResultActivity) {
                                    if (!isLoading) {
                                        Log.d("current cnt", "${viewModel.servicesList.value!!.size}")
                                        Log.d("total cnt", "${total}")
                                        if ((viewModel.searchResult.value?.workCounter!! / 10).toInt() + 1 > page &&
                                            viewModel.servicesList.value!!.size < viewModel.searchResult.value?.workCounter!!.toInt()) {
                                            Log.d("page cnt", "${(viewModel.searchResult.value?.workCounter!! / 10).toInt()}")
                                            Log.d("page before cnt", "${page}")
                                            page++
                                            getServices(token, this@SearchResultActivity, resultKeyword)
                                            Log.d("page after cnt", "${page}")

                                        }
                                    }

                                }
                            }

                        }
                    }
                })


            }
        }



        binding.ivGoBack.setOnClickListener {
            finish()
        }

        binding.ivSearchService.setOnClickListener {

            if (binding.editSearch.text.toString().length >= 2) {
                // 화면 이동하기
                val intent = Intent(this, SearchResultActivity::class.java)
                intent.putExtra("keyword", binding.editSearch.text.toString())
                Log.d("봉사활동 search keyword", viewModel.searchedKeyword.value.toString())
                finish()
                startActivity(intent)
            } else {
                // 스낵바 띄우기
                Snackbar.make(binding.coordinatorLayoutResult, "2글자 이상 입력해주세요.", Snackbar.LENGTH_SHORT).show()
            }


        }
    }

    override fun onResume() {
        super.onResume()
    }

    fun getServices(token: String, contextActivity: Context, keyword: String) {
        isLoading = true
        // 당장 데이터 가져오는 것을 요청한 직후에는 프로그레스 바가 보여야 함
        binding.progressAll.visibility = View.VISIBLE
        // TODO 뷰모델로 네트워크 요청. 네트워크 통신을 통해 가져온 페이지
        // TODO 만약 GET 성공하면, (result.isSuccessful) 핸들러 실행
        viewModel.searchKeyword(token, keyword, page)
        Log.d("봉사활동 서비스 불러오기 성공 여부", "${viewModel.isSuccessful.value}")

        viewModel.isSuccessful.observe(this) {
            if (viewModel.isSuccessful.value == true) {
                Handler().postDelayed({
                    if (::adapter.isInitialized) {
//                        adapter = ServicesSearchRVAdapter(contextActivity as MainActivity, viewModel, viewModel.servicesList.value)
//                        binding.rvResultAll.adapter = adapter
//                        binding.rvResultAll.layoutManager = layoutManager
                        adapter.notifyDataSetChanged()
//                        page++
                        Log.d("봉사활동 서비스 목록", viewModel.servicesList.value.toString())

                    } else {
                        adapter = SearchKeywordRVAdapter(this, viewModel, viewModel.servicesList.value)
                        binding.rvSearchResult.adapter = adapter
                        binding.rvSearchResult.layoutManager = layoutManager
                    }
                    isLoading = false
                    binding.progressAll.visibility = View.GONE
                }, 500)
            } else {
                Log.d("네트워크 통신 이뤄지지 않음, 네트워크 통신 성공 여부", "${viewModel.isSuccessful.value}")
            }
        }

    }


    private fun showSnackbar(text: String) {
        val snackbar = Snackbar.make(binding.coordinatorLayoutResult, text, Snackbar.LENGTH_LONG)
            .setAction("로그인") {
                val intent = Intent(this, SocialLoginActivity::class.java)
                startActivity(intent)
                MainActivity().finish()
            }
        snackbar.show()
    }
}