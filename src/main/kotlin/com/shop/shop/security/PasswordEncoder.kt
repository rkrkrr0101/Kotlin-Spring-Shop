package com.shop.shop.security

import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class PasswordEncoder {
    @Bean
    fun encodePwd(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}