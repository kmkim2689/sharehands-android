package com.sharehands.sharehands_frontend.network.signin

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION_CODES.P
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.model.signin.LoginData
import com.sharehands.sharehands_frontend.model.signin.LoginResponse
import com.sharehands.sharehands_frontend.model.signin.LoginResult
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.view.MainActivity
import com.sharehands.sharehands_frontend.view.signin.SocialLoginActivity
import com.sharehands.sharehands_frontend.view.signin.TermsAgreeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoogleLogin(private val context: Context) {
    var isLoggedIn: Boolean? = null
    var email: String? = null
    var profileUrl: String? = null

    private lateinit var firebaseAuth: FirebaseAuth

    // Google Sign in Options
    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(R.string.default_web_client_id.toString())
        .requestEmail()
        .requestProfile()
        .build()

    // Google Sign in Client
    private val gsc = GoogleSignIn.getClient(context, gso)

    // 로그인을 위한 클라이언트
    private val signInIntent = gsc.signInIntent
    private val socialLoginActivity = context as SocialLoginActivity


    fun login(context: Context): LoginResponse {
        Log.d("구글 로그인", "진입")
        lateinit var loginResponse: LoginResponse

        CoroutineScope(Dispatchers.IO).launch {
            val loginLauncher = socialLoginActivity.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(), ActivityResultCallback { result ->
                    Log.e("result", result.toString())
                    val googleLoginResult = getGoogleUserInfo(result)
                    val email = googleLoginResult.email
                    val profileUrl = googleLoginResult.profileUrl
//                    RetrofitClient.createRetorfitClient().loginGoogle(profileUrl.toString(), email.toString())
//                        .enqueue(object : Callback<LoginResponse> {
//                            override fun onResponse(
//                                call: Call<LoginResponse>,
//                                response: Response<LoginResponse>
//                            ) {
//                                if (response.isSuccessful) {
//                                    Log.d("구글 로그인으로 서버로부터 토큰 받아오기 성공", "${response.body()}")
//                                    val result = response.body()
//                                    val accessToken = result?.accessToken.toString()
//                                    loginResponse = LoginResponse(email.toString(), accessToken)
//                                } else {
//                                    Log.d("구글 로그인으로 서버로부터 토큰 받아오기 실패(연결은 ok) :", "${response.code()}")
//                                    loginResponse = LoginResponse(null, null)
//                                }
//                            }
//
//                            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                                Log.d("구글 로그인으로 서버로부터 토큰 받아오기 실패 :", "통신 실패")
//                                loginResponse = LoginResponse(null, null)
//                            }
//
//                        })
                    loginResponse = LoginResponse(email, null)
                }
            )

            loginLauncher.launch(signInIntent)
        }
        Log.d("구글 로그인", "종료")
        return loginResponse
    }

    private fun getGoogleUserInfo(result: ActivityResult): LoginResult {
        // 구글 로그인 했는데 결과가 올바르게 나온다면
        lateinit var loginResult: LoginResult
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                task.getResult(ApiException::class.java)?.let { account ->
                    val tokenId = account.idToken
                    // 구글 로그인 성공
                    if (tokenId != null && tokenId != "") {
                        Log.d("구글 소셜 로그인 성공", "${tokenId}")
                        val loginData = getEmailProfile(account)
                        loginResult = LoginResult(true, loginData.email, loginData.profileUrl)
                    } else {
                        loginResult = LoginResult(false, null, null)
                    }

                } ?: throw Exception()
            } catch (e: Exception) {
                e.printStackTrace()
                loginResult = LoginResult(false, null, null)
            }
        } else {
            loginResult = LoginResult(false, null, null)
        }

        return loginResult
    }

    private fun getEmailProfile(account: GoogleSignInAccount): LoginData {
        lateinit var loginData: LoginData
        val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if(firebaseAuth.currentUser != null) {
                val user: FirebaseUser = firebaseAuth.currentUser!!
                email = user.email.toString()
                profileUrl = user.photoUrl.toString()
                Log.d("구글 로그인 이메일 가져오기 성공", email.toString())
                Log.d("구글 로그인 프로필 가져오기 성공", profileUrl.toString())
                loginData = LoginData(email, profileUrl)
            }
        }
        return loginData

    }

    private fun requestToken(): LoginResponse {
        TODO("Not yet implemented")
    }

}