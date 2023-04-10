package com.sharehands.sharehands_frontend.network.signin

import com.google.gson.annotations.SerializedName

data class UserInterest(
    @SerializedName ("interest") val interest: ArrayList<String>
)
