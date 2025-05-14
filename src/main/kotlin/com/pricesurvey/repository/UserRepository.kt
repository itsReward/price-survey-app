package com.pricesurvey.repository

import com.pricesurvey.entity.User
import com.pricesurvey.entity.UserRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>
    fun findByEmailAndIsActive(email: String, isActive: Boolean): Optional<User>
    fun findByRole(role: UserRole): List<User>
    fun findByIsActive(isActive: Boolean): List<User>
    fun existsByEmail(email: String): Boolean

    @Query("SELECT u FROM User u WHERE u.role = :role AND u.isActive = true")
    fun findActiveUsersByRole(@Param("role") role: UserRole): List<User>

    @Query("SELECT u FROM User u WHERE u.isActive = false ORDER BY u.createdAt DESC")
    fun findPendingUsersOrderByCreatedAtDesc(): List<User>

    @Query("SELECT u FROM User u JOIN u.assignedStores s WHERE s.id = :storeId")
    fun findUsersByAssignedStore(@Param("storeId") storeId: Long): List<User>
}