package com.sharehands.sharehands_frontend.network.search


import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Review(
    @SerializedName("author")
    val author: Boolean,
    @SerializedName("content")
    val content: String,
    @SerializedName("level")
    val level: String,
    @SerializedName("memberName")
    val memberName: String,
    @SerializedName("profileUrl")
    val profileUrl: String,
    // 사용자가 남긴 평점
    @SerializedName("rateAvg")
    val rateAvg: Int,
    @SerializedName("updatedDate")
    val updatedDate: LocalDateTime,
    @SerializedName("workId")
    val workId: Long
)