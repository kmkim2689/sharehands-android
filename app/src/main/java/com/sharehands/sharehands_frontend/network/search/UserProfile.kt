package com.sharehands.sharehands_frontend.network.search

import com.google.gson.annotations.SerializedName


data class UserProfile(
    @SerializedName ("appliedWork") val appliedWork: Int,
    @SerializedName ("avgRate") val avgRate: Double,
    @SerializedName ("author") val author: Boolean,
    @SerializedName ("level") val level: Long,
    @SerializedName ("location") val location: String,
    @SerializedName ("managedWork") val managedWork: Int,
    @SerializedName ("nickname") val nickname: String,
    @SerializedName ("participatedWork") val participatedWork: Int,
    @SerializedName ("profileUrl") val profileUrl: String
)