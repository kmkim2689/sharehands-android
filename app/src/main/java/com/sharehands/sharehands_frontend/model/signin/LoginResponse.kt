package com.sharehands.sharehands_frontend.model.signin

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName ("email") val email: String?,
    @SerializedName ("accessToken") val accessToken: String?
)
