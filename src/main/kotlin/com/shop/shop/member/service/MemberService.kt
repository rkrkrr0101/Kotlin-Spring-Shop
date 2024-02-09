package com.shop.shop.member.service

import com.shop.shop.member.domain.Member
import com.shop.shop.member.domain.MemberPasswordService
import com.shop.shop.member.dto.MemberCreateDto
import com.shop.shop.member.dto.MemberRoleUpdateDto
import com.shop.shop.member.repository.MemberRepository
import jakarta.transaction.Transactional
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class MemberService(val memberRepository: MemberRepository,val pwEncoder: BCryptPasswordEncoder) {

    fun isUser(dto: MemberCreateDto):Boolean{
        val findMember = memberRepository.findByUsername(dto.username)
        return findMember!=null
    }


    @Transactional
    fun memberSave(dto: MemberCreateDto){
        val member = Member(dto.username,dto.password,dto.name,dto.email)
        member.role="ROLE_USER"
        member.password=MemberPasswordService().cryptPassword(member)
        memberRepository.save(member)
    }
    @Transactional
    fun memberRoleUpdate(member: Member){
        val findMember = memberRepository.findById(member.id).orElseThrow{throw IllegalArgumentException()}

        findMember.role ="ROLE_MANAGER"
    }
}