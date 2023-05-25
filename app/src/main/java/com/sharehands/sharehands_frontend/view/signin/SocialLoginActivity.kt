package com.sharehands.sharehands_frontend.view.signin

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivitySocialLoginBinding
import com.sharehands.sharehands_frontend.model.signin.LoginData
import com.sharehands.sharehands_frontend.model.signin.LoginResponse
import com.sharehands.sharehands_frontend.model.signin.LoginResult
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.signin.GoogleLogin
import com.sharehands.sharehands_frontend.network.signin.KakaoLogin
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SocialLoginActivity: AppCompatActivity() {
    lateinit var binding: ActivitySocialLoginBinding
    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_social_login)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_social_login)

        sharedPreferencesManager = SharedPreferencesManager.getInstance(this)
        // 테스트용 sp 삭제하기
//        sharedPreferencesManager.deleteString()

        // 구글 로그인
        val firebaseAuth = FirebaseAuth.getInstance()

        val loggedInIntent = Intent(this, MainActivity::class.java)
        val joinIntent = Intent(this, TermsAgreeActivity::class.java)
        val interestsIntent = Intent(this, UserPreferencesActivity::class.java)

        binding.btnKakaoLogin.setOnClickListener {
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.d(ContentValues.TAG, "카카오계정으로 로그인 실패", error)
                } else if (token != null) {
                    Log.d(ContentValues.TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                    UserApiClient.instance.me { user, error ->
                        if (error != null) {
                            Log.d(ContentValues.TAG, "사용자 정보 요청 실패", error)
                        } else if (user != null) {
                            Log.d(ContentValues.TAG, "사용자 정보 요청 성공")
                            val kakaoEmail = user?.kakaoAccount?.email.toString()
                            val kakaoProfileUrl = user?.kakaoAccount?.profile?.profileImageUrl.toString()
                            Log.d("카카오 이메일", kakaoEmail)
                            Log.d("카카오 프로필", kakaoProfileUrl)
                            // 서버에 토큰요청
                            RetrofitClient.createRetorfitClient().loginKakao(kakaoProfileUrl, kakaoEmail)
                                .enqueue(object : Callback<LoginResponse> {
                                    override fun onResponse(
                                        call: Call<LoginResponse>,
                                        response: Response<LoginResponse>
                                    ) {
                                        Log.d("카카오 로그인 백엔드 서버에 토큰 요청", "요청")
                                        if (response.isSuccessful) {
                                            Log.d("카카오로 로그인 토큰 받아오기 성공", "${response.body()}")
                                            val result = response.body()
                                            if (result?.accessToken != null && result?.email != null) {
                                                Log.d("카카오 로그인 서버로부터 받는 accessToken", "${result?.accessToken}")
                                                SharedPreferencesManager.getInstance(this@SocialLoginActivity)
                                                    .saveString("email", result?.email.toString())
                                                if (result?.accessToken == "memberDetails") {
                                                    startActivity(joinIntent)
                                                } else if (result?.accessToken == "interests") {
                                                    startActivity(interestsIntent)
                                                } else {
                                                    SharedPreferencesManager.getInstance(this@SocialLoginActivity)
                                                        .saveString("token", result?.accessToken.toString())
                                                    startActivity(loggedInIntent)
                                                    finish()
                                                }
                                            } else if (result?.accessToken == null && result?.email != null) {
                                                SharedPreferencesManager.getInstance(this@SocialLoginActivity)
                                                    .saveString("email", result?.email.toString())
                                                startActivity(joinIntent)
                                            } else {
                                                Log.d("로그인 실패", "통신 오류")
                                            }
                                        } else {
                                            Log.d("카카오로 로그인 토큰 받아오기 실패(연결은 ok)", "${response.code()}")
                                        }
                                    }

                                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                        Log.d("카카오로 로그인 토큰 받아오기 실패", "${t.message}")
                                    }
                                })
                        }
                    }
                }
            }
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                Log.d("카카오톡 설치 확인", "설치")
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    Log.d("카카오톡 앱", "진입")
                    if (error != null) {
                        Log.d("카카오톡으로 로그인 실패", "${error}")
                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            Log.d("카카오톡 로그인", "취소")
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else if (token != null) {
                        Log.d(ContentValues.TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                        UserApiClient.instance.me { user, error ->
                            if (error != null) {
                                Log.d(ContentValues.TAG, "사용자 정보 요청 실패", error)
                            } else if (user != null) {
                                Log.d(ContentValues.TAG, "사용자 정보 요청 성공")
                                val kakaoEmail = user?.kakaoAccount?.email.toString()
                                val kakaoProfileUrl = user?.kakaoAccount?.profile?.profileImageUrl.toString()
                                Log.d("카카오 이메일", kakaoEmail)
                                Log.d("카카오 프로필", kakaoProfileUrl)
                                // 서버에 토큰 요청
                                RetrofitClient.createRetorfitClient().loginKakao(kakaoProfileUrl, kakaoEmail)
                                    .enqueue(object : Callback<LoginResponse> {
                                        override fun onResponse(
                                            call: Call<LoginResponse>,
                                            response: Response<LoginResponse>
                                        ) {
                                            if (response.isSuccessful) {
                                                Log.d("카카오로 로그인 토큰 받아오기 성공", "${response.body()}")
                                                val result = response.body()
                                                if (result?.accessToken != null && result?.email != null) {
                                                    Log.d("카카오 로그인 서버로부터 받는 accessToken", "${result?.accessToken}")
                                                    SharedPreferencesManager.getInstance(this@SocialLoginActivity)
                                                        .saveString("email", result?.email.toString())
                                                    if (result?.accessToken == "memberDetails") {
                                                        startActivity(joinIntent)
                                                    } else if (result?.accessToken == "interests") {
                                                        startActivity(interestsIntent)
                                                    } else {
                                                        SharedPreferencesManager.getInstance(this@SocialLoginActivity)
                                                            .saveString("token", result?.accessToken.toString())
                                                        startActivity(loggedInIntent)
                                                        finish()
                                                    }
                                                } else if (result?.accessToken == null && result?.email != null) {
                                                    Log.d("카카오 로그인으로 회원가입", "약관 동의로 이동")
                                                    SharedPreferencesManager.getInstance(this@SocialLoginActivity)
                                                        .saveString("email", result?.email.toString())
                                                    startActivity(joinIntent)
                                                } else {
                                                    Log.d("로그인 실패", "통신 오류")
                                                }
                                            } else {
                                                Log.d("카카오로 로그인 토큰 받아오기 실패(연결은 ok)", "${response.code()}")
                                            }
                                        }

                                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                            Log.d("카카오로 로그인 토큰 받아오기 실패", "${t.message}")
                                        }
                                    })
                            }
                        }
                    }
                }
            } else {
                Log.d("카카오톡 설치 확인", "미설치")
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                Log.d("카카오 로그인 절차 - 카카오 계정 로그인", "종료")
            }
            Log.d("카카오톡 로그인", "종료")
        }

        val loginLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(), ActivityResultCallback { result ->
                Log.e("result", result.toString())
                if (result.resultCode == RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        task.getResult(ApiException::class.java)?.let { account ->
                            val tokenId = account.idToken
                            // 구글 로그인 성공
                            if (tokenId != null && tokenId != "") {
                                Log.d("구글 소셜 로그인 성공", "${tokenId}")
                                val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
                                firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
                                    if (firebaseAuth.currentUser != null) {
                                        val user: FirebaseUser = firebaseAuth.currentUser!!
                                        val googleEmail = user.email.toString()
                                        val googleProfileUrl = user.photoUrl.toString()
                                        Log.d("구글 로그인 이메일 가져오기 성공", googleEmail)
                                        Log.d("구글 로그인 프로필 가져오기 성공", googleProfileUrl)
                                                RetrofitClient.createRetorfitClient().loginGoogle(googleProfileUrl, googleEmail)
                                                    .enqueue(object : Callback<LoginResponse> {
                                                        override fun onResponse(
                                                            call: Call<LoginResponse>,
                                                            response: Response<LoginResponse>
                                                        ) {
                                                            if (response.isSuccessful) {
                                                                Log.d("구글 로그인으로 서버로부터 토큰 받아오기 성공", "${response.body()}")
                                                                val result = response.body()
                                                                if (result?.accessToken != null && result?.email != null) {
                                                                    SharedPreferencesManager.getInstance(this@SocialLoginActivity)
                                                                        .saveString("email", result?.email.toString())
                                                                    if (result?.accessToken == "memberDetails") {
                                                                        startActivity(joinIntent)
                                                                    } else if (result?.accessToken == "interests") {
                                                                        startActivity(interestsIntent)
                                                                    } else {
                                                                        SharedPreferencesManager.getInstance(this@SocialLoginActivity)
                                                                            .saveString("token", result?.accessToken.toString())
                                                                        startActivity(loggedInIntent)
                                                                        finish()
                                                                    }
                                                                } else if (result?.accessToken == null && result?.email != null) {
                                                                    Log.d("구글 로그인으로 회원가입 진행", "약관 동의로 이동")
                                                                    SharedPreferencesManager.getInstance(this@SocialLoginActivity)
                                                                        .saveString("email", result?.email.toString())
                                                                    startActivity(joinIntent)
                                                                } else {
                                                                    Log.d("구글 로그인 실패", "통신 실패")
                                                                }
                                                            } else {
                                                                Log.d("구글 로그인으로 서버로부터 토큰 받아오기 실패(연결은 ok) :", "${response.code()}")
                                                            }
                                                        }

                                                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                                            Log.d("구글 로그인으로 서버로부터 토큰 받아오기 실패 :", "통신 실패")
                                                        }

                                                    })
                                    }
                                }
                            }

                        } ?: throw Exception()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        )

        binding.btnGoogleLogin.setOnClickListener {
            Log.d("구글 로그인", "진입")

            CoroutineScope(Dispatchers.IO).launch {
                // Google Sign in Options
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("305228364242-vuvna28p86npq7sjh7bf2knigdc4im99.apps.googleusercontent.com")
                    .requestEmail()
                    .requestProfile()
                    .build()

                // Google Sign in Client
                val gsc = GoogleSignIn.getClient(this@SocialLoginActivity, gso)

                // 로그인을 위한 클라이언트
                val signInIntent = gsc.signInIntent
                loginLauncher.launch(signInIntent)
            }
            Log.d("구글 로그인", "종료")
        }
    }
}