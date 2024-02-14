package com.shop.shop.post.dto

class PostUpdateDto(
                        val title:String?=null,
                        val price:Int?=null,
                        val discountRate:Int?=null,
                        val shipCount:Int?=null,
                        val body:String?=null,
                        val coupon:String?=null,
                        val titleImage:String?=null,) {

}