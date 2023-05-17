package com.sharehands.sharehands_frontend.network.search

import com.google.gson.annotations.SerializedName

data class ReportBody(
    @SerializedName("reportedId") val reportedId: Long,
    @SerializedName("reportType") val reportType: String
)
