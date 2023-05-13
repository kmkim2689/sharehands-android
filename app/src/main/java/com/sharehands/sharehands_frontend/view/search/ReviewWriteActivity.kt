package com.sharehands.sharehands_frontend.view.search

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivityReviewWriteBinding
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.ProgressDialog
import com.sharehands.sharehands_frontend.viewmodel.search.ReviewWriteViewModel

class ReviewWriteActivity: AppCompatActivity() {
    lateinit var binding: ActivityReviewWriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_write)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review_write)
        val viewModel = ViewModelProvider(this).get(ReviewWriteViewModel::class.java)


        val token = SharedPreferencesManager.getInstance(this).getString("token", "null")
        val serviceId = intent.getIntExtra("serviceId", 0)
        Log.d("serviceId", "${serviceId}")

        binding.ratingBarAchievement.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            Log.d("achievement", "${rating.toDouble()}")
            viewModel.onAchievementChanged(rating.toDouble())
        }

        binding.ratingBarTraffic.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            Log.d("traffic", "${rating.toDouble()}")
            viewModel.onTrafficChanged(rating.toDouble())
        }

        binding.ratingBarSystem.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            Log.d("system", "${rating.toDouble()}")
            viewModel.onSystemChanged(rating.toDouble())
        }



        binding.editRateOverall.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length!! >= 10) {
                    binding.viewNotFulfilled.visibility = View.INVISIBLE
                    binding.viewFulfilled.visibility = View.VISIBLE
                    if (binding.btnSubmitActive.visibility == View.GONE) {
                        binding.btnSubmitActive.visibility = View.VISIBLE
                        binding.btnSubmitInactive.visibility = View.GONE
                        binding.btnSubmitActive.startAnimation(
                            AnimationUtils.loadAnimation(
                                this@ReviewWriteActivity,
                                R.anim.fade_in
                            )
                        )
                    }

                } else {
                    binding.viewNotFulfilled.visibility = View.VISIBLE
                    binding.viewFulfilled.visibility = View.INVISIBLE
                    binding.btnSubmitActive.visibility = View.GONE
                    binding.btnSubmitInactive.visibility = View.VISIBLE
                }
                binding.tvCurrLength.text = s.length.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.btnSubmitInactive.setOnClickListener {
            binding.tvWarning.visibility = View.VISIBLE
        }

        binding.btnSubmitActive.setOnClickListener {
            val progressDialog = ProgressDialog(this, "업로드 중")
            progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressDialog.setCancelable(false)
            progressDialog.show()
            if (token != "null") {
                viewModel.uploadReview(token, serviceId.toLong(), binding.editRateOverall.text.toString())
                viewModel.isSuccessful.observe(this) {
                    if (viewModel.isSuccessful.value == true) {
                        progressDialog.dismiss()
                        Log.d("리뷰 작성 완료", "${viewModel.isSuccessful.value}")
                        showSnackbar("리뷰 작성이 완료되었습니다.")
                        finish()
                    } else {
                        progressDialog.dismiss()
                        Log.d("리뷰 작성 완료", "${viewModel.isSuccessful.value}")
                        showSnackbar("네트워크 오류가 발생하였습니다.")
                    }
                }
            } else {
                Log.d("토큰 없음", "업로드 실패")
            }
        }

        binding.ivGoBack.setOnClickListener {
            finish()
        }
    }

    private fun showSnackbar(text: String) {
        val snackbar = Snackbar.make(binding.coordinatorLayout, text, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }
}