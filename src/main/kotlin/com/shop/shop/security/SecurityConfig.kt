package com.shop.shop.security

import com.shop.shop.member.repository.MemberRepository
import com.shop.shop.security.auth.filter.JwtAuthenticationFilter
import com.shop.shop.security.auth.filter.JwtAuthorizationFilter
import com.shop.shop.security.oauth.Oauth2AuthenticationFailureHandler
import com.shop.shop.security.oauth.Oauth2AuthenticationSuccessHandler
import com.shop.shop.security.oauth.PrincipalOauth2UserService
import com.shop.shop.token.service.JwtService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.filter.CorsFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
class SecurityConfig(val corsFilter: CorsFilter,
                     val principalOauth2UserService: PrincipalOauth2UserService,
                     val authConfiguration: AuthenticationConfiguration,
                     val memberRepository: MemberRepository,
                     val jwtService: JwtService,
                     val oauth2AuthenticationSuccessHandler: Oauth2AuthenticationSuccessHandler,
                     val oauth2AuthenticationFailureHandler: Oauth2AuthenticationFailureHandler,
                     val entryPoint: AuthenticationEntryPoint) {
    @Bean
    fun filterChain(http:HttpSecurity):SecurityFilterChain{
        http.formLogin {
            it.disable()
        }
        http.httpBasic {
            it.disable()
        }
        http.sessionManagement{
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        http.csrf(CsrfConfigurer<HttpSecurity>::disable)
        http.addFilter(corsFilter)
        //여기까지 restapi+jwt기본설정

        http.authorizeHttpRequests {
            it.requestMatchers("/user/**").authenticated()
            it.requestMatchers("/manager/**").hasAnyRole("MANAGER","ADMIN")
            it.requestMatchers("/admin/**").hasAnyRole("ADMIN")
            it.anyRequest().permitAll()
        }//url권한설정

        http.addFilter(JwtAuthenticationFilter(authenticationManager(authConfiguration),memberRepository,jwtService))
        http.addFilter(JwtAuthorizationFilter(authenticationManager(authConfiguration),memberRepository))
        http.exceptionHandling {it.authenticationEntryPoint(entryPoint)  }

        //일반로그인

        http.oauth2Login {
            //oauth2로그인시 정보가져올 엔드포인트설정
            it.userInfoEndpoint{
                it.userService(principalOauth2UserService) ////oauth2로그인시 처리할 서비스설정
            }
            it.successHandler(oauth2AuthenticationSuccessHandler)//성공핸들러
            it.failureHandler(oauth2AuthenticationFailureHandler)//실패핸들러
        }//oauth2설정








        return http.build()
    }
    @Bean
    fun authenticationManager(authConfiguration: AuthenticationConfiguration):AuthenticationManager{
        return authConfiguration.authenticationManager
    }
}