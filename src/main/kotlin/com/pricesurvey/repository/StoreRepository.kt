package com.pricesurvey.repository

import com.pricesurvey.entity.Store
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface StoreRepository : JpaRepository<Store, Long> {
    override fun findAll(): List<Store>
    
    fun findByIsActive(isActive: Boolean): List<Store>

    @Query("SELECT s FROM Store s WHERE s.city LIKE %:city% AND s.isActive = true")
    fun findActiveByCityContaining(@Param("city") city: String): List<Store>

    @Query("SELECT s FROM Store s WHERE s.region = :region AND s.isActive = true")
    fun findActiveByRegion(@Param("region") region: String): List<Store>

    fun existsByNameAndAddress(name: String, address: String): Boolean
}