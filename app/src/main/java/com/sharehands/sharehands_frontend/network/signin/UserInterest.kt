package com.sharehands.sharehands_frontend.network.signin

import com.google.gson.annotations.SerializedName

data class UserInterest(
    @SerializedName ("email") val email: String,
    @SerializedName ("interests") val interests: ArrayList<String>
)
