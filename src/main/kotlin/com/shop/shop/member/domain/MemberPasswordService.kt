package com.shop.shop.member.domain

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class MemberPasswordService {
    private val pwEncoder= BCryptPasswordEncoder()
    fun cryptPassword(member: Member):String{
        return pwEncoder.encode(member.password)
    }
}