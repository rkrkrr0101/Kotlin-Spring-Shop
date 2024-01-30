package com.shop.shop.token.domain

class AccessToken(token:String):Token(token) {
    fun getTokenUsername():String{
        return getTokenClaim("username")
    }
}