package com.sharehands.sharehands_frontend.network.home

import com.google.gson.annotations.SerializedName

data class HomeData(
    @SerializedName("nickname") val nickname: String,
    @SerializedName("profileUrl") val profileUrl: String,
    @SerializedName("invitation") val invitation: Long,
    @SerializedName("suggestion") val suggestion: List<SuggestedItem>,
    @SerializedName("rankings") val rankings: List<RankingItem>,
    @SerializedName("popular") val popular: List<PopularItem>
)

data class SuggestedItem(
    @SerializedName("thumbnailUrl") val thumbnailUrl: String,
    @SerializedName("title") val title: String,
    @SerializedName("period") val period: String,
    @SerializedName("weekday") val weekday: String,
    @SerializedName("time") val time: String,
    @SerializedName("location") val location: String,
    @SerializedName("serviceId") val serviceId: Long
)

data class RankingItem(
    @SerializedName("ranking") val ranking: Long,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("level") val level: Long,
    @SerializedName("count") val count: Long,
    @SerializedName("userId") val userId: Long
)

data class PopularItem(
    @SerializedName("ranking") val ranking: Long,
    @SerializedName("title") val title: String,
    @SerializedName("views") val views: Long,
    @SerializedName("likes") val likes: Long,
    @SerializedName("scraps") val scraps: Long,
    @SerializedName("serviceId") val serviceId: Long,
    @SerializedName("total") val total: Long
)