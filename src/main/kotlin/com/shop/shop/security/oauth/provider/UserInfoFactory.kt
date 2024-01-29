package com.shop.shop.security.oauth.provider

import com.cos.Security1.config.oauth.provider.GoogleUserInfo
import com.cos.Security1.config.oauth.provider.NaverUserInfo
import com.cos.Security1.config.oauth.provider.OAuth2UserInfo

class UserInfoFactory {
    companion object{
        fun createUserInfo(registrationId:String,attributes:Map<String,Any>):OAuth2UserInfo{
            when (registrationId){
                "google"-> return GoogleUserInfo(attributes)
                "naver"-> {
                    @Suppress("UNCHECKED_CAST")
                    return NaverUserInfo(attributes["response"] as Map<String, Any>)
                }
                else-> throw IllegalArgumentException()
            }

        }
    }

}