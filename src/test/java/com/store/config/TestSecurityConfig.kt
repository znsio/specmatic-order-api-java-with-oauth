package com.store.config

import com.store.filters.BearerTokenCheckFilter
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter

@TestConfiguration
@Profile("test")
open class TestSecurityConfig {
    @Bean
    open fun testFilterChain(http: HttpSecurity): SecurityFilterChain? {
        http
            .authorizeRequests(Customizer { auth ->
                auth
                    .antMatchers(HttpMethod.POST, "/**").authenticated()
                    .antMatchers(HttpMethod.DELETE, "/**").authenticated()
                    .anyRequest()
                    .permitAll()
            })
            .addFilterBefore(BearerTokenCheckFilter(), AbstractPreAuthenticatedProcessingFilter::class.java)
            .csrf().disable()

        return http.build()
    }
}