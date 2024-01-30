package com.shop.shop.security.oauth

import com.shop.shop.member.repository.MemberRepository
import com.shop.shop.security.auth.PrincipalDetails
import com.shop.shop.security.jwt.JwtUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class Oauth2AuthenticationSuccessHandler(val memberRepository: MemberRepository):SimpleUrlAuthenticationSuccessHandler() {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        if (authentication==null || response ==null) {
           throw IllegalArgumentException()
        }
        val principalDetails = authentication.principal as PrincipalDetails
        val memberEntity = principalDetails.member

        val accessToken = JwtUtil().generateAccessToken(memberEntity)
        val refreshToken = JwtUtil().generateRefreshToken(memberEntity)

        memberEntity.refreshTokenId=refreshToken.getTokenTokenId()
        memberRepository.save(memberEntity)

        println("성공로직동작")
        println("액세스토큰 $accessToken")
        println("리프레시토큰 $refreshToken")


        response.addHeader(HttpHeaders.AUTHORIZATION,"Bearer $accessToken")
        response.addHeader("Refresh-Token","Bearer $refreshToken")
    }
}