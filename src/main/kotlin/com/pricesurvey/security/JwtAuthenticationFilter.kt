package com.pricesurvey.security

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class JwtAuthenticationFilter(
    private val customUserDetailsService: CustomUserDetailsService,
    private val jwtTokenUtil: JwtTokenUtil
) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val requestHeader = request.getHeader("Authorization")

        var username: String? = null
        var jwtToken: String? = null

        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            jwtToken = requestHeader.substring(7)
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken)
            } catch (e: IllegalArgumentException) {
                logger.error("Unable to get JWT Token")
            } catch (e: ExpiredJwtException) {
                logger.error("JWT Token has expired")
            } catch (e: MalformedJwtException) {
                logger.error("JWT Token is invalid")
            }
        } else {
            logger.debug("JWT Token does not begin with Bearer String")
        }

        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = customUserDetailsService.loadUserByUsername(username)

            if (jwtTokenUtil.validateToken(jwtToken!!, userDetails)) {
                val authToken = UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.authorities
                )
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authToken
            }
        }
        chain.doFilter(request, response)
    }
}