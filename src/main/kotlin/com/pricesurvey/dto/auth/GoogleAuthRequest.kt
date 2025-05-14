package com.pricesurvey.dto.auth

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class GoogleAuthRequest(
    @field:NotBlank(message = "Google token is required")
    val token: String,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String,

    @field:NotBlank(message = "First name is required")
    val firstName: String,

    @field:NotBlank(message = "Last name is required")
    val lastName: String,

    val picture: String? = null
)