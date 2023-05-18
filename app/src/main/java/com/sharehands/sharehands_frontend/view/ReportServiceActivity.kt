package com.sharehands.sharehands_frontend.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivityReportServiceBinding
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.search.ReportBody
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit

class ReportServiceActivity: AppCompatActivity() {
    private lateinit var binding: ActivityReportServiceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_report_service)

        val btn1 = binding.radioIllegal
        val btn2 = binding.radioNonRelevant
        val btn3 = binding.radioAdult
        val btn4 = binding.radioPersonal

        val token = SharedPreferencesManager.getInstance(this).getString("token", "null")
        val serviceId = intent.getLongExtra("serviceId", 0)
        var reason = ""
        Log.d("serviceId", "$serviceId")

        binding.ivGoBack.setOnClickListener {
            finish()
        }

        binding.radioReport.setOnCheckedChangeListener { group, checkedId ->

            binding.btnBlockInactive.visibility = View.GONE
            binding.btnBlockActive.visibility = View.VISIBLE
            binding.btnBlockActive.startAnimation(
                AnimationUtils.loadAnimation(
                    this,
                    R.anim.fade_in

                )
            )

            Log.d("checkedId", "${checkedId}")
            if (btn1.isChecked) {
                reason = "허위/불법 정보"
            } else if (btn2.isChecked) {
                reason = "봉사활동과 관련 없는 게시물"
            } else if (btn3.isChecked) {
                reason = "성인/음란물 유포"
            } else if (btn4.isChecked) {
                reason = "개인정보 악용 및 권리 침해"
            } else {
                reason = ""
            }
            Log.d("reason", "${reason}")
        }


        binding.btnBlockActive.setOnClickListener {
            if (reason != "") {
                CoroutineScope(Dispatchers.Main).launch {
                    val result = reportService(token, serviceId, reason)
                    if (result == true) {
                        Log.d("봉사활동 신고", "성공")
                        finish()
                        Snackbar.make(binding.coordinatorLayout, "봉사활동을 신고하였습니다", 1000).show()
                    } else {
                        Log.d("봉사활동 신고 실패", "실패")
                    }
                }
            }

        }
    }

    suspend fun reportService(token: String, serviceId: Long, reason: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.createRetorfitClient()
                    .reportService(token, ReportBody(serviceId, reason))
                    .execute()
                if (response.code() == 200) {
                    Log.d("봉사활동 신고 성공", "${response.code()}")
                    true
                } else {
                    Log.d("봉사활동 신고 실패", "${response.code()}")
                    false
                }
            } catch (e: Exception) {
                Log.d("봉사활동 신고 실패", "${e.message}")
                false
            }
        }
    }
}