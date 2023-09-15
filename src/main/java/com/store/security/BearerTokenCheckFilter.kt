package com.store.security

import com.store.model.User
import org.springframework.http.HttpMethod
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class BearerTokenCheckFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.method in listOf(HttpMethod.POST.name, HttpMethod.DELETE.name)) {
            val authHeader = request.getHeader("Authorization")
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                SecurityContextHolder.getContext().authentication = PreAuthenticatedAuthenticationToken(
                    User("Dummy User"),
                    null,
                    emptyList()
                ).apply { isAuthenticated = true }
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Bearer token missing")
                return
            }
        }
        filterChain.doFilter(request, response)
    }
}