package com.shop.shop.token.controller


import com.auth0.jwt.exceptions.JWTDecodeException
import com.shop.shop.common.ApiBody
import com.shop.shop.common.ApiMessage
import com.shop.shop.common.Result
import com.shop.shop.member.repository.MemberRepository
import com.shop.shop.token.service.JwtService
import com.shop.shop.token.domain.RefreshToken
import com.shop.shop.token.dto.TokenResponseDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/jwt")
class JwtController(val memberRepository: MemberRepository,
    val jwtService: JwtService
) {
    @PostMapping("/refresh")
    fun accessTokenReissue(@RequestBody requestMap: Map<String,String>):Result<ApiMessage<TokenResponseDto>>{
        val refreshToken = RefreshToken( requestMap["requestToken"]
                            ?: throw JWTDecodeException(""))

        return Result(ApiMessage(ApiBody(jwtService.createToken(refreshToken))))
    }
}