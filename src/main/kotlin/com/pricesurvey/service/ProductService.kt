package com.pricesurvey.service

import com.pricesurvey.dto.product.*
import com.pricesurvey.entity.Product
import com.pricesurvey.exception.ResourceNotFoundException
import com.pricesurvey.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.format.DateTimeFormatter

@Service
class ProductService(
    private val productRepository: ProductRepository
) {

    @Transactional
    fun createProduct(request: ProductRequest): ProductResponse {
        val product = Product(
            name = request.name,
            description = request.description,
            category = request.category,
            volumeMl = request.volumeMl,
            brand = request.brand
        )

        val savedProduct = productRepository.save(product)
        return convertToResponse(savedProduct)
    }

    fun getAllProducts(): List<ProductResponse> {
        return productRepository.findAll().map { convertToResponse(it) }
    }

    fun getActiveProducts(): List<ProductResponse> {
        return productRepository.findByIsActive(true).map { convertToResponse(it) }
    }

    fun getProductById(id: Long): ProductResponse {
        val product = productRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Product not found with id: $id") }
        return convertToResponse(product)
    }

    fun getProductsByCategory(category: String): List<ProductResponse> {
        return productRepository.findActiveByCategoryOrderByVolumeMl(category)
            .map { convertToResponse(it) }
    }

    fun getProductsByBrand(brand: String): List<ProductResponse> {
        return productRepository.findByBrandAndIsActive(brand, true)
            .map { convertToResponse(it) }
    }

    fun getCategories(): List<String> {
        return productRepository.findDistinctCategories()
    }

    fun getAvailableVolumes(): List<Int> {
        return productRepository.findDistinctVolumes()
    }

    @Transactional
    fun updateProduct(id: Long, request: UpdateProductRequest): ProductResponse {
        val product = productRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Product not found with id: $id") }

        val updatedProduct = product.copy(
            name = request.name ?: product.name,
            description = request.description ?: product.description,
            category = request.category ?: product.category,
            volumeMl = request.volumeMl ?: product.volumeMl,
            brand = request.brand ?: product.brand,
            isActive = request.isActive ?: product.isActive
        )

        val savedProduct = productRepository.save(updatedProduct)
        return convertToResponse(savedProduct)
    }

    @Transactional
    fun deleteProduct(id: Long) {
        val product = productRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Product not found with id: $id") }

        // Soft delete by deactivating the product
        val deactivatedProduct = product.copy(isActive = false)
        productRepository.save(deactivatedProduct)
    }

    private fun convertToResponse(product: Product): ProductResponse {
        return ProductResponse(
            id = product.id!!,
            name = product.name,
            description = product.description,
            category = product.category,
            volumeMl = product.volumeMl,
            brand = product.brand,
            isActive = product.isActive,
            createdAt = product.createdAt?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) ?: "",
            updatedAt = product.updatedAt?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
    }
}