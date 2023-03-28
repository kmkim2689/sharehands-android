package com.sharehands.sharehands_frontend.view.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivityServiceWriteBinding

class ServiceWriteActivity: AppCompatActivity() {
    lateinit var binding: ActivityServiceWriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_write)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_service_write)
    }
}