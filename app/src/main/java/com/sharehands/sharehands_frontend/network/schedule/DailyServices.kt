package com.sharehands.sharehands_frontend.network.schedule

import com.google.gson.annotations.SerializedName

data class DailyServices(
    @SerializedName("workCounter")
    val workCounter: Int?,
    @SerializedName("workList")
    val workList: List<DailyService>
)

data class DailyService(
    @SerializedName("workId") val workId: Long,
    @SerializedName("workName") val workName: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("location") val location: String,
    @SerializedName("startTime") val startTime: String,
    @SerializedName("endTime") val endTime: String
)
