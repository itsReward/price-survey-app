package com.pricesurvey.security

import com.pricesurvey.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByEmailAndIsActive(username, true)
            .orElseThrow { UsernameNotFoundException("User not found: $username") }

        return CustomUserDetails(user)
    }
}