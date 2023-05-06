package com.sharehands.sharehands_frontend.network.search

import com.google.gson.annotations.SerializedName

data class SearchResult(
    @SerializedName ("workCounter") val workCounter: Long,
    @SerializedName ("workList") val serviceList: List<ServiceList>,
)

data class ServiceList(
    @SerializedName ("workId") val workId: Long,
    @SerializedName ("imageUrl") val imageUrl: String,
    @SerializedName ("profileUrl") val profileUrl: String,
    @SerializedName ("nickname") val nickName: String,
    @SerializedName ("userApplied") val userApplied: Boolean,
    @SerializedName ("workName") val workName: String,
    @SerializedName ("location") val location: String,
    @SerializedName ("maxNum") val maxNum: String,
    @SerializedName ("date") val date: String,
    @SerializedName ("dow") val dow: String
)
