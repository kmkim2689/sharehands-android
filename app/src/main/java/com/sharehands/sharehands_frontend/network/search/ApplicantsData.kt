package com.sharehands.sharehands_frontend.network.search


import com.google.gson.annotations.SerializedName

data class ApplicantsData(
    @SerializedName ("participatedList") val participatedList: List<Participated?>?,
    @SerializedName ("participatedNum") val participatedNum: Int?,
    @SerializedName ("recruitNum") val recruitNum: Int?,
    @SerializedName ("suggestionList") val suggestionList: List<Suggestion?>?,
    @SerializedName ("suggestionNum") val suggestionNum: Int?,
    @SerializedName ("workId") val workId: Long?
)

data class Participated(
    @SerializedName ("isPermission") val isPermission: Boolean?,
    @SerializedName ("level") val level: Long?,
    @SerializedName ("name") val name: String?,
    @SerializedName ("profileUrl") val profileUrl: String?,
    @SerializedName ("rating") val rating: Double?,
    @SerializedName ("userId") val userId: Long?
)

data class Suggestion(
    @SerializedName("isInvited") val isInvited: Boolean?,
    @SerializedName("level") val level: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("profileUrl") val profileUrl: String?,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("userId") val userId: Long?
)