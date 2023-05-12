package com.sharehands.sharehands_frontend.network

import com.sharehands.sharehands_frontend.model.signin.LoginResponse
import com.sharehands.sharehands_frontend.model.signin.PostUserResponse
import com.sharehands.sharehands_frontend.network.mypage.*
import com.sharehands.sharehands_frontend.network.schedule.DailyServices
import com.sharehands.sharehands_frontend.network.schedule.MonthlyServices
import com.sharehands.sharehands_frontend.network.schedule.TodayServices
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
    // 오늘의 봉사 호출
    @GET("/today")
    fun getTodayService(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String
    ): Call<TodayServices>

    // 월별 계획 호출
    @GET("/monthly")
    fun getMonthlyService(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Call<MonthlyServices>

    // 일별 계획 호출
    @GET("/daily")
    fun getDailyService(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int
    ): Call<DailyServices>

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

    // 봉사활동 검색 결과 스크롤
    @GET("/service/search")
    fun getSearchResultAdditional(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("keyword") keyword: String,
        @Query("last") last: Int
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
    @GET("/profile")
    fun getUserProfile(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("userId") userId: Long
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
        @Path("workId") workId: Long
    ): Call<Void>

    // 취소하기
    @POST("/service/{workId}/cancel")
    fun cancelApplyService(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Path("workId") workId: Long
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

    // 좋아요
    @POST("/manage/like")
    fun postLike(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        // 이거 Long임?
        @Query("workId") workId: Long
    ): Call<Void>

    // 좋아요 취소
    @POST("/manage/unlike")
    fun cancelLike(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        // 이거 Long임?
        @Query("workId") workId: Long
    ): Call<Void>

    // 스크랩
    @POST("/manage/scrap")
    fun postScrap(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        // 이거 Long임?
        @Query("workId") workId: Long
    ): Call<Void>

    // 스크랩 취소
    @POST("/manage/unscrap")
    fun cancelScrap(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        // 이거 Long임?
        @Query("workId") workId: Long
    ): Call<Void>

    // 5. My Page

    // 마이페이지 초기화면
    @GET("/my-page/preview")
    fun viewMyPage(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String
    ): Call<MyPageInitial>

    // 내 정보 호출하기
    @GET("/my-page")
    fun viewMyPageDetail(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String
    ): Call<MyPageDetail>

    // 회원정보 수정
    @POST("/my-page/edit")
    fun editUserInfo(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Body userInfoEdit: UserInfoEdit
    ): Call<Void>

    // 모집한 봉사
    @GET("/my-page/recruit-list")
    fun getRecruitList(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String
    ): Call<RecruitedServices>

    // 모집한 봉사 - 스크롤
    @GET("/my-page/recruit-list")
    fun getRecruitListAdditional(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("last") last: Int
    ): Call<RecruitedServices>

    // 지원한 봉사
    @GET("/my-page/apply-list")
    fun getAppliedList(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String
    ): Call<RecruitedServices>

    // 지원한 봉사 - 스크롤
    @GET("/my-page/apply-list")
    fun getAppliedListAdditional(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("last") last: Int
    ): Call<RecruitedServices>

    // 완료한 봉사
    @GET("/my-page/participate-list")
    fun getCompleteList(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String
    ): Call<CompletedServices>

    // 완료한 봉사 - 스크롤
    @GET("/my-page/participate-list")
    fun getCompleteListAdditional(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("last") lastService: Int
    ): Call<CompletedServices>

    // 스크랩한 봉사
    @GET("/my-page/scrap-list")
    fun getScrapedList(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String
    ): Call<ScrapedServices>

    // 스크랩한 봉사 - 스크롤
    @GET("/my-page/scrap-list")
    fun getScrapedListAdditional(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("last") lastService: Int
    ): Call<ScrapedServices>

    // 모집글 삭제
    @POST("/service/delete/{workId}")
    fun deleteService(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Path("workId") workId: Long
    ): Call<Void>

    // 회원탈퇴
    @POST("/my-page/withdraw")
    fun withdraw(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String
    ): Call<Void>
}