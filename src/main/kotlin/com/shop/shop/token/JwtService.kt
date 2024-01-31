package com.shop.shop.token


import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.shop.shop.member.domain.Member
import com.shop.shop.member.repository.MemberRepository
import com.shop.shop.security.jwt.JwtUtil
import com.shop.shop.token.domain.RefreshToken
import com.shop.shop.token.dto.TokenResponseDto
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class JwtService(val memberRepository: MemberRepository) {
    fun createToken(refreshToken: RefreshToken):TokenResponseDto{
        val tokenId = refreshToken.getTokenTokenId()
        val username=refreshToken.getTokenUsername()

        val member = memberRepository.findByUsername(username)
            ?:throw JWTDecodeException("")
        if(member.refreshTokenId!=tokenId){//단일토큰 체크
            throw TokenExpiredException("", Instant.now())
        }

        return createToken(member)
    }

    fun createToken(member: Member): TokenResponseDto{
        val newAccessToken = JwtUtil().generateAccessToken(member)
        val newRefreshToken = JwtUtil().generateRefreshToken(member)
        member.refreshTokenId = newRefreshToken.getTokenTokenId()
        memberRepository.save(member)
        return TokenResponseDto(newAccessToken.tokenCode, newRefreshToken.tokenCode)
    }
}