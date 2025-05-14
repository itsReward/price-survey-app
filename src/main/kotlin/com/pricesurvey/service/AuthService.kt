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
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import org.springframework.beans.factory.annotation.Value
import java.util.Collections

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenUtil: JwtTokenUtil,
    private val userLoginActivityRepository: UserLoginActivityRepository
) {

    @Value("\${google.client-id}")
    private lateinit var googleClientId: String

    fun login(loginRequest: LoginRequest, request: HttpServletRequest): LoginResponse {
        val authentication = authenticateUser(loginRequest.email, loginRequest.password)

        val userDetails = authentication.principal as UserDetails
        val user = userRepository.findByEmailAndIsActive(userDetails.username, true)
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

    fun register(registerRequest: RegisterRequest, request: HttpServletRequest): LoginResponse {
        // Check if user already exists
        if (userRepository.existsByEmail(registerRequest.email)) {
            throw IllegalArgumentException("User with this email already exists")
        }

        // Create new user (inactive by default, admin needs to activate)
        val user = User(
            email = registerRequest.email,
            password = passwordEncoder.encode(registerRequest.password),
            firstName = registerRequest.firstName,
            lastName = registerRequest.lastName,
            role = UserRole.USER,
            isActive = false // Admin needs to activate user
        )

        val savedUser = userRepository.save(user)

        // Don't create session for inactive user, just return confirmation
        return LoginResponse(
            token = "",
            user = UserInfo(
                id = savedUser.id!!,
                email = savedUser.email,
                firstName = savedUser.firstName,
                lastName = savedUser.lastName,
                role = savedUser.role.name
            )
        )
    }

    fun googleAuth(googleAuthRequest: GoogleAuthRequest, request: HttpServletRequest): LoginResponse {
        // Verify Google token
        val verifier = GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
            .setAudience(Collections.singletonList(googleClientId))
            .build()

        val idToken: GoogleIdToken = verifier.verify(googleAuthRequest.token)
            ?: throw IllegalArgumentException("Invalid Google token")

        val payload = idToken.payload
        val email = payload.email
        val emailVerified = payload.emailVerified

        if (!emailVerified) {
            throw IllegalArgumentException("Google email not verified")
        }

        // Check if user exists
        val existingUser = userRepository.findByEmail(email)

        val user = if (existingUser.isPresent) {
            // Update existing user with Google info if needed
            val user = existingUser.get()
            if (!user.isActive) {
                throw IllegalArgumentException("Account is not activated. Please contact admin.")
            }
            user
        } else {
            // Create new user with Google info (inactive by default)
            val newUser = User(
                email = email,
                password = passwordEncoder.encode("GOOGLE_AUTH_" + System.currentTimeMillis()), // Dummy password
                firstName = googleAuthRequest.firstName,
                lastName = googleAuthRequest.lastName,
                role = UserRole.USER,
                isActive = false // Admin needs to activate
            )
            userRepository.save(newUser)
        }

        if (!user.isActive) {
            throw IllegalArgumentException("Account is not activated. Please contact admin.")
        }

        // Generate token
        val userDetails = org.springframework.security.core.userdetails.User(
            user.email,
            user.password,
            emptyList()
        )
        val token = jwtTokenUtil.generateToken(userDetails)

        // Record login activity
        val loginActivity = UserLoginActivity(
            user = user,
            ipAddress = getClientIpAddress(request),
            userAgent = request.getHeader("User-Agent") + " (Google OAuth)"
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
        return userRepository.findByEmailAndIsActive(userDetails.username, true)
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