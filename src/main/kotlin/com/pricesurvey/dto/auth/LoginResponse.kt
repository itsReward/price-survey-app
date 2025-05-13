package com.pricesurvey.dto.auth

data class LoginResponse(
    val token: String,
    val type: String = "Bearer",
    val user: UserInfo
)
