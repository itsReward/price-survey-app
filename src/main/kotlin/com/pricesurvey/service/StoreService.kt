package com.pricesurvey.service

import com.pricesurvey.dto.store.*
import com.pricesurvey.entity.Store
import com.pricesurvey.exception.ResourceNotFoundException
import com.pricesurvey.repository.StoreRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.format.DateTimeFormatter

@Service
class StoreService(
    private val storeRepository: StoreRepository
) {

    @Transactional
    fun createStore(request: StoreRequest): StoreResponse {
        if (storeRepository.existsByNameAndAddress(request.name, request.address)) {
            throw IllegalArgumentException("Store with this name and address already exists")
        }

        val store = Store(
            name = request.name,
            address = request.address,
            city = request.city,
            region = request.region,
            country = request.country,
            latitude = request.latitude,
            longitude = request.longitude
        )

        val savedStore = storeRepository.save(store)
        return convertToResponse(savedStore)
    }

    fun getAllStores(): List<StoreResponse> {
        return storeRepository.findAll().map { convertToResponse(it) }
    }

    fun getActiveStores(): List<StoreResponse> {
        return storeRepository.findByIsActive(true).map { convertToResponse(it) }
    }

    fun getStoreById(id: Long): StoreResponse {
        val store = storeRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Store not found with id: $id") }
        return convertToResponse(store)
    }

    fun getStoresByCity(city: String): List<StoreResponse> {
        return storeRepository.findActiveByCityContaining(city).map { convertToResponse(it) }
    }

    fun getStoresByRegion(region: String): List<StoreResponse> {
        return storeRepository.findActiveByRegion(region).map { convertToResponse(it) }
    }

    @Transactional
    fun updateStore(id: Long, request: UpdateStoreRequest): StoreResponse {
        val store = storeRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Store not found with id: $id") }

        val updatedStore = store.copy(
            name = request.name ?: store.name,
            address = request.address ?: store.address,
            city = request.city ?: store.city,
            region = request.region ?: store.region,
            country = request.country ?: store.country,
            latitude = request.latitude ?: store.latitude,
            longitude = request.longitude ?: store.longitude,
            isActive = request.isActive ?: store.isActive
        )

        val savedStore = storeRepository.save(updatedStore)
        return convertToResponse(savedStore)
    }

    @Transactional
    fun deleteStore(id: Long) {
        val store = storeRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Store not found with id: $id") }

        // Soft delete by deactivating the store
        val deactivatedStore = store.copy(isActive = false)
        storeRepository.save(deactivatedStore)
    }

    fun getStoresForMap(): List<MapStoreResponse> {
        return storeRepository.findAll()
            .filter { it.latitude != null && it.longitude != null }
            .map { store ->
                MapStoreResponse(
                    id = store.id!!,
                    name = store.name,
                    address = store.address,
                    city = store.city,
                    latitude = store.latitude!!,
                    longitude = store.longitude!!,
                    isActive = store.isActive
                )
            }
    }

    private fun convertToResponse(store: Store): StoreResponse {
        return StoreResponse(
            id = store.id!!,
            name = store.name,
            address = store.address,
            city = store.city,
            region = store.region,
            country = store.country,
            latitude = store.latitude,
            longitude = store.longitude,
            isActive = store.isActive,
            createdAt = store.createdAt?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) ?: "",
            updatedAt = store.updatedAt?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
    }

    data class MapStoreResponse(
        val id: Long,
        val name: String,
        val address: String,
        val city: String,
        val latitude: java.math.BigDecimal,
        val longitude: java.math.BigDecimal,
        val isActive: Boolean
    )
}