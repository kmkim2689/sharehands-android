package com.sharehands.sharehands_frontend.view.signin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivitySocialLoginBinding

class SocialLoginActivity: AppCompatActivity() {
    lateinit var binding: ActivitySocialLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_social_login)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_social_login)

    }
}