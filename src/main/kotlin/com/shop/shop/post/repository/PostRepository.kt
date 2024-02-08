package com.shop.shop.post.repository

import com.shop.shop.post.domain.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PostRepository:JpaRepository<Post,Long> {



    @Query("select p from Post p where match_against(p.title,:title)")
    fun findByTitleContaining(@Param("title") title:String,pageable: Pageable): Page<Post>
}