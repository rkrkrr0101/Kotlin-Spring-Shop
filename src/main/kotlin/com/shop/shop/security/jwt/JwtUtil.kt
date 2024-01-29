package com.shop.shop.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.shop.shop.constants.Constants
import com.shop.shop.member.domain.Member
import java.util.*

class JwtUtil {
    private val secretKey=Constants.PRIVATE_KEY
    private val accessTokenExpire=Constants.ACCESS_TOKEN_EXPIRE
    private val refreshTokenExpire=Constants.REFRESH_TOKEN_EXPIRE

    fun generateAccessToken(member: Member):String{
        return JWT.create()
            .withSubject("AccessToken")
            .withExpiresAt(Date(System.currentTimeMillis()+accessTokenExpire))
            .withClaim("id",member.id)
            .withClaim("role",member.role)
            .sign(Algorithm.HMAC512(secretKey))
    }
    fun generateRefreshToken(member: Member):String{
        return JWT.create()
            .withSubject("RefreshToken")
            .withExpiresAt(Date(System.currentTimeMillis()+refreshTokenExpire))
            .withClaim("id",member.id)
            .withClaim("tokenId",UUID.randomUUID().toString())
            .sign(Algorithm.HMAC512(secretKey))
    }
    fun getTokenUsername(token:String):String{
        return getTokenClaim(token,"username")
    }
    fun getTokenClaim(token: String, claimKey: String): String {
        val jwtToken = token.replace("Bearer ", "")
        return JWT.require(Algorithm.HMAC512(secretKey))//토큰만들때 넣은 시크릿값
            .build().verify(jwtToken).getClaim(claimKey)
            .asString()
    }
}