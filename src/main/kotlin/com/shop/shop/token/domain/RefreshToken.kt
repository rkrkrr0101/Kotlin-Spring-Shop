package com.shop.shop.token.domain

class RefreshToken(token:String):Token(token) {
    fun getTokenTokenId():String{
        return getTokenClaim("tokenId")
    }
}