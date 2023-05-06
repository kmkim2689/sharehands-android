package com.sharehands.sharehands_frontend.network.mypage

import com.google.gson.annotations.SerializedName


data class MyPageDetail(
    @SerializedName("complete")
    val complete: Int,
    @SerializedName("dob")
    val dob: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("interests")
    val interests: List<String>,
    @SerializedName("level")
    val level: Int,
    @SerializedName("location")
    val location: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("participate")
    val participate: Int,
    @SerializedName("levelPercent")
    val levelPercent: Int,
    @SerializedName("reviewPercent")
    val reviewPercent: Int,
    @SerializedName("profileUrl")
    val profileUrl: String,
    @SerializedName("recruit")
    val recruit: Int,
    @SerializedName("review")
    val review: Int,
    @SerializedName("reviewScore")
    val reviewScore: Double,
    @SerializedName("tel")
    val tel: String,
    @SerializedName("volunteer")
    val volunteer: Int
)