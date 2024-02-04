package com.shop.shop.member.controller

import com.shop.shop.common.ApiHeader
import com.shop.shop.common.Result
import com.shop.shop.member.dto.MemberCreateDto
import com.shop.shop.member.service.MemberService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(val memberService: MemberService) {
    @PostMapping("/join")
    fun join(@RequestBody memberCreateDto:MemberCreateDto):Result<ApiHeader>{
        if(memberService.isUser(memberCreateDto)){
            throw IllegalArgumentException()
        }
        memberService.memberSave(memberCreateDto)
        return Result(ApiHeader())
    }

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    //@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun illegalArgument():Result<ApiHeader>{
        return Result(ApiHeader(400,"중복아이디"))
    }
}