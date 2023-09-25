package com.store.config

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.jwk.source.RemoteJWKSet
import com.nimbusds.jose.proc.JWSAlgorithmFamilyJWSKeySelector
import com.nimbusds.jose.proc.SecurityContext
import com.nimbusds.jwt.proc.DefaultJWTProcessor
import com.store.security.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer
import org.springframework.security.config.core.GrantedAuthorityDefaults
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy
import java.net.URL


@Configuration
@Profile("prod")
@EnableWebSecurity
open class SecurityConfig(keycloakLogoutHandler: KeycloakLogoutHandler) {
    private val keycloakLogoutHandler: KeycloakLogoutHandler

//    @Autowired
//    private lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter

    init {
        this.keycloakLogoutHandler = keycloakLogoutHandler
    }

    @Bean
    open fun sessionAuthenticationStrategy(): SessionAuthenticationStrategy {
        return RegisterSessionAuthenticationStrategy(SessionRegistryImpl())
    }

    @Bean
    @Throws(Exception::class)
    open fun resourceServerFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/*")
            .permitAll()
            .and()
            .addFilterBefore(jwtAuthenticationFilter(), OAuth2LoginAuthenticationFilter::class.java)

        http.oauth2ResourceServer { obj: OAuth2ResourceServerConfigurer<HttpSecurity?> -> obj.jwt() }

        return http.build()
    }

    @Bean
    open fun grantedAuthorityDefaults() : GrantedAuthorityDefaults {
        return GrantedAuthorityDefaults("");
    }

//    @Bean
//    @Throws(Exception::class)
//    open fun authenticationManager(http: HttpSecurity): AuthenticationManager {
//        return http.getSharedObject(AuthenticationManagerBuilder::class.java)
//            .build()
//    }

    @Bean
    open fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        val jwtProcessor = DefaultJWTProcessor<SecurityContext>()
        // Point to the JWKS endpoint of the token issuer (e.g., Keycloak)
        val keySource: JWKSource<SecurityContext> = RemoteJWKSet(URL("http://localhost:8083/realms/specmatic/protocol/openid-connect/certs"))

        // Configure the JWT processor with the key selector to process the expected signature algorithm
        val keySelector = JWSAlgorithmFamilyJWSKeySelector(JWSAlgorithm.Family.RSA, keySource)
        jwtProcessor.jwsKeySelector = keySelector
        return JwtAuthenticationFilter(jwtProcessor)
    }
}