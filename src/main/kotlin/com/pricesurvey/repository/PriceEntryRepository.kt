package com.pricesurvey.repository

import com.pricesurvey.entity.PriceEntry
import com.pricesurvey.entity.Store
import com.pricesurvey.entity.Product
import com.pricesurvey.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface PriceEntryRepository : JpaRepository<PriceEntry, Long> {

    @Query("SELECT pe FROM PriceEntry pe WHERE (:userId IS NULL OR pe.user.id = :userId) " +
            "AND (:storeId IS NULL OR pe.store.id = :storeId) " +
            "AND (:productId IS NULL OR pe.product.id = :productId) " +
            "ORDER BY pe.createdAt DESC")
    fun findWithFilters(
        @Param("userId") userId: Long?,
        @Param("storeId") storeId: Long?,
        @Param("productId") productId: Long?
    ): List<PriceEntry>

    @Query("SELECT pe FROM PriceEntry pe WHERE pe.user = :user ORDER BY pe.createdAt DESC")
    fun findByUserOrderByCreatedAtDesc(@Param("user") user: User): List<PriceEntry>

    @Query("SELECT pe FROM PriceEntry pe WHERE pe.store = :store ORDER BY pe.createdAt DESC")
    fun findByStoreOrderByCreatedAtDesc(@Param("store") store: Store): List<PriceEntry>

    @Query("SELECT pe FROM PriceEntry pe WHERE pe.product = :product ORDER BY pe.createdAt DESC")
    fun findByProductOrderByCreatedAtDesc(@Param("product") product: Product): List<PriceEntry>

    @Query("SELECT pe FROM PriceEntry pe WHERE pe.createdAt BETWEEN :startDate AND :endDate " +
            "ORDER BY pe.createdAt DESC")
    fun findByCreatedAtBetween(
        @Param("startDate") startDate: LocalDateTime,
        @Param("endDate") endDate: LocalDateTime
    ): List<PriceEntry>

    @Query("SELECT pe FROM PriceEntry pe WHERE pe.user.id IN :userIds AND pe.store.id IN :storeIds " +
            "ORDER BY pe.createdAt DESC")
    fun findByUserIdsAndStoreIds(
        @Param("userIds") userIds: List<Long>,
        @Param("storeIds") storeIds: List<Long>
    ): List<PriceEntry>
}