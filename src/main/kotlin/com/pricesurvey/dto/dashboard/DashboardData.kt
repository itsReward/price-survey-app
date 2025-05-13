package com.pricesurvey.dto.dashboard

import java.math.BigDecimal
import java.time.LocalDateTime

data class DashboardData(
    val totalPriceEntries: Long,
    val totalStores: Long,
    val totalProducts: Long,
    val recentEntries: List<RecentEntry>,
    val priceByStore: List<PriceByStore>,
    val priceByProduct: List<PriceByProduct>,
    val priceByDate: List<PriceByDate>,
    val userActivity: List<UserActivity>
) {
    data class RecentEntry(
        val id: Long,
        val storeName: String,
        val productName: String,
        val price: BigDecimal,
        val quantity: Int,
        val createdAt: LocalDateTime
    )

    data class PriceByStore(
        val storeId: Long,
        val storeName: String,
        val averagePrice: BigDecimal,
        val entryCount: Long
    )

    data class PriceByProduct(
        val productId: Long,
        val productName: String,
        val averagePrice: BigDecimal,
        val entryCount: Long
    )

    data class PriceByDate(
        val date: String,
        val averagePrice: BigDecimal,
        val entryCount: Long
    )

    data class UserActivity(
        val userId: Long,
        val userEmail: String,
        val userName: String,
        val lastLogin: LocalDateTime?,
        val entriesCount: Long
    )
}

