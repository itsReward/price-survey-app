package com.pricesurvey.dto.user

data class UserRequest(
    val email: String,
    val password: String? = null,
    val firstName: String,
    val lastName: String,
    val role: String,
    val assignedStoreIds: List<Long> = emptyList()
)