package com.pricesurvey.dto.auth

data class RegisterRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val assignedStoreIds: List<Long> = emptyList()
)