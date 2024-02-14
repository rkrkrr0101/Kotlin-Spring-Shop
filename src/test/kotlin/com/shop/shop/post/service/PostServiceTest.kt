package com.shop.shop.post.service

import com.shop.shop.member.domain.Member
import com.shop.shop.member.repository.MemberRepository
import com.shop.shop.post.domain.Post
import com.shop.shop.post.dto.PostRequestDto
import com.shop.shop.post.dto.PostUpdateDto
import com.shop.shop.post.repository.PostRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.transaction.AfterTransaction
import org.springframework.test.context.transaction.BeforeTransaction
import org.testcontainers.junit.jupiter.Testcontainers
import javax.security.sasl.AuthenticationException

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = [ "spring.config.location = classpath:application-testContainer.yml"])
@Testcontainers
class PostServiceTest(@Autowired private val postRepository: PostRepository,
                      @Autowired private val memberRepository: MemberRepository,
) {
    val postService=PostService(postRepository)

    @BeforeTransaction
    @Rollback(false)
    fun init(){
        val postList = mutableListOf<Post>()
        postList.add(Post("고기 라면 볶음", 3, 10, 1, "da", "none", 3, "aa.img"))
        postList.add(Post("고기 피자 세트", 4, 10, 1, "da", "none", 3, "aa.img"))
        postList.add(Post("라면 제육 정식", 1, 10, 1, "da", "none", 3, "aa.img"))
        postList.add(Post("고기 고기 고기", 2, 10, 1, "da", "none", 3, "aa.img"))
        postList.add(Post("제육 라면 세트", 7, 10, 1, "da", "none", 3, "aa.img"))
        postList.add(Post("야채 피자 세트", 6, 10, 1, "da", "none", 3, "aa.img"))
        postList.add(Post("야채 제육 정식", 5, 10, 1, "da", "none", 3, "aa.img"))
        postList.add(Post("호빵 붕어빵 식빵", 5, 10, 1, "da", "none", 3, "aa.img"))
        postList.add(Post("라면 라면 라면", 8, 10, 1, "da", "none", 3, "aa.img"))
        postList.add(Post("피자 피자 피자", 9, 10, 1, "da", "none", 3, "aa.img"))
        postList.add(Post("rocking and rolling", 9, 10, 1, "da", "none", 3, "aa.img"))

        val member = Member("username111", "password111", "name111", "qqq@aqw.com")
        memberRepository.save(member)
        val findMember = memberRepository.findByUsername("username111")!!
        for (post in postList) {
            post.member = findMember
            postRepository.save(post)
        }
    }
    @AfterTransaction
    @Rollback(false)
    fun clean(){
        postRepository.deleteAll()
        memberRepository.deleteAll()
    }

    @Test
    fun 정상적으로_전문검색을_할수있다(){
        val pageRequest= PageRequest.of(
            0,5, Sort.by("price")
        )
        val findPostPage = postService.findByTitlePaging("고기", pageRequest)

        Assertions.assertThat(findPostPage.content.size).isEqualTo(3)
        Assertions.assertThat(findPostPage.content[0].price).isEqualTo(2)
        Assertions.assertThat(findPostPage.content[2].price).isEqualTo(4)
    }
    @Test
    fun 전문검색을_하면서_높은가격순_정렬을_할수있다(){
        //g
        val pageRequest= PageRequest.of(
            0,5, Sort.by("price").descending()
        )
        //w
        val findPostPage = postService.findByTitlePaging("고기", pageRequest)
        //t
        Assertions.assertThat(findPostPage.content.size).isEqualTo(3)
        Assertions.assertThat(findPostPage.content[0].price).isEqualTo(4)
        Assertions.assertThat(findPostPage.content[2].price).isEqualTo(2)
    }

    @Test
    fun postRequestDto를_받아서_post를_저장할수_있다(){
        postRepository.deleteAll()
        memberRepository.deleteAll()
        val postRequestDto = PostRequestDto("힐스테이트 자이 시티", 3000, 10, 1, "da", "none", 3, "aa.img")
        val member = Member("username111", "password111", "name111", "qqq@aqw.com")
        memberRepository.save(member)
        val findMember = memberRepository.findByUsername("username111")!!

        val saveId = postService.save(postRequestDto, findMember)
        val findPost = postRepository.findById(saveId).orElseThrow()

        Assertions.assertThat(findPost.title).isEqualTo("힐스테이트 자이 시티")
        Assertions.assertThat(findPost.price).isEqualTo(3000)
    }

    @Test
    fun postUpdateDto를_받아서_post를_수정할수있다(){
        postRepository.deleteAll()
        memberRepository.deleteAll()
        val postRequestDto = PostRequestDto("힐스테이트 자이 시티", 3000, 10, 1, "da", "none", 3, "aa.img")
        val member = Member("username111", "password111", "name111", "qqq@aqw.com")
        memberRepository.save(member)
        val findMember = memberRepository.findByUsername("username111")!!
        val saveId = postService.save(postRequestDto, findMember)
        val postUpdateDto = PostUpdateDto("고구마볶음", titleImage = "lux.img")
        postService.update(saveId,postUpdateDto,member)

        val findPost = postRepository.findById(saveId).orElseThrow()

        Assertions.assertThat(findPost.title).isEqualTo("고구마볶음")
        Assertions.assertThat(findPost.price).isEqualTo(3000)
        Assertions.assertThat(findPost.titleImage).isEqualTo("lux.img")
    }
    @Test
    fun 다른멤버면_post를_업데이트를_할수_없다(){
        postRepository.deleteAll()
        memberRepository.deleteAll()
        val postRequestDto = PostRequestDto("힐스테이트 자이 시티", 3000, 10, 1, "da", "none", 3, "aa.img")
        val member = Member("username111", "password111", "name111", "qqq@aqw.com")
        val otherMember = Member("otherusername111", "otherpassword111", "othername111", "otherqqq@aqw.com")
        memberRepository.save(member)
        memberRepository.save(otherMember)
        val otherFindMember = memberRepository.findByUsername("otherusername111")!!
        val saveId = postService.save(postRequestDto, otherFindMember)
        val postUpdateDto = PostUpdateDto("고구마볶음", titleImage = "lux.img")

        Assertions.assertThatThrownBy {
            postService.update(saveId,postUpdateDto,member)
        }.isInstanceOf(AuthenticationException::class.java)
            .hasMessage("권한없음")
    }

}