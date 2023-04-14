package com.sharehands.sharehands_frontend.network

import com.sharehands.sharehands_frontend.model.signin.LoginResponse
import com.sharehands.sharehands_frontend.model.signin.PostUserResponse
import com.sharehands.sharehands_frontend.network.signin.UserInfoDetail
import com.sharehands.sharehands_frontend.network.signin.UserInfoEdit
import com.sharehands.sharehands_frontend.network.signin.UserInterest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    // TODO : API Request 전송 방식 협의하기

    // 1. Login & Sign up

    // 구글 로그인
    @FormUrlEncoded
    @POST ("/login/google")
    fun loginGoogle(
        @Field("profileUrl") profileUrl: String,
        @Field("email") email: String
    ): Call<LoginResponse>

    // 카카오 로그인
    @FormUrlEncoded
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

    // 관심분야 전송
    @POST("/my-page/interest")
    fun postUserInterest(
        @Body userInterest: UserInterest
    ): Call<PostUserResponse>

    // 2. Home
    // 3. Schedule(not yet...)

    // 4. Search
    // 글쓰기
    @Multipart
    @POST("/service/new-service")
    fun uploadService(
        @Header("accessToken") accessToken: String,
        @Part("category") category: RequestBody,
        @Part("name") name: RequestBody,
        @Part("intro") intro: RequestBody,
        @Part("due") due: RequestBody,
        @Part("location") location: RequestBody,
        @Part("startDate") startDate: RequestBody,
        @Part("endDate") endDate: RequestBody,
        @Part("weekday") weekday: RequestBody,
        @Part("startTime") startTime: RequestBody,
        @Part("endTime") endTime: RequestBody,
        @Part("maxNum") maxNum: RequestBody,
        @Part("expense") expense: RequestBody,
        @Part("detailDesc") detailDesc: RequestBody,
        @Part("tel") tel: RequestBody,
        @Part("email") email: RequestBody,
        @Part("contactEtc") contactEtc: RequestBody,
        @Part imageList: ArrayList<MultipartBody.Part>
    ): Call<Unit>

    // 5. My Page

    // 회원정보 수정
    @POST("/user/my-page/edit")
    fun editUserInfo(
        @Body userInfoEdit: UserInfoEdit
    ): Call<Unit> // 자료형을 Unit으로 설정함으로써, 아무것도 안 받아올 수 있음
}