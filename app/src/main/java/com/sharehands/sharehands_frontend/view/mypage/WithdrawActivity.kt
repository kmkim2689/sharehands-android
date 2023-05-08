package com.sharehands.sharehands_frontend.view.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivityWithdrawBinding
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.signin.SocialLoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WithdrawActivity: AppCompatActivity() {
    private lateinit var binding: ActivityWithdrawBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_withdraw)

        val sp = SharedPreferencesManager.getInstance(this)
        val token = SharedPreferencesManager.getInstance(this).getString("token", "null")
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
            if (token != "null") {
                RetrofitClient.createRetorfitClient().withdraw(token)
                    .enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                Log.d("회원탈퇴 성공", "${response.code()}")
                                val intent = Intent(this@WithdrawActivity, SocialLoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Log.d("회원탈퇴 실패", "${response.code()}")
                            }

                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.d("회원탈퇴 성공", "${t.message}")
                        }

                    })
                // TODO 탈퇴 API 호출
                // TODO sharedpreference 토큰 제거
                sp.deleteStringByKey("token")
                try {
                    sp.deleteStringByKey("email")
                } catch (e: Exception) {

                }

            }

        }

        binding.btnWithdrawInactive.setOnClickListener {
            binding.tvWarning.visibility = View.VISIBLE
        }

        binding.ivGoBack.setOnClickListener {
            finish()
        }
    }
}