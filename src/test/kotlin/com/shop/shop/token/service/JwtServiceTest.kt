package com.shop.shop.token.service

import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.shop.shop.member.domain.Member
import com.shop.shop.member.repository.MemberRepository
import com.shop.shop.token.domain.RefreshToken
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@Transactional
class JwtServiceTest @Autowired constructor(val memberRepository: MemberRepository,
                                            val jwtService: JwtService){

    @Test
    fun 토큰을_받아서_정상적으로_토큰을_재발급할수_있다(){
        val member = Member("username111", "password111", "name111", "qqq@aqw.com")
        memberRepository.save(member)
        val firstTokenDto = jwtService.createToken(member)

        val createToken = jwtService.createToken(RefreshToken(firstTokenDto.refreshTokenCode))

        Assertions.assertThat(createToken.accessTokenCode.substring(0..6)).isEqualTo("Bearer ")
        Assertions.assertThat(createToken.refreshTokenCode.substring(0..6)).isEqualTo("Bearer ")

    }
    @Test
    fun 토큰의_멤버가_DB에_존재하지않으면_예외가_발생한다(){
        val member = Member("username111", "password111", "name111", "qqq@aqw.com")
        memberRepository.save(member)
        val firstTokenDto = jwtService.createToken(member)
        memberRepository.delete(member)

        assertThrows<JWTDecodeException> {
            jwtService.createToken(RefreshToken(firstTokenDto.refreshTokenCode))
        }


    }
    @Test
    fun 토큰의_멤버의_DB에_토큰ID가_다르면_예외가_발생한다(){
        val member = Member("username111", "password111", "name111", "qqq@aqw.com")
        memberRepository.save(member)
        val firstTokenDto = jwtService.createToken(member)
        val findMember = memberRepository.findByUsername(member.username)!!
        findMember.refreshTokenId=""
        memberRepository.save(findMember)

        assertThrows<TokenExpiredException> {
            jwtService.createToken(RefreshToken(firstTokenDto.refreshTokenCode))
        }
    }
    @Test
    fun 멤버를_받아서_정상적으로_토큰을_발급할수_있다(){
        val member = Member("username111", "password111", "name111", "qqq@aqw.com")
        memberRepository.save(member)

        val createToken = jwtService.createToken(member)

        Assertions.assertThat(createToken.accessTokenCode.substring(0..6)).isEqualTo("Bearer ")
        Assertions.assertThat(createToken.refreshTokenCode.substring(0..6)).isEqualTo("Bearer ")
    }

}