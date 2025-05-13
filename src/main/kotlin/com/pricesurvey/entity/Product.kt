package com.pricesurvey.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener::class)
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val name: String,

    @Column
    val description: String? = null,

    @Column(nullable = false)
    val category: String,

    @Column(nullable = false)
    val volumeMl: Int, // Volume in milliliters

    @Column(nullable = false)
    val brand: String? = null,

    @Column(nullable = false)
    val isActive: Boolean = true,

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime? = null,

    @LastModifiedDate
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime? = null
)