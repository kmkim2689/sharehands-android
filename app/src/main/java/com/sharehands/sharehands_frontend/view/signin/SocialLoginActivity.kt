package com.sharehands.sharehands_frontend.view.signin

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivitySocialLoginBinding
import com.sharehands.sharehands_frontend.network.signin.SocialLogin

class SocialLoginActivity: AppCompatActivity() {
    lateinit var binding: ActivitySocialLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_social_login)
        val socialLogin = SocialLogin()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_social_login)
        binding.btnKakaoLogin.setOnClickListener {

            socialLogin.kakaoLogin(this)
        }
    }
}