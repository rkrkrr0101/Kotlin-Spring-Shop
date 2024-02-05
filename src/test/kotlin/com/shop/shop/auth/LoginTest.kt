package com.shop.shop.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.ObjectMapper
import com.shop.shop.constants.Constants
import com.shop.shop.member.domain.Member
import com.shop.shop.member.domain.MemberPasswordService
import com.shop.shop.member.repository.MemberRepository
import com.shop.shop.security.jwt.JwtUtil
import com.shop.shop.token.domain.AccessToken
import com.shop.shop.token.dto.TokenResponseDto
import com.shop.shop.token.service.JwtService
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.mock
import java.util.*

@SpringBootTest
//@WithMockUser(roles = ["USER"]) //@WithSecurityContext도 포함되어있음
@WithAnonymousUser
@AutoConfigureMockMvc
@Transactional
class LoginTest(@Autowired val mvc:MockMvc,@Autowired val memberRepository: MemberRepository) {
    val om=ObjectMapper()

    @Test
    fun 일반로그인을_성공하면_토큰이_발급된다(){
        val member = Member("username111", "password111", "name111", "qqq@aqw.com")
        member.password=MemberPasswordService().cryptPassword(member)
        memberRepository.save(member)
        val jsonMap = hashMapOf<String, String>()
        jsonMap["username"]="username111"
        jsonMap["password"]="password111"
        val contentJson = om.writeValueAsString(jsonMap)

        mvc.perform(
            post("/login")
            .contentType("application/json")
            .content(contentJson)
        )
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.header().exists("Authorization"))
            .andExpect(MockMvcResultMatchers.header().exists("Refresh-Token"))
           // .andDo(MockMvcResultHandlers.print())


    }

    @Test
    fun 일반로그인을_실패하면_401코드가_발생한다(){
        val jsonMap = hashMapOf<String, String>()
        jsonMap["username"]="username111"
        jsonMap["password"]="password111"
        val contentJson = om.writeValueAsString(jsonMap)

        mvc.perform(
            post("/login")
                .contentType("application/json")
                .content(contentJson)
        )
            .andExpect(status().isUnauthorized)
        // .andDo(MockMvcResultHandlers.print())

    }
    @Test
    fun 적절한_액세스토큰을_가지고_userurl에_접근하면_404가_발생한다(){
        val member = Member("username111", "password111", "name111", "qqq@aqw.com")
        memberRepository.save(member)
        val tokenDto = JwtService(memberRepository).createToken(member)
        mvc.perform(
            get("/user/nonexist")
                .header("Authorization",tokenDto.accessTokenCode)
        )
            .andExpect(status().isNotFound)
        // .andDo(MockMvcResultHandlers.print())
    }
    @Test
    fun 부적절한_액세스토큰을_가지고_userurl에_접근하면_401이_발생한다(){
        val member = Member("username111", "password111", "name111", "qqq@aqw.com")
        memberRepository.save(member)
        val tokenDto = JwtService(memberRepository).createToken(member)
        memberRepository.delete(member)

        mvc.perform(
            get("/user/nonexist")
                .header("Authorization",tokenDto.accessTokenCode)
        )
            .andExpect(status().isUnauthorized)
        // .andDo(MockMvcResultHandlers.print())
    }
    @Test
    fun 만료된_액세스토큰을_가지고_userurl에_접근하면_401이_발생한다(){
        val member = Member("username111", "password111", "name111", "qqq@aqw.com")
        memberRepository.save(member)
        val minusTime=-1000*60*10
        val accessTokenCode=AccessToken("""Bearer ${
            JWT.create()
                .withSubject("AccessToken")
                .withExpiresAt(Date(System.currentTimeMillis()+minusTime))
                .withClaim("username",member.username)
                .withClaim("role",member.role)
                .sign(Algorithm.HMAC512(Constants.PRIVATE_KEY))}""").tokenCode

        mvc.perform(
            get("/user/nonexist")
                .header("Authorization",accessTokenCode)
        )
            .andExpect(status().isUnauthorized)
        // .andDo(MockMvcResultHandlers.print())
    }

//    @Test
//    fun 처음으로_소셜로그인을_하면_회원가입이_되고_토큰이_발급된다(){}
//
//    @Test
//    fun 두번이상_같은계정으로_소셜로그인을_하면_회원가입이_되지않고_토큰만_발급된다(){}
//
//    @Test
//    fun 다른프로바이더로_소셜로그인을_하면_다른_프로바이더로_회원가입이_된다(){}

}