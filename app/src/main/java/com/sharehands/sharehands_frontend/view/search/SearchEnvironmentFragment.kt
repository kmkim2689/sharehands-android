package com.sharehands.sharehands_frontend.view.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.adapter.search.ServicesSearchEduRVAdapter
import com.sharehands.sharehands_frontend.adapter.search.ServicesSearchEnvironmentRVAdapter
import com.sharehands.sharehands_frontend.adapter.search.ServicesSearchHealthRVAdapter
import com.sharehands.sharehands_frontend.databinding.FragmentSearchEducationBinding
import com.sharehands.sharehands_frontend.databinding.FragmentSearchEnvironmentBinding
import com.sharehands.sharehands_frontend.databinding.FragmentSearchHealthBinding
import com.sharehands.sharehands_frontend.network.search.ServiceList
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.MainActivity
import com.sharehands.sharehands_frontend.view.signin.SocialLoginActivity
import com.sharehands.sharehands_frontend.viewmodel.search.ServiceSearchEduViewModel
import com.sharehands.sharehands_frontend.viewmodel.search.ServiceSearchEnvironmentViewModel
import com.sharehands.sharehands_frontend.viewmodel.search.ServiceSearchHealthViewModel

class SearchEnvironmentFragment: Fragment() {
    private var sort = 1
    private val category = 5
    private var page = 1
    private lateinit var binding: FragmentSearchEnvironmentBinding
    private lateinit var adapter: ServicesSearchEnvironmentRVAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var viewModel: ServiceSearchEnvironmentViewModel
    // 네트워크 통신중 여부
    private var isLoading = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_environment, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(ServiceSearchEnvironmentViewModel::class.java)
//            ViewModelProvider(requireActivity()).get(ServiceSearchEduViewModel::class.java)
        binding.lifecycleOwner = MainActivity()
        binding.viewModel = viewModel

        // Fragment 왔다갔다 했을 때 오류뜨는 이슈 및 중복해서 나타나는 해결 방법 : context를 미리 선언해놓고 사용한다.
        val context = requireContext()

        viewModel._servicesList.value = ArrayList<ServiceList>()
        adapter = ServicesSearchEnvironmentRVAdapter(context as MainActivity, viewModel, viewModel._servicesList.value)
        binding.rvResultEnv.adapter = adapter


        val recyclerView = binding.rvResultEnv

        val token = SharedPreferencesManager.getInstance(context as MainActivity).getString("token", "null")
        if (token == "null") {
            Snackbar.make(requireActivity().findViewById(R.id.layout_main_activity), "서비스 이용을 위하여 로그인이 필요합니다.", Snackbar.LENGTH_SHORT)
                .setAction("로그인", View.OnClickListener { view ->
                    val intent = Intent(requireContext(), SocialLoginActivity::class.java)
                    startActivity(intent)
                })
                .show()
        }


        getServices(token, context)
        viewModel.count.observe(viewLifecycleOwner) {
            Log.d("list counts", "${viewModel.count.value}")
        }

        viewModel.searchResult.observe(viewLifecycleOwner) {
            binding.tvTotalEnv.text = "총 ${viewModel.searchResult.value?.workCounter}건의 봉사가 있습니다."
        }


        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
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
                        viewModel.isSuccessful.observe(viewLifecycleOwner) {
                            if (!isLoading) {
                                Log.d("current cnt", "${viewModel.count.value}")
                                Log.d("total cnt", "${total}")
                                if ((viewModel.searchResult.value?.workCounter!! / 10).toInt() + 1 > page &&
                                    viewModel.count.value!! < viewModel.searchResult.value?.workCounter!!.toInt()) {
                                    Log.d("page cnt", "${(viewModel.searchResult.value?.workCounter!! / 10).toInt()}")
                                    Log.d("page before cnt", "${page}")
                                    page++
                                    getServices(token, context)
                                    Log.d("page after cnt", "${page}")

                                } else {
                                    viewModel._isInitialized.value = false
                                }
                            }

                        }
                    } else if (page < (viewModel.searchResult.value?.workCounter!! / 10).toInt() + 1) {
                        viewModel.isScrollSuccessful.observe(viewLifecycleOwner) {
                            if (!isLoading) {
                                Log.d("current cnt", "${viewModel.count.value}")
                                Log.d("total cnt", "${total}")
                                if ((viewModel.searchResult.value?.workCounter!! / 10).toInt() + 1 > page &&
                                    viewModel.count.value!! < viewModel.searchResult.value?.workCounter!!.toInt()) {
                                    Log.d("page cnt", "${(viewModel.searchResult.value?.workCounter!! / 10).toInt()}")
                                    Log.d("page before cnt", "${page}")
                                    page++
                                    getServices(token, context)
                                    Log.d("page after cnt", "${page}")

                                }
                            }

                        }
                    }

                }
            }
        })


        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

    override fun onResume() {
        super.onResume()

    }

    fun getServices(token: String, contextActivity: Context) {
        isLoading = true
        // 당장 데이터 가져오는 것을 요청한 직후에는 프로그레스 바가 보여야 함
        binding.progressEnv.visibility = View.VISIBLE
        // TODO 뷰모델로 네트워크 요청. 네트워크 통신을 통해 가져온 페이지
        // TODO 만약 GET 성공하면, (result.isSuccessful) 핸들러 실행
        viewModel.loadServices(token, category, sort, page)
        Log.d("봉사활동 서비스 불러오기 성공 여부", "${viewModel.isSuccessful.value}")

        viewModel.isSuccessful.observe(viewLifecycleOwner) {
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
                        adapter = ServicesSearchEnvironmentRVAdapter(context as MainActivity, viewModel, viewModel.servicesList.value)
                        binding.rvResultEnv.adapter = adapter
                        binding.rvResultEnv.layoutManager = layoutManager
                    }
                    isLoading = false
                    binding.progressEnv.visibility = View.GONE
                }, 500)
            } else {
                Log.d("네트워크 통신 이뤄지지 않음, 네트워크 통신 성공 여부", "${viewModel.isSuccessful.value}")
            }
        }

    }


}