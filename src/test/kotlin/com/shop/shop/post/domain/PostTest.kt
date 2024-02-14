package com.shop.shop.post.domain

import com.shop.shop.post.dto.PostUpdateDto
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class PostTest{

    @Test
    fun postUpdateDto를_받아서_현재값을_업데이트_할수_있다(){
        val post = Post("힐스테이트 자이 시티", 3000, 10, 1, "da", "none", 3, "aa.img")
        val postUpdateDto = PostUpdateDto("고구마볶음", 20, 30, 2, "qeqweqwe", "none", "lux.img" )

        post.update(postUpdateDto)

        Assertions.assertThat(post.title).isEqualTo("고구마볶음")
        Assertions.assertThat(post.titleImage).isEqualTo("lux.img")
        Assertions.assertThat(post.price).isEqualTo(20)
    }
    @Test
    fun postUpdateDto를_받아서_현재값을_일부만_업데이트_할수_있다(){
        val post = Post("힐스테이트 자이 시티", 3000, 10, 1, "da", "none", 3, "aa.img")
        val postUpdateDto = PostUpdateDto("고구마볶음", titleImage = "lux.img")

        post.update(postUpdateDto)

        Assertions.assertThat(post.title).isEqualTo("고구마볶음")
        Assertions.assertThat(post.titleImage).isEqualTo("lux.img")
        Assertions.assertThat(post.price).isEqualTo(3000)
    }
}

