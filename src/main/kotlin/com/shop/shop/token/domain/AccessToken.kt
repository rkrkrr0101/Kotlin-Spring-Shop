package com.shop.shop.token.domain

class AccessToken(tokenCode:String):Token(tokenCode) {
    fun getTokenUsername():String{
        return getTokenClaim("username")
    }
}