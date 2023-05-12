package com.sharehands.sharehands_frontend.network.schedule

import com.google.gson.annotations.SerializedName

data class MonthlyServices(
    @SerializedName("workCounter") val workCounter: Int,
    @SerializedName("workList") val workList: List<MonthlyService>
)

data class MonthlyService(
    @SerializedName("workId") val workId: Long,
    @SerializedName("workName") val workName: String,
    @SerializedName("dDay") val dDay: Int,
    @SerializedName("location") val location: String,
    @SerializedName("year") val year: Int,
    @SerializedName("month") val month: Int,
    @SerializedName("day") val day: Int,
    @SerializedName("time") val time: String
)
