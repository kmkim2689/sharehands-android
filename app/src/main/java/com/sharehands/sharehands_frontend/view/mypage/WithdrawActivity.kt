package com.sharehands.sharehands_frontend.view.mypage

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivityWithdrawBinding
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager

class WithdrawActivity: AppCompatActivity() {
    private lateinit var binding: ActivityWithdrawBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_withdraw)

        val sp = SharedPreferencesManager.getInstance(this)

        val chkbox = binding.chkAgree
        chkbox.setOnClickListener {
            if (chkbox.isChecked) {
                binding.btnWithdrawActive.visibility = View.VISIBLE
                binding.btnWithdrawInactive.visibility = View.INVISIBLE
            } else {
                binding.btnWithdrawActive.visibility = View.INVISIBLE
                binding.btnWithdrawInactive.visibility = View.VISIBLE
            }
        }

        binding.btnWithdrawActive.setOnClickListener {
            // TODO 탈퇴 API 호출
            // TODO sharedpreference 토큰 제거
            sp.deleteStringByKey("token")
            sp.deleteStringByKey("email")
        }

        binding.btnWithdrawInactive.setOnClickListener {
            binding.tvWarning.visibility = View.VISIBLE
        }

        binding.ivGoBack.setOnClickListener {
            finish()
        }
    }
}