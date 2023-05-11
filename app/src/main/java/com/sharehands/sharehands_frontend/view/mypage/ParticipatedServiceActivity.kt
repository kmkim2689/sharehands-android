package com.sharehands.sharehands_frontend.view.mypage

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.adapter.mypage.CompletedServiceRVAdapter
import com.sharehands.sharehands_frontend.databinding.ActivityParticipatedServiceBinding
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.viewmodel.mypage.ServiceMgtViewModel

class ParticipatedServiceActivity: AppCompatActivity() {
    private lateinit var binding: ActivityParticipatedServiceBinding
    private lateinit var viewModel: ServiceMgtViewModel
    private lateinit var adapter: CompletedServiceRVAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var page = 1
    private var last = 0
    private var isLoading = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_participated_service)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_participated_service)

        viewModel = ViewModelProvider(this).get(ServiceMgtViewModel::class.java)
        val token = SharedPreferencesManager.getInstance(this).getString("token", "null")
        val recyclerView = binding.rvResultParticipated

        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        getServices(token, viewModel, last)
    }

    override fun onResume() {
        super.onResume()

    }
    fun getServices(token: String, viewModel: ServiceMgtViewModel, lastId: Int) {
        isLoading = true
        // 당장 데이터 가져오는 것을 요청한 직후에는 프로그레스 바가 보여야 함
        binding.progressParticipated.visibility = View.VISIBLE
        // TODO 뷰모델로 네트워크 요청. 네트워크 통신을 통해 가져온 페이지
        // TODO 만약 GET 성공하면, (result.isSuccessful) 핸들러 실행
        viewModel.getCompletedList(token, last)
        Log.d("봉사활동 서비스 불러오기 성공 여부", "${viewModel.isRecruitedSuccessful.value}")

        viewModel.isCompletedSuccessful.observe(this) {
            binding.tvTotalParticipated.text = "총 ${viewModel.completedNum.value}개의 봉사를 완료하였습니다."
            if (viewModel.isCompletedSuccessful.value == true) {
                Handler().postDelayed({
                    if (::adapter.isInitialized) {
                        adapter = CompletedServiceRVAdapter(this, viewModel, viewModel.completedServices.value)
                        binding.rvResultParticipated.adapter = adapter
                        binding.rvResultParticipated.layoutManager = layoutManager
                        adapter.notifyDataSetChanged()
                        page++
                        last = viewModel.completedResult.value?.lastApplyId!!.toInt()
                        Log.d("봉사활동 서비스 목록", viewModel.recruitedServices.value.toString())
                    } else {
                        adapter = CompletedServiceRVAdapter(this, viewModel, viewModel.completedServices.value)
                        binding.rvResultParticipated.adapter = adapter
                        binding.rvResultParticipated.layoutManager = layoutManager
                    }
                    isLoading = false
                    binding.progressParticipated.visibility = View.GONE
                }, 500)
            } else {
                Log.d("네트워크 통신 이뤄지지 않음, 네트워크 통신 성공 여부", "${viewModel.isCompletedSuccessful.value}")
            }
        }
    }

}