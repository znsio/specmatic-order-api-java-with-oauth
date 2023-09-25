package com.store.config

import com.store.security.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter
import org.springframework.security.web.SecurityFilterChain


@Configuration
@Profile("dev")
open class SecurityConfig() {
    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain? {

        http
            .authorizeRequests(Customizer { auth ->
                auth
                    .anyRequest().hasAuthority("SCOPE_email")
            })
            .addFilterAfter(JwtAuthenticationFilter(), BearerTokenAuthenticationFilter::class.java)

        http.oauth2ResourceServer { obj: OAuth2ResourceServerConfigurer<HttpSecurity?> -> obj.jwt() }
        return http.build()
    }
}