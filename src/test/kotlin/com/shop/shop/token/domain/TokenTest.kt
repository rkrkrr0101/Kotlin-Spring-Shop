package com.shop.shop.token.domain

import com.shop.shop.member.domain.Member
import com.shop.shop.security.jwt.JwtUtil
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class TokenTest{

    @Test
    fun 일반회원가입_멤버로_리프레쉬토큰을_생성할수_있다(){
        //g
        val member = Member("username111", "password111", "name111", "qqq@aqw.com")
        //w
        val refreshToken = JwtUtil().generateRefreshToken(member)

        //t
        Assertions.assertThat(refreshToken.getTokenUsername())
            .isEqualTo("username111")
    }
    @Test
    fun 일반회원가입_멤버로_액세스토큰을_생성할수_있다(){
        //g
        val member = Member("username111", "password111", "name111", "qqq@aqw.com")
        //w
        val accessToken = JwtUtil().generateAccessToken(member)

        //t
        Assertions.assertThat(accessToken.getTokenUsername())
            .isEqualTo("username111")
    }
    @Test
    fun 소셜회원가입_멤버로_리프레쉬토큰을_생성할수_있다(){
        //g
        val oauth2Member = Member("google_rwrw123",
            "password111",
            "name111",
            "qqq@aqw.com")
        oauth2Member.provider="google"
        oauth2Member.providerId="rwrw123"
        //w
        val oauth2RefreshToken = JwtUtil().generateRefreshToken(oauth2Member)
        //t
        Assertions.assertThat(oauth2RefreshToken.getTokenUsername())
            .isEqualTo("google_rwrw123")
    }
    @Test
    fun 소셜회원가입_멤버로_액세스토큰을_생성할수_있다(){
        //g
        val oauth2Member = Member("google_rwrw123",
            "password111",
            "name111",
            "qqq@aqw.com")
        oauth2Member.provider="google"
        oauth2Member.providerId="rwrw123"
        //w
        val oauth2AccessToken = JwtUtil().generateAccessToken(oauth2Member)
        //t
        Assertions.assertThat(oauth2AccessToken.getTokenUsername())
            .isEqualTo("google_rwrw123")
    }
    @Test
    fun 액세스토큰에서_유저네임을_해독할수있다(){
        //g
        val member = Member("username111", "password111", "name111", "qqq@aqw.com")
        //w
        val accessToken = JwtUtil().generateAccessToken(member)

        //t
        Assertions.assertThat(accessToken.getTokenUsername())
            .isEqualTo("username111")
    }

}