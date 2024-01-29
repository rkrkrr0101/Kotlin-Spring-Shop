package com.shop.shop.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.shop.shop.constants.Constants
import java.util.*

class JwtUtil {
    private val secretKey=Constants.PRIVATE_KEY
    private val accessTokenExpire=Constants.ACCESS_TOKEN_EXPIRE
    private val refreshTokenExpire=Constants.REFRESH_TOKEN_EXPIRE

    fun generateAccessToken(id:Long,role:String):String{
        return JWT.create()
            .withSubject("AccessToken")
            .withExpiresAt(Date(System.currentTimeMillis()+accessTokenExpire))
            .withClaim("id",id)
            .withClaim("role",role)
            .sign(Algorithm.HMAC512(secretKey))
    }
    fun generateRefreshToken(id:Long):String{
        return JWT.create()
            .withSubject("RefreshToken")
            .withExpiresAt(Date(System.currentTimeMillis()+refreshTokenExpire))
            .withClaim("id",id)
            .withClaim("tokenId",UUID.randomUUID().toString())
            .sign(Algorithm.HMAC512(secretKey))
    }
}