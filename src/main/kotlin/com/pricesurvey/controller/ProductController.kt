package com.pricesurvey.controller

import com.pricesurvey.dto.product.*
import com.pricesurvey.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productService: ProductService
) {

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createProduct(@Valid @RequestBody request: ProductRequest): ResponseEntity<ProductResponse> {
        val response = productService.createProduct(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping
    fun getAllProducts(): ResponseEntity<List<ProductResponse>> {
        val response = productService.getActiveProducts()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long): ResponseEntity<ProductResponse> {
        val response = productService.getProductById(id)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/category/{category}")
    fun getProductsByCategory(@PathVariable category: String): ResponseEntity<List<ProductResponse>> {
        val response = productService.getProductsByCategory(category)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/brand/{brand}")
    fun getProductsByBrand(@PathVariable brand: String): ResponseEntity<List<ProductResponse>> {
        val response = productService.getProductsByBrand(brand)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/categories")
    fun getCategories(): ResponseEntity<List<String>> {
        val response = productService.getCategories()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/volumes")
    fun getAvailableVolumes(): ResponseEntity<List<Int>> {
        val response = productService.getAvailableVolumes()
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateProduct(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateProductRequest
    ): ResponseEntity<ProductResponse> {
        val response = productService.updateProduct(id, request)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Void> {
        productService.deleteProduct(id)
        return ResponseEntity.noContent().build()
    }
}