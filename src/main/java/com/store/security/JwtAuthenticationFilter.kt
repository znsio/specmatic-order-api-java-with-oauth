package com.store.security

import com.nimbusds.jose.proc.SecurityContext
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.proc.JWTProcessor
import com.store.config.JwtAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

open class JwtAuthenticationFilter(
    private val jwtProcessor: JWTProcessor<SecurityContext>
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val token = request.getHeader("Authorization")
        val jwtClaims = jwtProcessor.process(token.replace("Bearer ", ""), null)

        // Convert jwtClaims into an Authentication object and set it in SecurityContextHolder
        // ...
        val authorities = extractAuthorities(jwtClaims)

        val authentication = JwtAuthenticationToken(jwtClaims.claims, authorities)
        SecurityContextHolder.getContext().authentication = authentication

        filterChain.doFilter(request, response)
    }

    private fun extractAuthorities(jwtClaims: JWTClaimsSet): Collection<GrantedAuthority> {
        return ((jwtClaims.claims.get("realm_access") as com.nimbusds.jose.shaded.json.JSONObject).get("roles") as List<String>).map { SimpleGrantedAuthority(it) }
    }
}
