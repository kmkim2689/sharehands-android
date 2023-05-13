package com.sharehands.sharehands_frontend.network.search


import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Review(
    @SerializedName("isAuthor")
    val isAuthor: Boolean,
    @SerializedName("content")
    val content: String,
    @SerializedName("level")
    val level: Long,
    @SerializedName("memberName")
    val memberName: String,
    @SerializedName("profileUrl")
    val profileUrl: String,
    // 사용자가 남긴 평점
    @SerializedName("rateAvg")
    val rateAvg: Double,
    @SerializedName("updatedDate")
    val updatedDate: String,
    @SerializedName("workId")
    val workId: Long,
    @SerializedName("reviewId")
    val reviewId: Int
)