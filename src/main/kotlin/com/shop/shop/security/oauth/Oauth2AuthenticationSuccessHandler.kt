package com.shop.shop.security.oauth

import com.shop.shop.security.auth.PrincipalDetails
import com.shop.shop.security.jwt.JwtUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class Oauth2AuthenticationSuccessHandler:SimpleUrlAuthenticationSuccessHandler() {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        if (authentication==null || response ==null) {
           throw IllegalArgumentException()
        }
        val principalDetails = authentication.principal as PrincipalDetails

        val accessToken = JwtUtil().generateAccessToken(principalDetails.member)
        val refreshToken = JwtUtil().generateRefreshToken(principalDetails.member)

        response.addHeader(HttpHeaders.AUTHORIZATION,"Bearer $accessToken")
        response.addHeader("Refresh-Token","Bearer $refreshToken")
    }
}