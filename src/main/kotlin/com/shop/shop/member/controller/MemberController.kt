package com.shop.shop.member.controller

import com.shop.shop.common.ApiHeader
import com.shop.shop.common.Result
import com.shop.shop.member.domain.Member
import com.shop.shop.member.repository.MemberRepository
import com.shop.shop.member.service.MemberService
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(val memberRepository: MemberRepository,
                       val memberService: MemberService,
                       ) {
    @PostMapping("/join")
    fun join(@RequestBody member:Member):Result<ApiHeader>{
        if(memberService.isUser(member)){
            throw IllegalArgumentException()
        }
        memberService.memberSave(member)
        return Result(ApiHeader())
    }
    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun illegalArgument():Result<ApiHeader>{
        return Result(ApiHeader(400,"중복아이디"))
    }
}