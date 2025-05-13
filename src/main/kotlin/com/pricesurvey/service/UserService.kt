package com.pricesurvey.service

import com.pricesurvey.dto.user.*
import com.pricesurvey.entity.User
import com.pricesurvey.entity.UserRole
import com.pricesurvey.exception.ResourceNotFoundException
import com.pricesurvey.repository.UserRepository
import com.pricesurvey.repository.StoreRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.format.DateTimeFormatter

@Service
class UserService(
    private val userRepository: UserRepository,
    private val storeRepository: StoreRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun createUser(request: UserRequest): UserResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("User with this email already exists")
        }

        val stores = request.assignedStoreIds.map { storeId ->
            storeRepository.findById(storeId)
                .orElseThrow { ResourceNotFoundException("Store not found with id: $storeId") }
        }.toMutableSet()

        val user = User(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            firstName = request.firstName,
            lastName = request.lastName,
            role = UserRole.valueOf(request.role.uppercase()),
            assignedStores = stores
        )

        val savedUser = userRepository.save(user)
        return convertToResponse(savedUser)
    }

    fun getAllUsers(): List<UserResponse> {
        return userRepository.findAll().map { convertToResponse(it) }
    }

    fun getUserById(id: Long): UserResponse {
        val user = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User not found with id: $id") }
        return convertToResponse(user)
    }

    @Transactional
    fun updateUser(id: Long, request: UpdateUserRequest): UserResponse {
        val user = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User not found with id: $id") }

        val updatedUser = user.copy(
            firstName = request.firstName ?: user.firstName,
            lastName = request.lastName ?: user.lastName,
            password = request.password?.let { passwordEncoder.encode(it) } ?: user.password,
            isActive = request.isActive ?: user.isActive,
            assignedStores = request.assignedStoreIds?.let { storeIds ->
                storeIds.map { storeId ->
                    storeRepository.findById(storeId)
                        .orElseThrow { ResourceNotFoundException("Store not found with id: $storeId") }
                }.toMutableSet()
            } ?: user.assignedStores
        )

        val savedUser = userRepository.save(updatedUser)
        return convertToResponse(savedUser)
    }

    @Transactional
    fun deleteUser(id: Long) {
        val user = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User not found with id: $id") }

        // Soft delete by deactivating the user
        val deactivatedUser = user.copy(isActive = false)
        userRepository.save(deactivatedUser)
    }

    private fun convertToResponse(user: User): UserResponse {
        return UserResponse(
            id = user.id!!,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            role = user.role.name,
            isActive = user.isActive,
            assignedStores = user.assignedStores.map { store ->
                UserResponse.StoreInfo(
                    id = store.id!!,
                    name = store.name,
                    address = store.address
                )
            },
            createdAt = user.createdAt?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) ?: "",
            updatedAt = user.updatedAt?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
    }
}