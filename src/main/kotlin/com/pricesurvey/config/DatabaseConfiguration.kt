package com.pricesurvey.config

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import javax.sql.DataSource
import org.springframework.boot.jdbc.DataSourceBuilder
import java.net.URI
import java.nio.charset.StandardCharsets
import java.net.URLDecoder

@Configuration
@Profile("prod")
class DatabaseConfiguration {

    @Bean
    @Primary
    fun dataSource(): DataSource {
        val databaseUrl = System.getenv("DATABASE_URL")
            ?: throw IllegalStateException("DATABASE_URL environment variable is not set")

        // Parse the DATABASE_URL regardless of format
        val uri = URI.create(databaseUrl)

        val hostname = uri.host
        val port = if (uri.port != -1) uri.port else 5432
        val database = uri.path.substring(1) // Remove leading slash

        // Parse username and password from userInfo
        val userInfo = uri.userInfo?.split(":")
        val username = userInfo?.get(0) ?: "pricesurvey"
        val password = if (userInfo != null && userInfo.size > 1) {
            URLDecoder.decode(userInfo[1], StandardCharsets.UTF_8)
        } else {
            throw IllegalStateException("Password not found in DATABASE_URL")
        }

        // Construct proper JDBC URL
        val jdbcUrl = "jdbc:postgresql://$hostname:$port/$database"

        return DataSourceBuilder.create()
            .url(jdbcUrl)
            .username(username)
            .password(password)
            .driverClassName("org.postgresql.Driver")
            .build()
    }
}