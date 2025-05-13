package com.pricesurvey.repository

import com.pricesurvey.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    fun findByIsActive(isActive: Boolean): List<Product>

    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.isActive = true")
    fun findActiveByCategoryOrderByVolumeMl(@Param("category") category: String): List<Product>

    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.isActive = true")
    fun findDistinctCategories(): List<String>

    @Query("SELECT DISTINCT p.volumeMl FROM Product p WHERE p.isActive = true ORDER BY p.volumeMl")
    fun findDistinctVolumes(): List<Int>

    fun findByBrandAndIsActive(brand: String, isActive: Boolean): List<Product>
}