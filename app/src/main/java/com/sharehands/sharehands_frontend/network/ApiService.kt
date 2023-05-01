package com.sharehands.sharehands_frontend.network

import com.sharehands.sharehands_frontend.model.search.ServiceItem
import com.sharehands.sharehands_frontend.model.signin.LoginResponse
import com.sharehands.sharehands_frontend.model.signin.PostUserResponse
import com.sharehands.sharehands_frontend.network.signin.UserInfoDetail
import com.sharehands.sharehands_frontend.network.signin.UserInfoEdit
import com.sharehands.sharehands_frontend.network.signin.UserInterest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

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
    ): Call<Void>

    // 관심분야 전송
    @POST("/user/data/interests")
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
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Part("createServiceDto") createServiceDto: RequestBody,
        @Part files: ArrayList<MultipartBody.Part>
    ): Call<Void>

    // 봉사활동 목록 처음 불러오기
    @GET("/service")
    fun getServicesInitial(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("category") category: Int,
        @Query("sort") sort: Int
    ): Call<List<ServiceItem>>

    // 봉사활동 목록 스크롤시 불러오기
    @GET("/service")
    fun getServicesAdditional(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("category") category: Int,
        @Query("sort") sort: Int,
        @Query("last") last: Int
    ): Call<List<ServiceItem>>

    // 5. My Page

    // 회원정보 수정
    @POST("/user/my-page/edit")
    fun editUserInfo(
        @Body userInfoEdit: UserInfoEdit
    ): Call<Void>
}