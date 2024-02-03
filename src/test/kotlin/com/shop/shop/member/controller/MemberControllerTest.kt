package com.shop.shop.member.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.shop.shop.member.domain.Member
import com.shop.shop.member.dto.MemberCreateDto
import com.shop.shop.member.service.MemberService
import com.shop.shop.mock.MockSecurityConfig
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext

import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status



@WebMvcTest( MemberController::class)
@MockBean(JpaMetamodelMappingContext::class)
@ImportAutoConfiguration(MockSecurityConfig::class)
//@WithMockUser(roles = ["USER"])
class MemberControllerTest (
    @Autowired val mvc:MockMvc,
    @MockBean @Autowired  val memberService: MemberService,
    ){

    @Test
    fun 정상적으로_회원가입을_할수있다(){
        val dto = MemberCreateDto("username111", "password111", "name111", "qqq@aqw.com")
        given(memberService.isUser(dto))
            .willReturn(false)
        println(memberService.isUser(dto))
        println(memberService.isUser(dto))
        val om = ObjectMapper()
        val contentJson = om.writeValueAsString(dto)


        mvc.perform(post("/join")
            .contentType("application/json")
            .content(contentJson)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun 회원가입시_중복멤버일경우_예외가_발생한다(){
        val dto = MemberCreateDto("username111", "password111", "name111", "qqq@aqw.com")
        given(memberService.isUser(dto))
            .willReturn(true)
        val om = ObjectMapper()
        val contentJson = om.writeValueAsString(dto)



        mvc.perform(post("/join")
            .contentType("application/json")
            .content(contentJson)
        )
            .andExpect(status().isBadRequest)

    }
}

