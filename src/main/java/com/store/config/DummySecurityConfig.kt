package com.store.config

import com.store.security.BearerTokenCheckFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter

@Configuration
@Profile("test")
open class DummySecurityConfig {
    @Bean
    open fun dummyFilterChain(http: HttpSecurity): SecurityFilterChain? {
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