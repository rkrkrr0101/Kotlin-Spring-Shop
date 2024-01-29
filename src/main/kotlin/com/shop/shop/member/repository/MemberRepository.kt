package com.shop.shop.member.repository

import com.shop.shop.member.domain.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository:JpaRepository<Member,Long> {
    fun findByUsername(username:String):Member?
}