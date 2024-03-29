package com.store.config

import com.store.security.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter
import org.springframework.security.web.SecurityFilterChain

@Configuration
@Profile("prod")
open class SecurityConfig {
    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        http
            .authorizeRequests { auth ->
                verifyAuthority(auth.anyRequest())
            }
        configureFilterChain(http)
        return http.build()
    }

    protected open fun configureFilterChain(http: HttpSecurity) {
        http.addFilterAfter(JwtAuthenticationFilter(), BearerTokenAuthenticationFilter::class.java)
        http.oauth2ResourceServer { obj: OAuth2ResourceServerConfigurer<HttpSecurity?> -> obj.jwt() }
    }

    protected open fun verifyAuthority(authorizedUrl: ExpressionUrlAuthorizationConfigurer<HttpSecurity>.AuthorizedUrl) {
        authorizedUrl.hasAuthority("SCOPE_email")
    }
}