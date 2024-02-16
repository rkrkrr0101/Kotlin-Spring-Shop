package com.shop.shop.post.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.shop.shop.member.domain.Member
import com.shop.shop.member.repository.MemberRepository
import com.shop.shop.post.domain.Post
import com.shop.shop.post.dto.PostRequestDto
import com.shop.shop.post.dto.PostTitleResponseDto
import com.shop.shop.post.dto.PostUpdateDto
import com.shop.shop.post.repository.PostRepository
import com.shop.shop.post.service.PostService
import com.shop.shop.token.service.JwtService
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.testcontainers.junit.jupiter.Testcontainers


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@MockBean(JpaMetamodelMappingContext::class)//baseEntity추가하려면 있어야함
@Testcontainers
@TestPropertySource(properties = [ "spring.config.location = classpath:application-testContainer.yml"])
//@ImportAutoConfiguration(MockSecurityConfig::class)
class PostControllerTest(
    @Autowired val mvc: MockMvc,
    @MockBean @Autowired  val postService: PostService,
    @Autowired val memberRepository: MemberRepository,
    @Autowired val postRepository: PostRepository){

    @Test
    fun 정상적으로_포스트를_조회할수_있다(){
        val mockReturnPage:Page<PostTitleResponseDto> = Mockito.mock(Page::class.java) as Page<PostTitleResponseDto>
        val pageRequest= PageRequest.of(
            0,5, Sort.by("price")
        )
        val searchWord="고기"
        given(postService.findByTitlePaging(searchWord,pageRequest))
            .willReturn(mockReturnPage)





        mvc.perform(
            MockMvcRequestBuilders.get("""/post/${searchWord}""")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }
    @Test
    fun 정상적으로_포스트를_저장할수있다(){
        val member = Member("username111", "password111", "name111", "qqq@aqw.com")
        member.role="ROLE_MANAGER"
        memberRepository.save(member)
        val tokenDto = JwtService(memberRepository).createToken(member)
        val postRequestDto = PostRequestDto("힐스테이트 자이 시티", 3000, 10, 1, "da", "none", 3, "aa.img")

        val om = ObjectMapper()
        val contentJson = om.writeValueAsString(postRequestDto)

        mvc.perform(
            MockMvcRequestBuilders.post("/post/save")
            .contentType("application/json")
            .content(contentJson)
                .header("Authorization",tokenDto.accessTokenCode)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }
    @Test
    fun 로그인을_하지_않으면_포스트를_저장할수_없다(){

        val postRequestDto = PostRequestDto("힐스테이트 자이 시티", 3000, 10, 1, "da", "none", 3, "aa.img")

        val om = ObjectMapper()
        val contentJson = om.writeValueAsString(postRequestDto)

        mvc.perform(
            MockMvcRequestBuilders.post("/post/save")
                .contentType("application/json")
                .content(contentJson)
        )
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }
    @Test
    fun 권한이_없으면_포스트를_저장할수_없다(){
        val member = Member("username111", "password111", "name111", "qqq@aqw.com")
        member.role="ROLE_USER"
        memberRepository.save(member)
        val tokenDto = JwtService(memberRepository).createToken(member)
        val postRequestDto = PostRequestDto("힐스테이트 자이 시티", 3000, 10, 1, "da", "none", 3, "aa.img")

        val om = ObjectMapper()
        val contentJson = om.writeValueAsString(postRequestDto)

        mvc.perform(
            MockMvcRequestBuilders.post("/post/save")
                .contentType("application/json")
                .content(contentJson)
                .header("Authorization",tokenDto.accessTokenCode)
        )
            .andExpect(MockMvcResultMatchers.status().isForbidden)
    }
    @Test
    fun 정상적으로_포스트를_업데이트를_할수있다(){
        val member = Member("username111", "password111", "name111", "qqq@aqw.com")
        member.role="ROLE_MANAGER"
        memberRepository.save(member)
        val tokenDto = JwtService(memberRepository).createToken(member)

        val findMember = memberRepository.findByUsername("username111")!!
        val post=Post("고기 라면 볶음", 3, 10, 1, "da", "none", 3, "aa.img")
        post.member=findMember
        val saveId = postRepository.save(post).id

        val postUpdateDto = PostUpdateDto(title = "제육 국밥")

        val om = ObjectMapper()
        val contentJson = om.writeValueAsString(postUpdateDto)

        mvc.perform(
            MockMvcRequestBuilders.patch("""/post/${saveId}""")
                .contentType("application/json")
                .content(contentJson)
                .header("Authorization",tokenDto.accessTokenCode)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }
    @Test
    fun 로그인을_하지_않으면_포스트를_업데이트_할수_없다(){
        val member = Member("username111", "password111", "name111", "qqq@aqw.com")
        member.role="ROLE_MANAGER"
        memberRepository.save(member)

        val findMember = memberRepository.findByUsername("username111")!!
        val post=Post("고기 라면 볶음", 3, 10, 1, "da", "none", 3, "aa.img")
        post.member=findMember
        val saveId = postRepository.save(post).id

        val postUpdateDto = PostUpdateDto(title = "제육 국밥")

        val om = ObjectMapper()
        val contentJson = om.writeValueAsString(postUpdateDto)

        mvc.perform(
            MockMvcRequestBuilders.patch("""/post/${saveId}""")
                .contentType("application/json")
                .content(contentJson)

        )
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }
}