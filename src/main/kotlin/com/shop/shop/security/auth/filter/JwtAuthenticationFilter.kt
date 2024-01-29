package com.shop.shop.security.auth.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.shop.shop.member.domain.Member
import com.shop.shop.security.auth.PrincipalDetails
import com.shop.shop.security.jwt.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter



//일반로그인 필터
class JwtAuthenticationFilter(private val authManager: AuthenticationManager):UsernamePasswordAuthenticationFilter() {
    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        request?:throw IllegalArgumentException("JwtAuthenticationFilter request null")
        val om=ObjectMapper()
        val member = om.readValue(request.inputStream, Member::class.java)
        println(member)

        val authToken = UsernamePasswordAuthenticationToken(member.username, member.password)
        val auth = authManager.authenticate(authToken)

        return auth
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        if (response==null || authResult==null){
            throw IllegalArgumentException("잘못된입력")
        }
        val principalDetails = authResult.principal as PrincipalDetails

        val accessToken =
            JwtUtil().generateAccessToken(principalDetails.member)
        val refreshToken=JwtUtil().generateRefreshToken(principalDetails.member)

        response.addHeader(HttpHeaders.AUTHORIZATION,"Bearer $accessToken")
        response.addHeader("Refresh-Token","Bearer $refreshToken")
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        failed: AuthenticationException?
    ) {
        super.unsuccessfulAuthentication(request, response, failed)
        //실패시로직
    }
}