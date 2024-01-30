package com.shop.shop.token.domain

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.shop.shop.constants.Constants

open class Token(val token:String) {

    fun removeBearer():String{
        return token.replace("Bearer ", "")
    }
    fun getTokenClaim(claimKey: String): String {
        val jwtToken = removeBearer()
        return JWT.require(Algorithm.HMAC512(Constants.PRIVATE_KEY))//토큰만들때 넣은 시크릿값
            .build().verify(jwtToken).getClaim(claimKey)
            .asString()
    }

}