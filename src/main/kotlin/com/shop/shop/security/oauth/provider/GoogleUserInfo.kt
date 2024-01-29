package com.cos.Security1.config.oauth.provider

class GoogleUserInfo(val attributes: Map<String,Any>):OAuth2UserInfo {
    override fun getProviderId():String {
        return attributes["sub"] as String
    }

    override fun getProvider():String {
        return "google"
    }

    override fun getEmail():String {
        return attributes["email"] as String
    }

    override fun getName():String {
        return attributes["name"] as String
    }
}