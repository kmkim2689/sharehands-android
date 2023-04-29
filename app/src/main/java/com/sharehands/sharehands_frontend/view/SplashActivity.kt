package com.sharehands.sharehands_frontend.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.common.util.Utility
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.signin.SocialLoginActivity

class SplashActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var keyHash = Utility.getKeyHash(this)
        Log.d("keyhash", keyHash)

        Handler(Looper.getMainLooper()).postDelayed({
            val sp = SharedPreferencesManager.getInstance(this)
            val token = sp.getString("token", "null")
            Log.d("token", "${token}")
            if (token != "null") {
                val loggedInIntent = Intent(this, MainActivity::class.java)
                startActivity(loggedInIntent)
                finish()
            } else {
                val signInIntent = Intent(this, SocialLoginActivity::class.java)
                startActivity(signInIntent)
                finish()
            }
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }, 2000)
    }
}