package com.shop.shop.security.auth.filter

import com.auth0.jwt.exceptions.JWTDecodeException
import com.fasterxml.jackson.databind.ObjectMapper
import com.shop.shop.member.repository.MemberRepository
import com.shop.shop.security.auth.PrincipalDetails
import com.shop.shop.security.jwt.JwtUtil
import com.shop.shop.token.domain.AccessToken
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.server.ResponseStatusException


//토큰인증시(일반+소셜)토큰확인필터,토큰확인후 세션에 강제로그인
//유저가 로그인은 이미 했고, 그 이후로 요청이 들어올때마다 작동하는 필터
class JwtAuthorizationFilter(authManager: AuthenticationManager,
                             private val memberRepository: MemberRepository):BasicAuthenticationFilter(authManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        try {
            val jwtHeader = request.getHeader("Authorization")
            if (jwtHeader==null || !jwtHeader.startsWith("Bearer")){ //토큰없거나 이상하면 리턴

                throw JWTDecodeException("")

            }

            val accessToken = AccessToken(jwtHeader)
            var username=accessToken.getTokenUsername()
            val memberEntity = memberRepository.findByUsername(username)?:
                throw JWTDecodeException("")

            val principalDetails = PrincipalDetails(memberEntity)
            val auth:Authentication=UsernamePasswordAuthenticationToken( //토큰으로 강제로그인
                principalDetails,
                null,
                principalDetails.authorities
            )
            SecurityContextHolder.getContext().authentication=auth//세션에 강제로 박아넣기
        } catch (e:Exception){
            request.setAttribute("exception",e)
        }

        chain.doFilter(request, response)


    }
}