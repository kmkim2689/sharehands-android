package com.sharehands.sharehands_frontend.view.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivityReviewWriteBinding

class ReviewWriteActivity: AppCompatActivity() {
    lateinit var binding: ActivityReviewWriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_write)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review_write)
    }
}