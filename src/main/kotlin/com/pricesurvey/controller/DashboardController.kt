package com.pricesurvey.controller

import DashboardFilters
import com.pricesurvey.dto.dashboard.*
import com.pricesurvey.service.DashboardService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/dashboard")
class DashboardController(
    private val dashboardService: DashboardService
) {

    @GetMapping
    fun getDashboardData(
        @RequestParam(required = false) storeId: Long?,
        @RequestParam(required = false) productId: Long?,
        @RequestParam(required = false) userId: Long?,
        @RequestParam(required = false) startDate: String?,
        @RequestParam(required = false) endDate: String?
    ): ResponseEntity<DashboardData> {
        val filters = DashboardFilters(
            storeId = storeId,
            productId = productId,
            userId = userId,
            startDate = startDate,
            endDate = endDate
        )
        val response = dashboardService.getDashboardData(filters)
        return ResponseEntity.ok(response)
    }
}