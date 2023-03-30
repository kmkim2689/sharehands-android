package com.sharehands.sharehands_frontend.view.mypage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivitySuggestedServiceBinding

class SuggestedServiceActivity: AppCompatActivity() {
    lateinit var binding: ActivitySuggestedServiceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suggested_service)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_suggested_service)
    }
}