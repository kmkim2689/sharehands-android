package com.sharehands.sharehands_frontend.network.schedule

import com.google.gson.annotations.SerializedName


data class TodayServices(
    @SerializedName("workCounter")
    val workCounter: Int?,
    @SerializedName("workList")
    val workList: List<TodayService>
)


data class TodayService(
    @SerializedName("location")
    val location: String?,
    @SerializedName("time")
    val time: String?,
    @SerializedName("workName")
    val workName: String?
)