package com.pricesurvey.controller

import com.pricesurvey.dto.auth.*
import com.pricesurvey.service.AuthService
import com.pricesurvey.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import jakarta.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val userService: UserService
) {

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody loginRequest: LoginRequest,
        request: HttpServletRequest
    ): ResponseEntity<LoginResponse> {
        val response = authService.login(loginRequest, request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/register")
    fun register(
        @Valid @RequestBody registerRequest: RegisterRequest,
        request: HttpServletRequest
    ): ResponseEntity<LoginResponse> {
        val response = authService.register(registerRequest, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PostMapping("/google")
    fun googleAuth(
        @Valid @RequestBody googleAuthRequest: GoogleAuthRequest,
        request: HttpServletRequest
    ): ResponseEntity<LoginResponse> {
        val response = authService.googleAuth(googleAuthRequest, request)
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
            "isActive" to user.isActive,
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

    @PostMapping("/logout")
    fun logout(): ResponseEntity<Map<String, String>> {
        // For stateless JWT, logout is handled client-side
        // You could maintain a blacklist of tokens if needed
        return ResponseEntity.ok(mapOf("message" to "Logout successful"))
    }
}