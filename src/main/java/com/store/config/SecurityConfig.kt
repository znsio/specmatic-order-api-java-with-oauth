package com.store.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer
import org.springframework.security.web.SecurityFilterChain

@Configuration
@Profile("prod")
open class SecurityConfig {
    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        http
            .authorizeRequests(Customizer { auth ->
                auth
                    .antMatchers(HttpMethod.POST, "/**").hasAuthority("SCOPE_write")
                    .antMatchers(HttpMethod.DELETE, "/**").hasAuthority("SCOPE_delete")
                    .anyRequest()
                    .permitAll()
            })
            .oauth2ResourceServer { oauth2: OAuth2ResourceServerConfigurer<HttpSecurity?> -> oauth2.jwt() }
        return http.build()
    }
}