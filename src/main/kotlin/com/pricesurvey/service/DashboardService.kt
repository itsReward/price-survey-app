package com.pricesurvey.service

import com.pricesurvey.dto.dashboard.*
import com.pricesurvey.entity.User
import com.pricesurvey.entity.UserRole
import com.pricesurvey.repository.*
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class DashboardService(
    private val priceEntryRepository: PriceEntryRepository,
    private val storeRepository: StoreRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
    private val userLoginActivityRepository: UserLoginActivityRepository,
    private val authService: AuthService
) {

    fun getDashboardData(filters: DashboardFilters): DashboardData {
        val currentUser = authService.getCurrentUser()

        return if (currentUser.role == UserRole.ADMIN) {
            getAdminDashboardData(filters)
        } else {
            getUserDashboardData(currentUser, filters)
        }
    }

    private fun getAdminDashboardData(filters: DashboardFilters): DashboardData {
        // Get filtered price entries
        val priceEntries = priceEntryRepository.findWithFilters(
            filters.userId,
            filters.storeId,
            filters.productId
        )

        val totalStores = storeRepository.findByIsActive(true).size.toLong()
        val totalProducts = productRepository.findByIsActive(true).size.toLong()

        // Recent entries (last 10)
        val recentEntries = priceEntries.take(10).map { entry ->
            DashboardData.RecentEntry(
                id = entry.id!!,
                storeName = entry.store.name,
                productName = entry.product.name,
                price = entry.price,
                quantity = entry.quantity,
                createdAt = entry.createdAt!!
            )
        }

        // Price by store
        val priceByStore = priceEntries.groupBy { it.store.id }
            .map { (storeId, entries) ->
                val store = entries.first().store
                DashboardData.PriceByStore(
                    storeId = storeId!!,
                    storeName = store.name,
                    averagePrice = entries.map { it.price }.fold(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal(entries.size), 2, BigDecimal.ROUND_HALF_UP),
                    entryCount = entries.size.toLong()
                )
            }

        // Price by product
        val priceByProduct = priceEntries.groupBy { it.product.id }
            .map { (productId, entries) ->
                val product = entries.first().product
                DashboardData.PriceByProduct(
                    productId = productId!!,
                    productName = product.name,
                    averagePrice = entries.map { it.price }.fold(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal(entries.size), 2, BigDecimal.ROUND_HALF_UP),
                    entryCount = entries.size.toLong()
                )
            }

        // Price by date (grouped by day)
        val priceByDate = priceEntries.groupBy {
            it.createdAt?.toLocalDate()?.toString() ?: "Unknown"
        }.map { (date, entries) ->
            DashboardData.PriceByDate(
                date = date,
                averagePrice = entries.map { it.price }.fold(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal(entries.size), 2, BigDecimal.ROUND_HALF_UP),
                entryCount = entries.size.toLong()
            )
        }.sortedBy { it.date }

        // User activity
        val userActivity = getUserActivity()

        return DashboardData(
            totalPriceEntries = priceEntries.size.toLong(),
            totalStores = totalStores,
            totalProducts = totalProducts,
            recentEntries = recentEntries,
            priceByStore = priceByStore,
            priceByProduct = priceByProduct,
            priceByDate = priceByDate,
            userActivity = userActivity
        )
    }

    private fun getUserDashboardData(user: User, filters: DashboardFilters): DashboardData {
        // Regular users can only see data for their assigned stores
        val userStoreIds = user.assignedStores.map { it.id!! }
        val userFilters = filters.copy(userId = user.id)

        val priceEntries = priceEntryRepository.findWithFilters(
            userFilters.userId,
            userFilters.storeId ?: userStoreIds.firstOrNull(),
            userFilters.productId
        ).filter { entry ->
            userStoreIds.contains(entry.store.id!!)
        }

        val totalStores = user.assignedStores.size.toLong()
        val totalProducts = productRepository.findByIsActive(true).size.toLong()

        // Recent entries (last 10)
        val recentEntries = priceEntries.take(10).map { entry ->
            DashboardData.RecentEntry(
                id = entry.id!!,
                storeName = entry.store.name,
                productName = entry.product.name,
                price = entry.price,
                quantity = entry.quantity,
                createdAt = entry.createdAt!!
            )
        }

        // Empty lists for user-specific restrictions
        val priceByStore = emptyList<DashboardData.PriceByStore>()
        val priceByProduct = emptyList<DashboardData.PriceByProduct>()
        val priceByDate = emptyList<DashboardData.PriceByDate>()
        val userActivity = emptyList<DashboardData.UserActivity>()

        return DashboardData(
            totalPriceEntries = priceEntries.size.toLong(),
            totalStores = totalStores,
            totalProducts = totalProducts,
            recentEntries = recentEntries,
            priceByStore = priceByStore,
            priceByProduct = priceByProduct,
            priceByDate = priceByDate,
            userActivity = userActivity
        )
    }

    private fun getUserActivity(): List<DashboardData.UserActivity> {
        val users = userRepository.findActiveUsersByRole(UserRole.USER)
        return users.map { user ->
            val lastLogin = userLoginActivityRepository.findByUserOrderByLoggedInAtDesc(user)
                .firstOrNull()?.loggedInAt

            val entriesCount = priceEntryRepository.findByUserOrderByCreatedAtDesc(user).size.toLong()

            DashboardData.UserActivity(
                userId = user.id!!,
                userEmail = user.email,
                userName = "${user.firstName} ${user.lastName}",
                lastLogin = lastLogin,
                entriesCount = entriesCount
            )
        }
    }
}