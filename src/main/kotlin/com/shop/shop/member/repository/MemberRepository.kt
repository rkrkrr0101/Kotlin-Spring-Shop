package com.shop.shop.member.repository

import com.shop.shop.member.domain.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository:JpaRepository<Member,Long> {
    fun findByUsername(username:String):Member?
}