package com.sharehands.sharehands_frontend.network.search

import com.google.gson.annotations.SerializedName

data class ReviewUpload(
    @SerializedName("rate1") val rate1: Double,
    @SerializedName("rate2") val rate2: Double,
    @SerializedName("rate3") val rate3: Double,
    @SerializedName("content") val content: String
    )
