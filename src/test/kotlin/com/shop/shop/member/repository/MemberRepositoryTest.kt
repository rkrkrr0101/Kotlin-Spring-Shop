package com.shop.shop.member.repository

import com.shop.shop.member.domain.Member
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class MemberRepositoryTest(@Autowired val memberRepository: MemberRepository) {

    @BeforeEach
    fun init(){

    }

    @Test
    fun 멤버레포지토리에_멤버를_추가하고_검색할수있다(){
        val member = Member("username111", "password111", "name111", "qqq@aqw.com")

        memberRepository.save(member)
        val findMember = memberRepository.findByUsername("username111")
        Assertions.assertThat(member).isEqualTo(findMember)
    }
    @Test
    fun 멤버레포지토리에_멤버가_없으면_null을_반환한다(){
        val findMember = memberRepository.findByUsername("username111")
        Assertions.assertThat(findMember).isEqualTo(null)
    }
}