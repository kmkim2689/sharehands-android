package com.sharehands.sharehands_frontend.model.signin

import com.google.gson.annotations.SerializedName

data class PostUserResponse(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("email") val email: String

)
