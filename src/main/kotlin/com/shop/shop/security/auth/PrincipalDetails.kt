package com.shop.shop.security.auth

import com.shop.shop.member.domain.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

class PrincipalDetails(val member: Member):UserDetails,OAuth2User {

    private var attributes=mutableMapOf<String,Any>()
    constructor(member: Member,attributes:MutableMap<String,Any>) : this(member) {
        this.attributes=attributes
    }


    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val collect = ArrayList<GrantedAuthority>()
        collect.add(GrantedAuthority { return@GrantedAuthority member.role })
        return collect
    }

    override fun getPassword(): String {
        return member.password
    }

    override fun getUsername(): String {
        return member.username
    }
    override fun getName(): String {
        return ""
    }

    override fun getAttributes(): MutableMap<String, Any> {
        return attributes
    }

    override fun isAccountNonExpired(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isAccountNonLocked(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isCredentialsNonExpired(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isEnabled(): Boolean {
        TODO("Not yet implemented")
    }




}