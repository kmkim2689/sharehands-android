package com.sharehands.sharehands_frontend.network.search.location


import com.google.gson.annotations.SerializedName

data class KakaoMapData(
    @SerializedName("documents")
    val documents: List<Document?>?,
    @SerializedName("meta")
    val meta: Meta?
)