package com.shop.shop.member.domain

import com.shop.shop.domain.BaseEntity
import com.shop.shop.token.domain.RefreshToken
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.jetbrains.annotations.NotNull

@Entity
class Member(
    var username:String,
    var password:String,
    var name:String,
    var email:String,
    var role:String="ROLE_USER",
    var provider:String="",
    var providerId:String="",
    var refreshTokenId: String="",
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long=0,
):BaseEntity() {
    override fun toString(): String {
        return "Member(username='$username', password='$password', name='$name', email='$email', role='$role', provider='$provider', providerId='$providerId', id=$id)"
    }
}