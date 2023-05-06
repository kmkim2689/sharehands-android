package com.sharehands.sharehands_frontend.network.mypage


import com.google.gson.annotations.SerializedName

data class RecruitedServices(
    @SerializedName("serviceCounter")
    val serviceCounter: Int,
    @SerializedName("serviceList")
    val serviceList: List<RecruitedService>
)

data class RecruitedService(
    @SerializedName("date")
    val date: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("nickName")
    val nickName: String,
    @SerializedName("profileUrl")
    val profileUrl: String,
    @SerializedName("workId")
    val serviceId: Int,
    @SerializedName("serviceName")
    val serviceName: String,
    @SerializedName("userApplied")
    val userApplied: Boolean
)