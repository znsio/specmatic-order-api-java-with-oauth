package com.store.config

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class JwtAuthenticationToken(
    private val jwtClaims: Map<String, Any>,
    authorities: Collection<GrantedAuthority>
) : AbstractAuthenticationToken(authorities) {

    override fun getCredentials(): Any? = null

    override fun getPrincipal(): Any = jwtClaims
}
