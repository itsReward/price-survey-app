package com.pricesurvey.dto.auth

data class LoginRequest(
    val email: String,
    val password: String
)