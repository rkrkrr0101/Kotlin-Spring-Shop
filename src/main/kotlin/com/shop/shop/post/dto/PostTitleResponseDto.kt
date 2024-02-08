package com.shop.shop.post.dto

import com.shop.shop.post.domain.Post

class PostTitleResponseDto(
    val id:Long=0,
    val title:String,
    val price:Int,
    val discountRate:Int,
    val shipCount:Int,
    val coupon:String="",
    val buyCount:Int=0,
    val titleImage:String="",) {
    companion object{
        fun domainToDto(post: Post):PostTitleResponseDto{
            return PostTitleResponseDto(
                post.id,
                post.title,
                post.price,
                post.discountRate,
                post.shipCount,
                post.coupon,
                post.buyCount,
                post.titleImage,)
        }
    }
}