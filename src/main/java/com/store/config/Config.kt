package com.store.config

import com.nimbusds.jose.proc.SecurityContext
import com.nimbusds.jwt.proc.DefaultJWTProcessor
import com.nimbusds.jwt.proc.JWTProcessor
import com.store.security.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.client.RestTemplate

@Configuration
@Profile("prod")
open class Config {
//    @Bean
//    open fun keycloakConfigResolver(): KeycloakSpringBootConfigResolver {
//        return KeycloakSpringBootConfigResolver()
//    }

    @Bean
    open fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

//    @Bean
//    open fun jwtProcessor(): JWTProcessor<SecurityContext> {
//        val processor = DefaultJWTProcessor<SecurityContext>()
//        // Add necessary configuration to validate and decode JWTs
//        // (e.g., JWK set URL, expected issuer, accepted algorithms, etc.)
//        return processor
//    }
}