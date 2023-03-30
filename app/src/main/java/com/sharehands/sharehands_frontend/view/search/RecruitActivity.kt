package com.sharehands.sharehands_frontend.view.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivityRecruitBinding
import com.sharehands.sharehands_frontend.databinding.ActivityServiceDetailBinding

class RecruitActivity: AppCompatActivity() {
    lateinit var binding: ActivityRecruitBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recruit)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recruit)


    }
}