package com.shop.shop.token.domain

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.shop.shop.constants.Constants
import java.util.Date

open class Token(val tokenCode:String) {

    fun removeBearer():String{
        return tokenCode.replace("Bearer ", "")
    }
    fun getTokenClaim(claimKey: String): String {
        val jwtToken = removeBearer()
        return JWT.require(Algorithm.HMAC512(Constants.PRIVATE_KEY))//토큰만들때 넣은 시크릿값
            .build().verify(jwtToken).getClaim(claimKey) //만료체크도 자동
            .asString()
    }
    fun getTokenExp():Date{
        val jwtToken=removeBearer()
        return JWT.decode(jwtToken).expiresAt//getTokenClaim("exp")
    }
    fun verifyToken():Boolean{
        val exp = getTokenExp()
        return  exp.after(Date())
    }

    override fun toString(): String {
        return tokenCode
    }

}