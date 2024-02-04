package com.shop.shop.member.service

import com.shop.shop.member.domain.Member
import com.shop.shop.member.dto.MemberCreateDto
import com.shop.shop.member.repository.MemberRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
@Transactional
class MemberServiceTest @Autowired constructor(private val memberRepository: MemberRepository){
    private final val pwEncoder=BCryptPasswordEncoder()
    val memberService: MemberService=MemberService(memberRepository,pwEncoder)

    @Test
    fun 멤버가_있으면_isUser는_true를_반환한다(){
        val member = Member("username111", "password111", "name111", "qqq@aqw.com")
        memberRepository.save(member)
        val dto = MemberCreateDto(member.username, member.password, member.name, member.email)

        val memberIsUser = memberService.isUser(dto)

        Assertions.assertThat(memberIsUser).isTrue()
    }
    @Test
    fun 멤버가_없으면_isUser는_false를_반환한다(){
        val member = Member("username111", "password111", "name111", "qqq@aqw.com")
        val dto = MemberCreateDto(member.username, member.password, member.name, member.email)

        val memberIsUser = memberService.isUser(dto)

        Assertions.assertThat(memberIsUser).isFalse()
    }

    @Test
    fun 멤버를_저장할때_비밀번호가_암호화된다(){
        val dto = MemberCreateDto("username111", "password111", "name111", "qqq@aqw.com")
        memberService.memberSave(dto)


        val findMember = memberRepository.findByUsername("username111")!!

        Assertions.assertThat(findMember.password).isNotEqualTo(dto.password)
    }



}