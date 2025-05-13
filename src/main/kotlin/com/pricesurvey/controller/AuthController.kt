package com.pricesurvey.controller

import com.pricesurvey.dto.auth.LoginRequest
import com.pricesurvey.dto.auth.LoginResponse
import com.pricesurvey.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody loginRequest: LoginRequest,
        request: HttpServletRequest
    ): ResponseEntity<LoginResponse> {
        val response = authService.login(loginRequest, request)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/me")
    fun getCurrentUser(): ResponseEntity<Map<String, Any>> {
        val user = authService.getCurrentUser()
        val response = mapOf(
            "id" to user.id!!,
            "email" to user.email,
            "firstName" to user.firstName,
            "lastName" to user.lastName,
            "role" to user.role.name,
            "assignedStores" to user.assignedStores.map { store ->
                mapOf(
                    "id" to store.id!!,
                    "name" to store.name,
                    "address" to store.address
                )
            }
        )
        return ResponseEntity.ok(response)
    }
}