package com.sharehands.sharehands_frontend.view.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivitySocialLoginBinding
import com.sharehands.sharehands_frontend.network.signin.GoogleLogin
import com.sharehands.sharehands_frontend.network.signin.KakaoLogin
import com.sharehands.sharehands_frontend.view.MainActivity

class SocialLoginActivity: AppCompatActivity() {
    lateinit var binding: ActivitySocialLoginBinding
    val loggedInIntent = Intent(this, MainActivity::class.java)
    val joinIntent = Intent(this, TermsAgreeActivity::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_social_login)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_social_login)

        val kakaoLogin = KakaoLogin(this)
        binding.btnKakaoLogin.setOnClickListener {
            val loginResult = kakaoLogin.login(this)
            if (loginResult.accessToken != null) {
                Log.d("쉐어핸즈 로그인", "완료")
                startActivity(loggedInIntent)
            } else {
                Log.d("쉐어핸즈 회원가입", "진행")
                startActivity(joinIntent)
            }
        }

        val googleLogin = GoogleLogin(this)
        binding.btnGoogleLogin.setOnClickListener {
            googleLogin.login(this)
        }
    }
}