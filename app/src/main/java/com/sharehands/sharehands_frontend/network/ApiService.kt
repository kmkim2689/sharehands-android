package com.sharehands.sharehands_frontend.network

import com.sharehands.sharehands_frontend.model.signin.LoginResponse
import com.sharehands.sharehands_frontend.model.signin.PostUserResponse
import com.sharehands.sharehands_frontend.network.signin.UserInfoDetail
import com.sharehands.sharehands_frontend.network.signin.UserInfoEdit
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.POST

interface ApiService {
    // TODO : API Request 전송 방식 협의하기

    // 1. Login & Sign up

    // 구글 로그인
    @POST ("/login/google")
    fun loginGoogle(
        @Field("profileUrl") profileUrl: String,
        @Field("email") email: String
    ): Call<LoginResponse>

    // 카카오 로그인
    @POST ("/login/kakao")
    fun loginKakao(
        @Field("profileUrl") profileUrl: String,
        @Field("email") email: String
    ): Call<LoginResponse>

    // 가입 시 회원 데이터 전송
    @POST("/user/data")
    fun postUserDetail(
        @Body userInfoDetail: UserInfoDetail
    ): Call<PostUserResponse>


    // 2. Home
    // 3. Schedule(not yet...)
    // 4. Search
    // 5. My Page

    // 회원정보 수정
    @POST("/user/my-page/edit")
    fun editUserInfo(
        @Body userInfoEdit: UserInfoEdit
    ): Call<Unit> // 자료형을 Unit으로 설정함으로써, 아무것도 안 받아올 수 있음
}