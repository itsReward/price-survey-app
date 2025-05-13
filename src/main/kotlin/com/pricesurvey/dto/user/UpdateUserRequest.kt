package com.pricesurvey.dto.user

data class UpdateUserRequest(
    val firstName: String?,
    val lastName: String?,
    val password: String?,
    val isActive: Boolean?,
    val assignedStoreIds: List<Long>?
)