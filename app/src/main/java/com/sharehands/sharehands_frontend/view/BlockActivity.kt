package com.sharehands.sharehands_frontend.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivityBlockBinding
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class BlockActivity: AppCompatActivity() {
    private lateinit var binding: ActivityBlockBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_block)

        val token = SharedPreferencesManager.getInstance(this).getString("token", "null")
        val userId = intent.getLongExtra("userId", 0)

        Log.d("userId", "${userId}")

        val chkbox = binding.chkAgree
        val btnInactive = binding.btnBlockInactive
        val btnActive = binding.btnBlockActive

        binding.ivGoBack.setOnClickListener {
            finish()
        }

        chkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (chkbox.isChecked) {
                btnInactive.visibility = View.GONE
                btnActive.visibility = View.VISIBLE
                btnActive.startAnimation(
                    AnimationUtils.loadAnimation(
                        this@BlockActivity,
                        R.anim.fade_in
                    )
                )
            } else {
                btnInactive.visibility = View.VISIBLE
                btnActive.visibility = View.GONE
                btnInactive.startAnimation(
                    AnimationUtils.loadAnimation(
                        this@BlockActivity,
                        R.anim.fade_in
                    )
                )
            }
        }

        btnActive.setOnClickListener {
            if (token != "null" && userId != 0.toLong()) {
                CoroutineScope(Dispatchers.Main).launch {
                    val result = submitBlock(token, userId)
                    if (result) {
                        Log.d("클라이언트 수락 화면 처리", "성공")
                        finish()
                        Snackbar.make(binding.coordinatorLayout, "사용자를 차단하였습니다.", 1000).show()
                    } else {
                        Log.d("클라이언트 수락 화면 처리", "실패")
                    }
                }
            }
        }
    }

    suspend fun submitBlock(token: String, userId: Long): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.createRetorfitClient().blockUser(token, userId)
                    .execute()
                if (response.code() == 200) {
                    Log.d("사용자 차단 성공", "${response.code()}")
                    true
                } else {
                    Log.d("사용자 차단 실패", "${response.code()}")
                    false
                }
            } catch (e: Exception) {
                Log.d("사용자 차단 exception 발생", "${e.message}")
                false
            }
        }
    }
}