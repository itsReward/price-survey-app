package com.pricesurvey.controller

import com.pricesurvey.dto.store.*
import com.pricesurvey.service.StoreService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/stores")
class StoreController(
    private val storeService: StoreService
) {

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createStore(@Valid @RequestBody request: StoreRequest): ResponseEntity<StoreResponse> {
        val response = storeService.createStore(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping
    fun getAllStores(): ResponseEntity<List<StoreResponse>> {
        val response = storeService.getActiveStores()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun getStoreById(@PathVariable id: Long): ResponseEntity<StoreResponse> {
        val response = storeService.getStoreById(id)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/city/{city}")
    fun getStoresByCity(@PathVariable city: String): ResponseEntity<List<StoreResponse>> {
        val response = storeService.getStoresByCity(city)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/region/{region}")
    fun getStoresByRegion(@PathVariable region: String): ResponseEntity<List<StoreResponse>> {
        val response = storeService.getStoresByRegion(region)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/map")
    fun getStoresForMap(): ResponseEntity<List<StoreService.MapStoreResponse>> {
        val response = storeService.getStoresForMap()
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateStore(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateStoreRequest
    ): ResponseEntity<StoreResponse> {
        val response = storeService.updateStore(id, request)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteStore(@PathVariable id: Long): ResponseEntity<Void> {
        storeService.deleteStore(id)
        return ResponseEntity.noContent().build()
    }
}