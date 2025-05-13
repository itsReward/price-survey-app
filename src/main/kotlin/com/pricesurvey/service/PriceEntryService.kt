package com.pricesurvey.service

import com.pricesurvey.dto.price.*
import com.pricesurvey.entity.PriceEntry
import com.pricesurvey.entity.User
import com.pricesurvey.exception.ResourceNotFoundException
import com.pricesurvey.repository.PriceEntryRepository
import com.pricesurvey.repository.StoreRepository
import com.pricesurvey.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class PriceEntryService(
    private val priceEntryRepository: PriceEntryRepository,
    private val storeRepository: StoreRepository,
    private val productRepository: ProductRepository,
    private val authService: AuthService
) {

    @Transactional
    fun createPriceEntry(request: PriceEntryRequest): PriceEntryResponse {
        val user = authService.getCurrentUser()

        val store = storeRepository.findById(request.storeId)
            .orElseThrow { ResourceNotFoundException("Store not found with id: ${request.storeId}") }

        val product = productRepository.findById(request.productId)
            .orElseThrow { ResourceNotFoundException("Product not found with id: ${request.productId}") }

        // Check if user has access to this store (if user is not admin)
        if (user.role != com.pricesurvey.entity.UserRole.ADMIN &&
            !user.assignedStores.any { it.id == store.id }) {
            throw IllegalArgumentException("User does not have access to this store")
        }

        val priceEntry = PriceEntry(
            user = user,
            store = store,
            product = product,
            price = request.price,
            quantity = request.quantity,
            notes = request.notes
        )

        val savedEntry = priceEntryRepository.save(priceEntry)
        return convertToResponse(savedEntry)
    }

    fun getAllPriceEntries(): List<PriceEntryResponse> {
        return priceEntryRepository.findAll().map { convertToResponse(it) }
    }

    fun getPriceEntriesWithFilters(filters: PriceEntryFilters): List<PriceEntryResponse> {
        return priceEntryRepository.findWithFilters(
            filters.userId,
            filters.storeId,
            filters.productId
        ).map { convertToResponse(it) }
    }

    fun getPriceEntriesByUser(user: User): List<PriceEntryResponse> {
        return priceEntryRepository.findByUserOrderByCreatedAtDesc(user)
            .map { convertToResponse(it) }
    }

    fun getPriceEntriesByStore(storeId: Long): List<PriceEntryResponse> {
        val store = storeRepository.findById(storeId)
            .orElseThrow { ResourceNotFoundException("Store not found with id: $storeId") }

        return priceEntryRepository.findByStoreOrderByCreatedAtDesc(store)
            .map { convertToResponse(it) }
    }

    fun getPriceEntriesByProduct(productId: Long): List<PriceEntryResponse> {
        val product = productRepository.findById(productId)
            .orElseThrow { ResourceNotFoundException("Product not found with id: $productId") }

        return priceEntryRepository.findByProductOrderByCreatedAtDesc(product)
            .map { convertToResponse(it) }
    }

    fun getPriceEntriesForUser(userId: Long): List<PriceEntryResponse> {
        val user = authService.getCurrentUser()

        return if (user.role == com.pricesurvey.entity.UserRole.ADMIN) {
            // Admin can see all entries
            priceEntryRepository.findAll().map { convertToResponse(it) }
        } else {
            // Regular users can only see their own entries and entries for their assigned stores
            val userStoreIds = user.assignedStores.map { it.id!! }
            priceEntryRepository.findByUserIdsAndStoreIds(listOf(user.id!!), userStoreIds)
                .map { convertToResponse(it) }
        }
    }

    private fun convertToResponse(priceEntry: PriceEntry): PriceEntryResponse {
        return PriceEntryResponse(
            id = priceEntry.id!!,
            user = PriceEntryResponse.UserInfo(
                id = priceEntry.user.id!!,
                email = priceEntry.user.email,
                firstName = priceEntry.user.firstName,
                lastName = priceEntry.user.lastName
            ),
            store = PriceEntryResponse.StoreInfo(
                id = priceEntry.store.id!!,
                name = priceEntry.store.name,
                address = priceEntry.store.address,
                city = priceEntry.store.city
            ),
            product = PriceEntryResponse.ProductInfo(
                id = priceEntry.product.id!!,
                name = priceEntry.product.name,
                category = priceEntry.product.category,
                volumeMl = priceEntry.product.volumeMl,
                brand = priceEntry.product.brand
            ),
            price = priceEntry.price,
            quantity = priceEntry.quantity,
            notes = priceEntry.notes,
            createdAt = priceEntry.createdAt?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) ?: "",
            updatedAt = priceEntry.updatedAt?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
    }
}