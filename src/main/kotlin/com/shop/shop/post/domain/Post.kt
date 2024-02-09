package com.shop.shop.post.domain

import com.shop.shop.domain.BaseEntity
import com.shop.shop.member.domain.Member
import com.shop.shop.post.dto.PostUpdateDto
import jakarta.persistence.*

//사진,제목,가격,할인율,무료배송,구매수,본문(json이나 text,사진은 텍스트중간에 경로로),쿠폰으로 구성됨
@Entity
//@Table(indexes = [Index(name = "FULLTEXT", columnList = "title")])
class Post(
    var title:String,
    var price:Int,
    var discountRate:Int,
    var shipCount:Int,
    @Column(columnDefinition = "TEXT")
    var body:String,
    var coupon:String="",
    var buyCount:Int=0,
    var titleImage:String="",
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long=0,
):BaseEntity() {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    lateinit var member:Member

    fun update(postUpdateDto: PostUpdateDto){
        title=postUpdateDto.title?:title
        price=postUpdateDto.price?:price
        discountRate=postUpdateDto.discountRate?:discountRate
        shipCount=postUpdateDto.shipCount?:shipCount
        body=postUpdateDto.body?:body
        coupon=postUpdateDto.coupon?:coupon
        titleImage=postUpdateDto.titleImage?:titleImage
    }


}