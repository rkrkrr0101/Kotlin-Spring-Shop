package com.shop.shop.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.shop.shop.constants.Constants
import com.shop.shop.member.domain.Member
import com.shop.shop.token.domain.AccessToken
import com.shop.shop.token.domain.RefreshToken
import java.util.*

class JwtUtil {
    private val secretKey=Constants.PRIVATE_KEY
    private val accessTokenExpire=Constants.ACCESS_TOKEN_EXPIRE
    private val refreshTokenExpire=Constants.REFRESH_TOKEN_EXPIRE

    fun generateAccessToken(member: Member):AccessToken{
        return AccessToken("""Bearer ${JWT.create()
            .withSubject("AccessToken")
            .withExpiresAt(Date(System.currentTimeMillis()+accessTokenExpire))
            .withClaim("username",member.username)
            .withClaim("role",member.role)
            .sign(Algorithm.HMAC512(secretKey))}""")
    }
    fun generateRefreshToken(member: Member):RefreshToken{
        return RefreshToken( """Bearer ${JWT.create()
            .withSubject("RefreshToken")
            .withExpiresAt(Date(System.currentTimeMillis()+refreshTokenExpire))
            .withClaim("username",member.username)
            .withClaim("tokenId",UUID.randomUUID().toString())
            .sign(Algorithm.HMAC512(secretKey))}""")
    }

}