package com.sharehands.sharehands_frontend.network.signin

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.model.signin.LoginResponse
import com.sharehands.sharehands_frontend.model.signin.LoginResult
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.view.MainActivity
import com.sharehands.sharehands_frontend.view.signin.TermsAgreeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KakaoLogin(context: Context) {
    var isLoggedIn: Boolean? = null
    var email: String? = null
    var profileUrl: String? = null

    fun login(context: Context): LoginResponse {
        lateinit var loginResult: LoginResponse
        var accessToken: String? = null
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(ContentValues.TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                getKakaoUserInfo()
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e(ContentValues.TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {
                    Log.i(ContentValues.TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")

                    // 로그인 성공 시 사용자 정보를 가져오는 코드
                    val kakaoLoginResult = getKakaoUserInfo()
                    isLoggedIn = getKakaoUserInfo().isLoggedIn
                    email = getKakaoUserInfo().email
                    profileUrl = getKakaoUserInfo().profileUrl

                    // 서버에 토큰 요청
                    val finalResult = requestToken(profileUrl, email)
                    accessToken = finalResult.accessToken
                    loginResult = LoginResponse(email.toString(), accessToken)
//                    context.startActivity(loggedInIntent)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }

        return loginResult
    }

    private fun requestToken(profileUrl: String?, email: String?): LoginResponse {
        var finalEmail: String = email.toString()
        var accessToken: String? = null
        RetrofitClient.createRetorfitClient().loginKakao(profileUrl.toString(), email.toString())
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {

                    if (response.isSuccessful) {
                        Log.d("카카오로 로그인 토큰 받아오기 성공", "${response.body()}")
                        val result = response.body()
                        finalEmail = email.toString()
                        accessToken = result?.accessToken.toString()
                    } else {
                        Log.d("카카오로 로그인 토큰 받아오기 실패(연결은 ok)", "${response.code()}")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.d("카카오로 로그인 토큰 받아오기 실패", "${t.message}")
                }
            })
        return LoginResponse(finalEmail, accessToken)
    }

    private fun getKakaoUserInfo(): LoginResult {

        lateinit var result: LoginResult
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
                result = LoginResult(false, null, null)
            } else if (user != null) {
                Log.i(TAG, "사용자 정보 요청 성공")
                val kakaoEmail = user?.kakaoAccount?.email.toString()
                val kakaoProfileUrl = user?.kakaoAccount?.profile?.profileImageUrl.toString()
                Log.d("카카오 이메일", kakaoEmail)
                Log.d("카카오 프로필", kakaoProfileUrl)
                result = LoginResult(true, kakaoEmail, kakaoProfileUrl)
            }
        }
        return result
    }
}