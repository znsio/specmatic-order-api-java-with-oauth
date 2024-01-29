package com.store.config

import com.store.security.DummySecurityFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter

@Configuration
@Profile("test")
open class DummySecurityConfig {
    @Bean
    open fun dummyFilterChain(http: HttpSecurity): SecurityFilterChain? {
        http
            .authorizeRequests { auth ->
                auth.anyRequest().permitAll()
            }
            .addFilterBefore(DummySecurityFilter(), AbstractPreAuthenticatedProcessingFilter::class.java)
            .csrf().disable()

        return http.build()
    }
}