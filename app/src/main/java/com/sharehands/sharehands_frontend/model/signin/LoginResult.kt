package com.sharehands.sharehands_frontend.model.signin

data class LoginResult(
    val isLoggedIn: Boolean,
    val email: String?,
    val profileUrl: String?
)
