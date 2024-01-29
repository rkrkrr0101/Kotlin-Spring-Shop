package com.shop.shop.member.domain

import com.shop.shop.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.jetbrains.annotations.NotNull

@Entity
class Member(
    @NotNull
    var username:String,
    @NotNull
    var password:String,
    @NotNull
    var name:String,
    @NotNull
    var email:String,
    @NotNull
    var role:String="ROLE_USER",
    var provider:String="",
    var providerId:String="",
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long,
):BaseEntity() {
}