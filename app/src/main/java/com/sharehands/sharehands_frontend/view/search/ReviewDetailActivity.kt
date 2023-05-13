package com.sharehands.sharehands_frontend.view.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivityReviewDetailBinding

class ReviewDetailActivity: AppCompatActivity() {
    lateinit var binding: ActivityReviewDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_detail)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review_detail)

        val serviceId = intent.getIntExtra("serviceId", 0)
        Log.d("serviceId", "${serviceId}")

        binding.btnWriteService.setOnClickListener {
            val intent = Intent(this, ReviewWriteActivity::class.java)
            intent.putExtra("serviceId", serviceId)
            startActivity(intent)
        }

    }
}