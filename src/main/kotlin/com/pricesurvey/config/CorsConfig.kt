package com.pricesurvey.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfig {

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()

        // Allow your frontend origins
        configuration.allowedOriginPatterns = listOf(
            "*",  // For development - remove in production
            "https://*.onrender.com",  // For Render deployments
            "https://price-survey-frontend.onrender.com",  // Add your actual frontend URL
            "http://localhost:*",  // For local development
            "https://localhost:*"   // For HTTPS local development
        )

        // Allow specific methods
        configuration.allowedMethods = listOf(
            "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"
        )

        // Allow all headers
        configuration.allowedHeaders = listOf("*")

        // Allow credentials
        configuration.allowCredentials = true

        // Expose headers
        configuration.exposedHeaders = listOf(
            "Authorization",
            "Content-Type",
            "Content-Length",
            "X-Requested-With"
        )

        // Max age for preflight requests
        configuration.maxAge = 3600L

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}