package com.sharehands.sharehands_frontend.view.mypage

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivityParticipatedServiceBinding

class ParticipatedServiceActivity: AppCompatActivity() {
    lateinit var binding: ActivityParticipatedServiceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_participated_service)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_participated_service)
    }
}