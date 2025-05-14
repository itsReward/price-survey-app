package com.pricesurvey.controller

import com.pricesurvey.dto.price.*
import com.pricesurvey.service.PriceEntryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import org.slf4j.LoggerFactory

@RestController
@RequestMapping("/api/price-entries")
class PriceEntryController(
    private val priceEntryService: PriceEntryService
) {
    private val logger = LoggerFactory.getLogger(PriceEntryController::class.java)

    @PostMapping
    fun createPriceEntry(@Valid @RequestBody request: PriceEntryRequest): ResponseEntity<PriceEntryResponse> {
        logger.info("Creating price entry for $request")
        val response = priceEntryService.createPriceEntry(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping
    fun getPriceEntries(
        @RequestParam(required = false) userId: Long?,
        @RequestParam(required = false) storeId: Long?,
        @RequestParam(required = false) productId: Long?,
        @RequestParam(required = false) startDate: String?,
        @RequestParam(required = false) endDate: String?
    ): ResponseEntity<List<PriceEntryResponse>> {
        val filters = PriceEntryFilters(
            userId = userId,
            storeId = storeId,
            productId = productId,
            startDate = startDate,
            endDate = endDate
        )
        val response = priceEntryService.getPriceEntriesWithFilters(filters)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun deletePriceEntry(@PathVariable id: Long): ResponseEntity<Void> {
        logger.info("Deleting price entry with id: $id")
        priceEntryService.deletePriceEntry(id)
        return ResponseEntity.noContent().build()
    }


    @GetMapping("/store/{storeId}")
    fun getPriceEntriesByStore(@PathVariable storeId: Long): ResponseEntity<List<PriceEntryResponse>> {
        val response = priceEntryService.getPriceEntriesByStore(storeId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/product/{productId}")
    fun getPriceEntriesByProduct(@PathVariable productId: Long): ResponseEntity<List<PriceEntryResponse>> {
        val response = priceEntryService.getPriceEntriesByProduct(productId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/my-entries")
    fun getMyPriceEntries(): ResponseEntity<List<PriceEntryResponse>> {
        val response = priceEntryService.getPriceEntriesForUser(0L) // Current user
        return ResponseEntity.ok(response)
    }
}