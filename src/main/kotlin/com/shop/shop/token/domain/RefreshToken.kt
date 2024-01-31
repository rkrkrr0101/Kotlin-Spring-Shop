package com.shop.shop.token.domain

class RefreshToken(tokenCode:String):Token(tokenCode) {
    fun getTokenTokenId():String{
        return getTokenClaim("tokenId")
    }
    fun getTokenUsername():String{
        return getTokenClaim("username")
    }
}