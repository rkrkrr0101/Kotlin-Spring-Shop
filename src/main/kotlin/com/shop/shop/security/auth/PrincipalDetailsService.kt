package com.shop.shop.security.auth

import com.shop.shop.member.repository.MemberRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class PrincipalDetailsService(val  memberRepository: MemberRepository):UserDetailsService{
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null){
            throw IllegalArgumentException("잘못된 username 입력")
        }
        val memberEntity=
            memberRepository.findByUsername(username)?:
                throw UsernameNotFoundException("존재하지 않는 username 입니다.")
        return PrincipalDetails(memberEntity)
    }
}