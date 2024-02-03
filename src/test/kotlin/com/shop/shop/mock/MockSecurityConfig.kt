package com.shop.shop.mock

import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
class MockSecurityConfig {
    @Bean
    fun filterChain(http: HttpSecurity):SecurityFilterChain{
        http.csrf {
            it.disable()
        }
        return http.build()
    }
}