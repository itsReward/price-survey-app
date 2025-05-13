package com.pricesurvey.repository

import com.pricesurvey.entity.User
import com.pricesurvey.entity.UserLoginActivity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface UserLoginActivityRepository : JpaRepository<UserLoginActivity, Long> {

    @Query("SELECT ula FROM UserLoginActivity ula WHERE ula.user = :user " +
            "ORDER BY ula.loggedInAt DESC")
    fun findByUserOrderByLoggedInAtDesc(@Param("user") user: User): List<UserLoginActivity>

    @Query("SELECT ula FROM UserLoginActivity ula WHERE ula.loggedInAt >= :fromDate " +
            "ORDER BY ula.loggedInAt DESC")
    fun findFromDate(@Param("fromDate") fromDate: LocalDateTime): List<UserLoginActivity>

    @Query("SELECT COUNT(ula) FROM UserLoginActivity ula WHERE ula.user = :user " +
            "AND ula.loggedInAt >= :fromDate")
    fun countByUserAndLoggedInAtAfter(
        @Param("user") user: User,
        @Param("fromDate") fromDate: LocalDateTime
    ): Long
}