package com.shop.shop.security.auth.filter

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.shop.shop.constants.Constants
import com.shop.shop.member.repository.MemberRepository
import com.shop.shop.security.auth.PrincipalDetails
import com.shop.shop.security.jwt.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

//토큰인증시(일반+소셜)토큰확인필터,토큰확인후 세션에 강제로그인
//유저가 로그인은 이미 했고, 그 이후로 요청이 들어올때마다 작동하는 필터
class JwtAuthorizationFilter(authManager: AuthenticationManager,
                             private val memberRepository: MemberRepository):BasicAuthenticationFilter(authManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        println("인증서버동작")
        val jwtHeader = request.getHeader("Authorization")
        if (jwtHeader==null || !jwtHeader.startsWith("Bearer")){ //토큰없거나 이상하면 리턴
            chain.doFilter(request, response)
            return
        }
        val username=JwtUtil().getTokenUsername(jwtHeader)
        println(username)
        val memberEntity = memberRepository.findByUsername(username)?:
            throw UsernameNotFoundException("잘못된 토큰")
        val principalDetails = PrincipalDetails(memberEntity)
        val auth:Authentication=UsernamePasswordAuthenticationToken( //토큰으로 강제로그인
            principalDetails,
            null,
            principalDetails.authorities
        )
        SecurityContextHolder.getContext().authentication=auth//세션에 강제로 박아넣기

        chain.doFilter(request, response)


    }
}