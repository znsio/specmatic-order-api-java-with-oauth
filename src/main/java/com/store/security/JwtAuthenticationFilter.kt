package com.store.security

import com.store.model.User
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

class JwtAuthenticationFilter : OncePerRequestFilter() {

    companion object {
        private val logger = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication is JwtAuthenticationToken) {
            logger.info("Request received: ${request.method} ${request.requestURI} with a jwt token with scopes: " + authentication.authorities.joinToString(", "))
            SecurityContextHolder.getContext().authentication =
                PreAuthenticatedAuthenticationToken(User("authenticated_user"), null, authentication.authorities)
        }
        filterChain.doFilter(request, response)
    }
}