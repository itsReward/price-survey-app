package com.pricesurvey.controller

import com.pricesurvey.dto.user.*
import com.pricesurvey.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
class UserController(
    private val userService: UserService
) {

    @PostMapping
    fun createUser(@Valid @RequestBody request: UserRequest): ResponseEntity<UserResponse> {
        val response = userService.createUser(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserResponse>> {
        val response = userService.getAllUsers()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<UserResponse> {
        val response = userService.getUserById(id)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateUserRequest
    ): ResponseEntity<UserResponse> {
        val response = userService.updateUser(id, request)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}/status")
    fun updateUserStatus(
        @PathVariable id: Long,
        @RequestBody request: UserStatusRequest
    ): ResponseEntity<UserResponse> {
        val response = userService.updateUserStatus(id, request.isActive)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}/stores")
    fun assignStoresToUser(
        @PathVariable id: Long,
        @RequestBody request: AssignStoresRequest
    ): ResponseEntity<UserResponse> {
        val response = userService.assignStoresToUser(id, request.storeIds)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        userService.deleteUser(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/pending")
    fun getPendingUsers(): ResponseEntity<List<UserResponse>> {
        val response = userService.getPendingUsers()
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}/approve")
    fun approveUser(@PathVariable id: Long): ResponseEntity<UserResponse> {
        val response = userService.approveUser(id)
        return ResponseEntity.ok(response)
    }
}