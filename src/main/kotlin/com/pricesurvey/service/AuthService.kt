package com.pricesurvey.service

import com.pricesurvey.dto.auth.*
import com.pricesurvey.entity.User
import com.pricesurvey.entity.UserRole
import com.pricesurvey.entity.UserLoginActivity
import com.pricesurvey.repository.UserRepository
import com.pricesurvey.repository.UserLoginActivityRepository
import com.pricesurvey.security.JwtTokenUtil
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import jakarta.servlet.http.HttpServletRequest

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenUtil: JwtTokenUtil,
    private val userLoginActivityRepository: UserLoginActivityRepository
) {

    fun login(loginRequest: LoginRequest, request: HttpServletRequest): LoginResponse {
        val authentication = authenticateUser(loginRequest.email, loginRequest.password)

        val userDetails = authentication.principal as UserDetails
        val user = userRepository.findByEmail(userDetails.username)
            .orElseThrow { RuntimeException("User not found") }

        val token = jwtTokenUtil.generateToken(userDetails)

        // Record login activity
        val loginActivity = UserLoginActivity(
            user = user,
            ipAddress = getClientIpAddress(request),
            userAgent = request.getHeader("User-Agent")
        )
        userLoginActivityRepository.save(loginActivity)

        return LoginResponse(
            token = token,
            user = UserInfo(
                id = user.id!!,
                email = user.email,
                firstName = user.firstName,
                lastName = user.lastName,
                role = user.role.name
            )
        )
    }

    fun getCurrentUser(): User {
        val authentication = SecurityContextHolder.getContext().authentication
        val userDetails = authentication.principal as UserDetails
        return userRepository.findByEmail(userDetails.username)
            .orElseThrow { RuntimeException("User not found") }
    }

    private fun authenticateUser(email: String, password: String): Authentication {
        return authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(email, password)
        )
    }

    private fun getClientIpAddress(request: HttpServletRequest): String {
        val xForwardedFor = request.getHeader("X-Forwarded-For")
        return when {
            !xForwardedFor.isNullOrEmpty() -> xForwardedFor.split(',')[0].trim()
            !request.getHeader("X-Real-IP").isNullOrEmpty() -> request.getHeader("X-Real-IP")
            else -> request.remoteAddr
        }
    }
}