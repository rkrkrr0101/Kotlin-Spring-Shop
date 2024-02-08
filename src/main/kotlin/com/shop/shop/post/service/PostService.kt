package com.shop.shop.post.service

import com.shop.shop.post.dto.PostRequestDto
import com.shop.shop.post.dto.PostTitleResponseDto
import com.shop.shop.post.repository.PostRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class PostService(val postRepository: PostRepository) {

    fun findByTitlePaging(title:String,pageable: Pageable):Page<PostTitleResponseDto>{
        val posts=postRepository.findByTitleContaining(title,pageable)
        return posts.map { PostTitleResponseDto.domainToDto(it) }
    }
    @Transactional
    fun save(postRequestDto: PostRequestDto):Long{
        val post = postRepository.save(postRequestDto.dtoToDomain())
        return post.id
    }
}