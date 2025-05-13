package com.pricesurvey.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "stores")
@EntityListeners(AuditingEntityListener::class)
data class Store(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val address: String,

    @Column(nullable = false)
    val city: String,

    @Column(nullable = false)
    val region: String,

    @Column(nullable = false)
    val country: String,

    @Column(precision = 10, scale = 8)
    val latitude: BigDecimal? = null,

    @Column(precision = 11, scale = 8)
    val longitude: BigDecimal? = null,

    @Column(nullable = false)
    val isActive: Boolean = true,

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime? = null,

    @LastModifiedDate
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime? = null
)