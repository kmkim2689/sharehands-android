package com.sharehands.sharehands_frontend.network.mypage

import com.google.gson.annotations.SerializedName

data class CompletedServices(
    @SerializedName("lastApplyId")
    val lastApplyId: Long?,
    @SerializedName("serviceCounter")
    val serviceCounter: Long?,
    @SerializedName("serviceList")
    val serviceList: List<RecruitedService?>?
)
