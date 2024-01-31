package com.shop.shop.member.controller

import com.shop.shop.common.ApiHeader
import com.shop.shop.common.Result
import com.shop.shop.member.domain.Member
import com.shop.shop.member.repository.MemberRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class JoinController(val memberRepository: MemberRepository,
                     val pwEncoder: BCryptPasswordEncoder) {
    @PostMapping("/join")
    fun join(@RequestBody member:Member):Result<ApiHeader>{
        if(memberRepository.findByUsername(member.username)!=null){
            return Result(ApiHeader(400,"중복아이디"))
        }
        member.role="ROLE_USER"
        val rawPassword=member.password
        val encPassword=pwEncoder.encode(rawPassword)
        member.password=encPassword
        memberRepository.save(member)

        return Result(ApiHeader())
    }
}