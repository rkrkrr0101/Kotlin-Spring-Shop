package com.shop.shop.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class CorsConfig {
    @Bean
    fun corsFilter():CorsFilter{
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials=true //내서버가 응답할떄 json을 js에서 처리할수있게 허용
        config.addAllowedOrigin("*") //모든 ip에 응답을 허용(와카)
        config.addAllowedHeader("*")//모든 header에 응답을 허용
        config.addAllowedMethod("*") //모든 post,get,put등에 요청을 허용
        source.registerCorsConfiguration("/api/**",config)
        return CorsFilter(source)
    }
}