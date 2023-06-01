package com.sharehands.sharehands_frontend.view.search

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivityApplicantInfoBinding
import com.sharehands.sharehands_frontend.network.mypage.MyPageDetail
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.viewmodel.search.ApplicantInfoViewModel

class ApplicantInfoActivity: AppCompatActivity() {
    private lateinit var binding: ActivityApplicantInfoBinding
    private lateinit var viewModel: ApplicantInfoViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_applicant_info)
        viewModel = ViewModelProvider(this).get(ApplicantInfoViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val token = SharedPreferencesManager.getInstance(this).getString("token", "null")
        val userId = intent.getLongExtra("userId", 0)

        viewModel.getUserInfo(token, userId)

        val observer = Observer<MyPageDetail?> {
            val progress = viewModel.userInfo.value?.reviewScore
            val progressBar = binding.progressBarRating
            if (progress != null) {
                progressBar.progress = (progress * 10).toInt()
                Log.d("progress", "${progressBar.progress}")
            }
            progressBar.max = 50
        }
        viewModel.userInfo.observe(this, observer)
    }
}