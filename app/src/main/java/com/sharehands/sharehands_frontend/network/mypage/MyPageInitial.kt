package com.sharehands.sharehands_frontend.network.mypage

import com.google.gson.annotations.SerializedName

data class MyPageInitial(
    @SerializedName("nickname") val nickname: String,
    @SerializedName("level") val level: Long,
    @SerializedName("profileUrl") val profileUrl: String
)
