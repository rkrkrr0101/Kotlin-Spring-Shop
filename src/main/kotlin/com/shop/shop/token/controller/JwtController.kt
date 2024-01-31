package com.shop.shop.token.controller


import com.auth0.jwt.exceptions.JWTDecodeException
import com.shop.shop.member.repository.MemberRepository
import com.shop.shop.security.jwt.JwtUtil
import com.shop.shop.token.domain.RefreshToken
import com.shop.shop.token.dto.TokenResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/auth/jwt")
class JwtController(val memberRepository: MemberRepository) {
    @PostMapping("/refresh")
    fun accessTokenReissue(@RequestBody requestMap: Map<String,String>):ResponseEntity<TokenResponseDto>{
        //토큰받아서 username까고 그거로 검색해서 하는거
        //그냥 서비스로빼던가 도메인에 넣자
        val refreshToken = RefreshToken( requestMap["requestToken"]
                            ?: return ResponseEntity<TokenResponseDto>(HttpStatus.BAD_REQUEST))


        try {
            if (!refreshToken.verifyToken()){
                println("3")
                //responseEntity로 에러보내게(메시지담아서)수정
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED,"만료된 토큰")
            }
            val tokenId = refreshToken.getTokenTokenId()
            val username=refreshToken.getTokenUsername()


            val member = memberRepository.findByUsername(username)
                ?:throw UsernameNotFoundException("not found member")
            if(member.refreshTokenId!=tokenId){
                println("2")
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED,"잘못된 토큰")
            }
            val newAccessToken = JwtUtil().generateAccessToken(member)
            val newRefreshToken = JwtUtil().generateRefreshToken(member)
            member.refreshTokenId=newRefreshToken.getTokenTokenId()
            memberRepository.save(member)


            return ResponseEntity.ok(TokenResponseDto(newAccessToken.token,newRefreshToken.token))
        }catch (e:Exception){
            println("1")
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED,e.message,e)
        }


        //리프레시토큰을 깔수있는지(검증)(401),리프레시토큰의 기간이 안지났는지(401),
        // 리프레시토큰 유저아이디가 있는지,리프레시토큰 유저아이디에 리프레시토큰이 현재값이랑 같은지

    }
}