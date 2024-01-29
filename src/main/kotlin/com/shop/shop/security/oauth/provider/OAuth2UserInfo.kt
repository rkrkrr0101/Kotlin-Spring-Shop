package com.cos.Security1.config.oauth.provider

interface OAuth2UserInfo {
    fun getProviderId():String
    fun getProvider():String
    fun getEmail():String
    fun getName():String
}