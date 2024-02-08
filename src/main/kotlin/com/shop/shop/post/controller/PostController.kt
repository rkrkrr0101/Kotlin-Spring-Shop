package com.shop.shop.post.controller

import com.shop.shop.common.Result
import com.shop.shop.post.dto.PostRequestDto
import com.shop.shop.post.dto.PostTitleResponseDto
import com.shop.shop.post.service.PostService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/post")
class PostController(val postService: PostService) {
    @PostMapping("/save")
    fun save(@Validated @RequestBody requestDto: PostRequestDto,bindingResult: BindingResult):Result<Any?>{
        if (bindingResult.hasErrors()){
            return Result(bindingResult.fieldError)
        }
        return Result(postService.save(requestDto))
    }

    @GetMapping("/{title}")
    fun findTitlePaging(@PathVariable title:String,page:Int=0):Result<Page<PostTitleResponseDto>>{
        val pageRequest=PageRequest.of( //예외처리추가
            page,10, Sort.by("price").descending()
        )
        return Result(postService.findByTitlePaging(title,pageRequest))
    }
}