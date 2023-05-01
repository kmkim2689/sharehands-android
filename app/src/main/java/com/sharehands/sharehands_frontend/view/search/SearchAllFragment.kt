package com.sharehands.sharehands_frontend.view.search

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker.OnScrollListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.adapter.search.ServicesSearchRVAdapter
import com.sharehands.sharehands_frontend.databinding.FragmentSearchAllBinding
import com.sharehands.sharehands_frontend.view.MainActivity
import com.sharehands.sharehands_frontend.viewmodel.search.ServiceSearchViewModel

class SearchAllFragment: Fragment() {
    private lateinit var binding: FragmentSearchAllBinding
    private lateinit var adapter: ServicesSearchRVAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var viewModel: ServiceSearchViewModel
    private var sort = 1
    private val category = 1
    // 페이지 초기화
    var page = 1
    // 네트워크 통신중 여부
    var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_all, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(MainActivity()).get(ServiceSearchViewModel::class.java)
        binding.lifecycleOwner = MainActivity()
        binding.viewModel = viewModel
        val recyclerView = binding.rvResultAll
        getServices()
        recyclerView.adapter = adapter
        layoutManager = LinearLayoutManager(MainActivity())
        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                    val total = adapter.itemCount

                    if (!isLoading) {
                        if ((visibleItemCount + pastVisibleItem) >= total) {
                            getServices()
                        }
                    }
                }
            }
        })


    }

    override fun onResume() {
        super.onResume()

    }

    fun getServices() {
        isLoading = true
        // 당장 데이터 가져오는 것을 요청한 직후에는 프로그레스 바가 보여야 함
        binding.progressAll.visibility = View.VISIBLE
        // TODO 뷰모델로 네트워크 요청. 네트워크 통신을 통해 가져온 페이지
        // TODO 만약 GET 성공하면, (result.isSuccessful) 핸들러 실행
        val isSuccessful = viewModel.loadServices(category, sort, page)
        if (isSuccessful) {
            Handler().postDelayed({
                if (::adapter.isInitialized) {
                    adapter.notifyDataSetChanged()
                    page++
                } else {
                    adapter = ServicesSearchRVAdapter(MainActivity(), viewModel, viewModel.servicesList.value)
                    binding.rvResultAll.adapter = adapter
                    binding.rvResultAll.layoutManager = layoutManager
                }
                isLoading = false
                binding.progressAll.visibility = View.GONE
            }, 1000)
        } else {
            Log.d("네트워크 통신 이뤄지지 않음, 네트워크 통신 성공 여부", "${isSuccessful}")
            // TODO 스낵바 띄우기. 로그인하세요
        }

            // 어댑터가 초기화 안됐으면 초기화하고, 초기화 되어있으면 adapter.notifyDataSetChanged 호출.

    }

}