package com.shop.shop.security.oauth

import com.shop.shop.member.repository.MemberRepository
import com.shop.shop.security.auth.PrincipalDetails
import com.shop.shop.security.jwt.JwtUtil
import com.shop.shop.token.JwtService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class Oauth2AuthenticationSuccessHandler(val memberRepository: MemberRepository,
                                         val jwtService: JwtService):SimpleUrlAuthenticationSuccessHandler() {
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

        val tokenResponseDto = jwtService.createToken(memberEntity)

        response.addHeader(HttpHeaders.AUTHORIZATION,tokenResponseDto.accessTokenCode)
        response.addHeader("Refresh-Token",tokenResponseDto.refreshTokenCode)
    }
}