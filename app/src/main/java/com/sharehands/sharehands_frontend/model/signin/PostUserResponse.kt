package com.sharehands.sharehands_frontend.model.signin

import com.google.gson.annotations.SerializedName

data class PostUserResponse(
    @SerializedName("email") val email: String,
    @SerializedName("accessToken") val accessToken: String
)
