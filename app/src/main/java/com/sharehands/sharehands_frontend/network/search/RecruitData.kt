package com.sharehands.sharehands_frontend.network.search

import com.google.gson.annotations.SerializedName

data class RecruitData(
    @SerializedName("postId") val postId: Int,
    @SerializedName("participatedList") val participatedList: List<UserRecruit>,
    @SerializedName("suggestionList") val suggestionList: List<UserRecruit>
)

data class UserRecruit(
    @SerializedName("name") val name: String,
    @SerializedName("level") val level: Int,
    @SerializedName("rating") val rating: Float,
    @SerializedName("userId") val userId: Int
)


