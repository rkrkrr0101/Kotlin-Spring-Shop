package com.shop.shop.security.oauth


import com.cos.Security1.config.oauth.provider.OAuth2UserInfo
import com.shop.shop.member.domain.Member
import com.shop.shop.member.repository.MemberRepository
import com.shop.shop.security.auth.PrincipalDetails
import com.shop.shop.security.oauth.provider.UserInfoFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class PrincipalOauth2UserService(val pwEncoder: BCryptPasswordEncoder,
    val memberRepository: MemberRepository):DefaultOAuth2UserService() {
    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
        if (userRequest==null){
            throw IllegalArgumentException("OAuth2UserRequest null")
        }
        val oAuth2User=super.loadUser(userRequest)
        val oAuth2UserInfo:OAuth2UserInfo=UserInfoFactory.createUserInfo(
            userRequest.clientRegistration.registrationId,
            oAuth2User.attributes)//프로바이더별 유저인포생성

        val provider = oAuth2UserInfo.getProvider()
        val providerId = oAuth2UserInfo.getProviderId()
        val username="""${provider}_${providerId}"""
        val password=pwEncoder.encode("Oauth2Password")
        val email = oAuth2UserInfo.getEmail()
        val role = "ROLE_USER"

        var memberEntity=memberRepository.findByUsername(username)
        if (memberEntity==null){
            memberEntity= Member(username,password,username,email,role,provider,providerId)
            memberRepository.save(memberEntity)
        }

        return PrincipalDetails(memberEntity,oAuth2User.attributes)

    }
}