package com.store.config

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter
import org.keycloak.adapters.springsecurity.management.HttpSessionManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy

@Configuration
@Profile("prod")
open class SecurityConfig : KeycloakWebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        super.configure(http)
        http.authorizeRequests()
//            .antMatchers("/your-secured-endpoint/*").hasRole("user") // Adjust roles as needed
            .anyRequest().permitAll()
    }

    @Autowired
    fun keycloakConfigResolver(): KeycloakSpringBootConfigResolver {
        return KeycloakSpringBootConfigResolver()
    }

    @Bean
    @ConditionalOnMissingBean(HttpSessionManager::class)
    override fun httpSessionManager(): HttpSessionManager {
        return HttpSessionManager()
    }

    @Bean
    override fun sessionAuthenticationStrategy(): SessionAuthenticationStrategy {
        return RegisterSessionAuthenticationStrategy(SessionRegistryImpl())
    }

}
