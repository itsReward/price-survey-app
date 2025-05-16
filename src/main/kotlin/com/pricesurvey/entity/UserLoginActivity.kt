package com.pricesurvey.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "user_login_activities")
@EntityListeners(AuditingEntityListener::class)
data class UserLoginActivity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(name = "ip_address", length = 45) // Add length constraint
    val ipAddress: String? = null,

    @Column(name = "user_agent", columnDefinition = "TEXT")
    val userAgent: String? = null,

    @CreatedDate
    @Column(name = "logged_in_at", nullable = false, updatable = false)
    val loggedInAt: LocalDateTime? = LocalDateTime.now()
)