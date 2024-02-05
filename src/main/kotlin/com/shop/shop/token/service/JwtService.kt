package com.shop.shop.token.service


import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.shop.shop.member.domain.Member
import com.shop.shop.member.repository.MemberRepository
import com.shop.shop.security.jwt.JwtUtil
import com.shop.shop.token.domain.RefreshToken
import com.shop.shop.token.dto.TokenResponseDto
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class JwtService(val memberRepository: MemberRepository) {
    @Transactional
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
    @Transactional
    fun createToken(member: Member): TokenResponseDto{
        val findMember = memberRepository.findByUsername(member.username)
            ?:throw JWTDecodeException("")

        val newAccessToken = JwtUtil().generateAccessToken(findMember)
        val newRefreshToken = JwtUtil().generateRefreshToken(findMember)
        findMember.refreshTokenId = newRefreshToken.getTokenTokenId()

        return TokenResponseDto(newAccessToken.tokenCode, newRefreshToken.tokenCode)
    }
}