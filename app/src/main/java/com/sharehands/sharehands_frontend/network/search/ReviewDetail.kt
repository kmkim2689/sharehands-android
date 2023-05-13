package com.sharehands.sharehands_frontend.network.search

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

// TODO 변수 확인 필요
data class ReviewDetail(
    @SerializedName("reviewAmount") val reviewAmount: Long,
    // worktitle인지 servicetitle인지
    @SerializedName("workTitle") val workTitle: String,
    @SerializedName("rateAvg") val rateAvg: Double,
    @SerializedName("rate1Avg") val rate1Avg: Double,
    @SerializedName("rate2Avg") val rate2Avg: Double,
    @SerializedName("rate3Avg") val rate3Avg: Double,
    // reviewLists인지 reviewList인지
    @SerializedName("reviewLists") val reviewLists: List<ReviewDetailItem>,
    @SerializedName("isPermission") val isPermission: Boolean
)

data class ReviewDetailItem(
    @SerializedName("workId") val workId: Long,
    @SerializedName("memberName") val memberName: String,
    @SerializedName("profileUrl") val profileUrl: String,
    // level string 맞나요...
    @SerializedName("level") val level: String,
    @SerializedName("rateAvg") val rateAvg: Double,
    @SerializedName("content") val content: String,
    @SerializedName("updatedDate") val updatedDate: String,
    @SerializedName("isAuthor") val isAuthor: Boolean
)