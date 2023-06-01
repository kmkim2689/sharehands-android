package com.sharehands.sharehands_frontend.network

import com.sharehands.sharehands_frontend.model.signin.LoginResponse
import com.sharehands.sharehands_frontend.model.signin.PostUserResponse
import com.sharehands.sharehands_frontend.network.home.HomeData
import com.sharehands.sharehands_frontend.network.home.RegisterToken
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
    @GET("/main-page")
    fun getMainPage(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String
    ): Call<HomeData>

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
    @GET("/profile")
    fun getUserProfile(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("userId") userId: Long
    ): Call<UserProfile>

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

    // 리뷰 불러오기 초기
    @GET("/review/all")
    fun getReviews(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("workId") workId: Long
    ): Call<ReviewDetail>

    // 리뷰 불러오기 스크롤
    @GET("/review/all")
    fun getReviewsAdditional(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("workId") workId: Long,
        @Query("last") last: Int
    ): Call<ReviewDetail>

    // 리뷰 전송하기
    @POST("/review/new-review")
    fun uploadReview(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("workId") workId: Long,
        @Body reviewContent: ReviewUpload
    ): Call<Void>

    // 좋아요
    @POST("/manage/like")
    fun postLike(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("workId") workId: Long
    ): Call<Void>

    // 좋아요 취소
    @POST("/manage/unlike")
    fun cancelLike(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("workId") workId: Long
    ): Call<Void>

    // 스크랩
    @POST("/manage/scrap")
    fun postScrap(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("workId") workId: Long
    ): Call<Void>

    // 스크랩 취소
    @POST("/manage/unscrap")
    fun cancelScrap(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("workId") workId: Long
    ): Call<Void>

    // 신청한 회원 불러오기
    @GET("/applicants")
    fun getApplicants(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("workId") workId: Long
    ): Call<ApplicantsData>


    // 신청자 정보 호출하기
    @GET("/user-info")
    fun viewUserDetail(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("userId") userId: Long
    ): Call<MyPageDetail>

    // 거절하기
    @POST("/manage/reject")
    fun rejectUser(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("userId") userId: Long,
        @Query("workId") workId: Long
    ): Call<Void>

    // 제안하기
    @POST("/service/inviteUser")
    fun suggest(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("userId") userId: Long,
        @Query("workId") workId: Long
    ): Call<Void>

    // 제안 취소하기
    @POST("/service/cancelUser")
    fun cancelSuggest(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("userId") userId: Long,
        @Query("workId") workId: Long
    ): Call<Void>

    // 리뷰 허용하기
    @PUT("/manage/allow")
    fun allowReview(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("userId") userId: Long,
        @Query("workId") workId: Long
    ): Call<Void>

    // 모집글 신고
    @POST("/manage/reportWork")
    fun reportService(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Body reportBody: ReportBody
    ): Call<Void>

    // 리뷰 신고
    @POST("/manage/reportReview")
    fun reportReview(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Body reportBody: ReportBody
    ): Call<Void>

    // 차단
    @POST("/manage/block")
    fun blockUser(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("userId") userId: Long
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

    // 제안받은 봉사
    @GET("/my-page/invited-list")
    fun getSuggestedList(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String
    ): Call<SuggestedServices>

    @GET("/my-page/invited-list")
    fun getSuggestedListAdditional(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("last") lastService: Int
    ): Call<SuggestedServices>

    // 제안받은 봉사 수락
    @POST("/service/acceptInvitation")
    fun acceptSuggestion(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Query("workId") workId: Long
    ): Call<Void>

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

    @POST("/registerToken")
    fun sendRegToken(
        @Header("ACCESS_TOKEN") ACCESS_TOKEN: String,
        @Body registerToken: RegisterToken
    ): Call<Void>
}