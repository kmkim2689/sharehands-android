package com.sharehands.sharehands_frontend.network.search


import com.google.gson.annotations.SerializedName

data class ServiceContent(
    @SerializedName("applydeadline")
    val applydeadline: String?,
    @SerializedName("area")
    val area: String?,
    @SerializedName("author")
    val author: Boolean?,
    @SerializedName("category")
    val category: String?,
    @SerializedName("contactEtc")
    val contactEtc: String?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("cost")
    val cost: Int?,
    @SerializedName("didApply")
    val didApply: Boolean?,
    @SerializedName("didLike")
    val didLike: Boolean?,
    @SerializedName("didScrap")
    val didScrap: Boolean?,
    @SerializedName("dow")
    val dow: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("endDate")
    val endDate: String?,
    @SerializedName("endTime")
    val endTime: String?,
    @SerializedName("hitCnt")
    val hitCnt: Long?,
    @SerializedName("intro")
    val intro: String?,
    @SerializedName("level")
    val level: Long,
    @SerializedName("likeCnt")
    val likeCnt: Long?,
    @SerializedName("nickname")
    val nickname: String?,
    @SerializedName("photoList")
    val photoList: List<String>?,
    @SerializedName("profileUrl")
    val profileUrl: String?,
    @SerializedName("recruitNum")
    val recruitNum: Int?,
    @SerializedName("reviewLists")
    val reviewLists: List<Review>?,
    @SerializedName("scrapCnt")
    val scrapCnt: Long?,
    @SerializedName("startDate")
    val startDate: String?,
    @SerializedName("startTime")
    val startTime: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("tel")
    val tel: String?,
    @SerializedName("userId")
    val userId: Long?,
    @SerializedName("userRate")
    val userRate: Double?,
    @SerializedName("workTitle")
    val workTitle: String?,
    @SerializedName ("isExpired")
    val isExpired: Boolean,
    @SerializedName ("isFull")
    val isFull: Boolean
)