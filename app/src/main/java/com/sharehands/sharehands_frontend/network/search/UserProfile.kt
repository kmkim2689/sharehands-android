package com.sharehands.sharehands_frontend.network.search

import com.google.gson.annotations.SerializedName


data class UserProfile(
    @SerializedName ("appliedWork") val appliedWork: Int,
    @SerializedName ("avgrate") val avgrate: String,
    @SerializedName ("isAuthor") val isAuthor: Boolean,
    @SerializedName ("level") val level: String,
    @SerializedName ("location") val location: String,
    @SerializedName ("managedWork") val managedWork: Int,
    @SerializedName ("nickname") val nickname: String,
    @SerializedName ("participatedWork") val participatedWork: Int
)