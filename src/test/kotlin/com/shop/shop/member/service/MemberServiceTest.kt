package com.shop.shop.member.service

import com.shop.shop.member.domain.Member
import com.shop.shop.member.repository.MemberRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class MemberServiceTest @Autowired constructor( val memberRepository: MemberRepository,
                        val memberService: MemberService, val pwEncoder: BCryptPasswordEncoder
                       ){

    @Test
    fun 멤버가_있으면_isUser는_true를_반환한다(){
        val member = Member("username111", "password111", "name111", "qqq@aqw.com")
        memberRepository.save(member)

        val memberIsUser = memberService.isUser(member)

        Assertions.assertThat(memberIsUser).isTrue()
    }
    @Test
    fun 멤버가_없으면_isUser는_false를_반환한다(){
        val member = Member("username111", "password111", "name111", "qqq@aqw.com")

        val memberIsUser = memberService.isUser(member)

        Assertions.assertThat(memberIsUser).isFalse()
    }

    @Test
    fun 멤버를_저장할때_비밀번호가_암호화된다(){
        val member = Member("username111", "password111", "name111", "qqq@aqw.com")
        memberService.memberSave(member)

        val findMember = memberRepository.findByUsername("username111")!!

        Assertions.assertThat(findMember.password).isNotEqualTo("password111")
    }



}