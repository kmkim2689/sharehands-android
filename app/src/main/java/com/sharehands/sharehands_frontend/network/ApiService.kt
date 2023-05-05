package com.sharehands.sharehands_frontend.network

import com.sharehands.sharehands_frontend.model.signin.LoginResponse
import com.sharehands.sharehands_frontend.model.signin.PostUserResponse
import com.sharehands.sharehands_frontend.network.search.*
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
    ): Call<SearchResult>

    // 봉사활동 목록 스크롤시 불러오기
    @GET("/service")
    fun getServicesAdditional(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("category") category: Int,
        @Query("sort") sort: Int,
        @Query("last") last: Int
    ): Call<SearchResult>

    // 봉사활동 검색 결과
    @GET("/service/search")
    fun getSearchResult(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("keyword") keyword: String
    ): Call<SearchResult>


    // 모집글 호출하기
    @GET("/service/{workId}")
    fun getService(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Path("workId") workId: Int
    ): Call<ServiceContent>

    // 작성자 프로필 호출
    // TODO 1 method (query or path)
    // TODO 2 parameter (userId or authorId?)
    @GET("/user")
    fun getUserProfile(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("userId") userId: Int
    ): Call<UserProfile>

    // 지원자 명단 호출
    // TODO query parameter(serviceId or workId?)
    @GET("/applicants")
    fun getApplicants(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("serviceId") serviceId: Int
    ): Call<RecruitData>

    // 지원하기
    @POST("/service/{workId}/apply")
    fun applyService(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Path("workId") workId: Int
    ): Call<Void>

    // 취소하기
    @POST("/service/{workId}/cancel")
    fun cancelApplyService(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Path("workId") workId: Int
    ): Call<Void>

    // 리뷰 허용하기
    // TODO url(not exists...)

    // 리뷰 리스트 3개 호출 -> 제거대상?
    // TODO method(query or path)
    // TODO Url(???)
//    @GET("/service")
//    fun getReviewsPreview(
//        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
//
//    ): Call<>

    // 리뷰 불러오기 초기
    @GET("/review/all")
    fun getReviews(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("workId") workId: Int
    ): Call<ReviewDetail>

    // 리뷰 불러오기 스크롤
    @GET("/review/all")
    fun getReviewsAdditional(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("workId") workId: Int,
        @Query("last") last: Int
    ): Call<ReviewDetail>

    // 5. My Page

    // 회원정보 수정
    @POST("/user/my-page/edit")
    fun editUserInfo(
        @Body userInfoEdit: UserInfoEdit
    ): Call<Void>
}