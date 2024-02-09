package com.shop.shop.post.controller

import com.shop.shop.common.ApiHeader
import com.shop.shop.common.Result
import com.shop.shop.post.dto.PostRequestDto
import com.shop.shop.post.dto.PostTitleResponseDto
import com.shop.shop.post.dto.PostUpdateDto
import com.shop.shop.post.service.PostService
import com.shop.shop.security.auth.PrincipalDetails
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.security.sasl.AuthenticationException

@RestController
@RequestMapping("/post")
class PostController(val postService: PostService) {


    @GetMapping("/{title}")
    fun findTitlePaging(@PathVariable title:String,page:Int=0):Result<Page<PostTitleResponseDto>>{
        val pageRequest=PageRequest.of( //예외처리추가
            page,10, Sort.by("price").descending()
        )
        return Result(postService.findByTitlePaging(title,pageRequest))
    }
    @PostMapping("/save")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    fun save(@Validated @RequestBody requestDto: PostRequestDto,
             bindingResult: BindingResult,
             @AuthenticationPrincipal principalDetails: PrincipalDetails?,
    ):Result<Any?>{
        if (bindingResult.hasErrors()){
            return Result(bindingResult.fieldError)
        }
        if (principalDetails==null){
            throw AuthenticationException()
        }


        val member = principalDetails.member
        return Result(postService.save(requestDto,member))
    }
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    fun update(@Validated @RequestBody requestDto: PostUpdateDto,
               @PathVariable id:Long,
             bindingResult: BindingResult,
             @AuthenticationPrincipal principalDetails: PrincipalDetails?,
    ):Result<Any?>{
        if (bindingResult.hasErrors()){
            return Result(bindingResult.fieldError)
        }
        if (principalDetails==null){
            throw AuthenticationException()
        }


        val member = principalDetails.member
        return Result(postService.update(id,requestDto,member))
    }

    @ExceptionHandler(AuthenticationException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    //@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun illegalArgument():Result<ApiHeader>{
        return Result(ApiHeader(401,"인증안됨"))
    }
}