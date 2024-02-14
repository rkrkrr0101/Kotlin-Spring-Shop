package com.shop.shop.post.repository

import com.shop.shop.member.domain.Member
import com.shop.shop.member.repository.MemberRepository
import com.shop.shop.post.domain.Post
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.transaction.BeforeTransaction
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = [ "spring.config.location = classpath:application-testContainer.yml"])
@Testcontainers
class PostRepositoryTest(@Autowired val postRepository: PostRepository,
                         @Autowired val memberRepository: MemberRepository,
                        ) {
    companion object{
        @JvmStatic
        @Container
        val mysqlContainer=MySQLContainer("mysql:8.0.33")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("1234")
            .withEnv("MYSQL_TCP_PORT","3307")
            //.withInitScript("init_data/fulltext_index_create.sql")
    }

    @BeforeTransaction
    @Rollback(false)
    fun init(){

            postRepository.deleteAll()
            memberRepository.deleteAll()

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

    @Test
    fun 정상적으로_전문검색을_할수있다(){
        //g

        val pageRequest= PageRequest.of(
            0,5, Sort.by("price")
        )

        //w
        val findPostPage = postRepository.findByTitleContaining("고기", pageRequest)
        //t
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
        val findPostPage = postRepository.findByTitleContaining("고기", pageRequest)

        //t
        Assertions.assertThat(findPostPage.content.size).isEqualTo(3)
        Assertions.assertThat(findPostPage.content[0].price).isEqualTo(4)
        Assertions.assertThat(findPostPage.content[2].price).isEqualTo(2)
    }
}