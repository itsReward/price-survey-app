package com.pricesurvey.dto.user

data class UserResponse(
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: String,
    val isActive: Boolean,
    val assignedStores: List<StoreInfo>,
    val createdAt: String,
    val updatedAt: String?
) {
    data class StoreInfo(
        val id: Long,
        val name: String,
        val address: String
    )
}