package com.shop.shop.post.service

import com.shop.shop.member.domain.Member
import com.shop.shop.post.dto.PostRequestDto
import com.shop.shop.post.dto.PostTitleResponseDto
import com.shop.shop.post.dto.PostUpdateDto
import com.shop.shop.post.repository.PostRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.security.sasl.AuthenticationException

@Service
class PostService(val postRepository: PostRepository) {

    fun findByTitlePaging(title:String,pageable: Pageable):Page<PostTitleResponseDto>{
        val posts=postRepository.findByTitleContaining(title,pageable)
        return posts.map { PostTitleResponseDto.domainToDto(it) }
    }
    @Transactional
    fun save(postRequestDto: PostRequestDto,member: Member ):Long{
        val post = postRequestDto.dtoToDomain()
        post.member=member
        val savePost = postRepository.save(post)
        return savePost.id
    }
    @Transactional
    fun update(id:Long,postUpdateDto: PostUpdateDto,member: Member){
        val post = postRepository.findById(id).orElseThrow {
            throw IllegalArgumentException()
        }
        if (post.member.id!=member.id){
            throw AuthenticationException("권한없음")
        }
        post.update(postUpdateDto)
    }

}