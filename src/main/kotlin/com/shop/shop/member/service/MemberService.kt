package com.shop.shop.member.service

import com.shop.shop.member.domain.Member
import com.shop.shop.member.repository.MemberRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class MemberService(val memberRepository: MemberRepository,val pwEncoder: BCryptPasswordEncoder) {

    fun isUser(member: Member):Boolean{
        val findMember = memberRepository.findByUsername(member.username)
        return findMember!=null
    }

    fun memberSave(member: Member){
        member.role="ROLE_USER"
        val rawPassword=member.password
        val encPassword=pwEncoder.encode(rawPassword)
        member.password=encPassword
        memberRepository.save(member)
    }
}