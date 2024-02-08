package com.shop.shop.post.dto

import com.shop.shop.post.domain.Post
import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

class PostRequestDto(
    val title:String,
    val price:Int,
    val discountRate:Int=0,
    val shipCount:Int=0,
    val body:String="",
    val coupon:String="",
    val buyCount:Int=0,
    val titleImage:String="", ) {
    fun dtoToDomain():Post{
        return Post(title, price, discountRate, shipCount, body, coupon, buyCount, titleImage)
    }
}