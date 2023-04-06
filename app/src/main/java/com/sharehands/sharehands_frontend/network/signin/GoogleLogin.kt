package com.sharehands.sharehands_frontend.network.signin

import android.content.Context
import android.content.Intent
import com.sharehands.sharehands_frontend.view.MainActivity
import com.sharehands.sharehands_frontend.view.signin.TermsAgreeActivity

class GoogleLogin(context: Context) {
    val loggedInIntent = Intent(context, MainActivity::class.java)
    val joinIntent = Intent(context, TermsAgreeActivity::class.java)

    fun login(context: Context) {

    }
}