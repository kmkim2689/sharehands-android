package com.sharehands.sharehands_frontend.view.search

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.adapter.search.ApplicantsRVAdapter
import com.sharehands.sharehands_frontend.adapter.search.RecommendRVAdapter
import com.sharehands.sharehands_frontend.databinding.ActivityRecruitBinding
import com.sharehands.sharehands_frontend.databinding.ActivityServiceDetailBinding
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.viewmodel.search.RecruitViewModel

class RecruitActivity: AppCompatActivity() {
    private lateinit var binding: ActivityRecruitBinding
    private lateinit var viewModel: RecruitViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recruit)

        viewModel = ViewModelProvider(this).get(RecruitViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recruit)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.ivGoBack.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        val serviceId = intent.getIntExtra("serviceId", 0)
        val isExpired = intent.getBooleanExtra("isExpired", true)
        Log.d("is expired", "$isExpired")
        val token = SharedPreferencesManager.getInstance(this).getString("token", "null")

        if (isExpired) {
            binding.tvRecommendedApplicants.visibility = View.GONE
            binding.layoutRecommendStatus.visibility = View.GONE
            binding.rvRecommendedApplicants.visibility = View.GONE
        }


        if (token != "null" && serviceId != 0) {
            viewModel.getApplicantsData(token, serviceId.toLong())
            viewModel.response.observe(this) {
                val result = viewModel.response
                val applicantsLayoutManager = LinearLayoutManager(this)
                val recommendLayoutManager = LinearLayoutManager(this)
                val applicantsAdapter = ApplicantsRVAdapter(this, result.value?.participatedList, token, serviceId, viewModel, isExpired)
                val recommendAdapter = RecommendRVAdapter(this, result.value?.suggestionList, serviceId.toLong(), viewModel, token)

                binding.rvRecruitedApplicants.adapter = applicantsAdapter
                binding.rvRecruitedApplicants.layoutManager = applicantsLayoutManager
                binding.rvRecommendedApplicants.adapter = recommendAdapter
                binding.rvRecommendedApplicants.layoutManager = recommendLayoutManager

                binding.tvCurrentNumApplied.text = result.value?.participatedNum.toString()
                binding.tvMaxNum.text = result.value?.recruitNum.toString()
                binding.tvCurrentRecommended.text = result.value?.suggestionNum.toString()
            }
        }

    }
}