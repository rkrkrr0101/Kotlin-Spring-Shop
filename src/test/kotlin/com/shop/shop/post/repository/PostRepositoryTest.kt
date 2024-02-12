package com.shop.shop.post.repository

import com.shop.shop.member.domain.Member
import com.shop.shop.member.repository.MemberRepository
import com.shop.shop.post.domain.Post
import jakarta.transaction.Transactional
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import javax.sql.DataSource

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = [ "spring.config.location = classpath:application-testContainer.yml"])
@Testcontainers
//@Sql(scripts = ["classpath:/init_data/fulltext_index_create.sql"])
class PostRepositoryTest(@Autowired val postRepository: PostRepository,
                         @Autowired val memberRepository: MemberRepository,
    @Autowired val dataSource:DataSource ) {
    companion object{
        @JvmStatic
        @Container
        val mysqlContainer=MySQLContainer("mysql:8.0.33")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("1234")
            .withEnv("MYSQL_TCP_PORT","3307")
    }


    @Test
    fun abc(){
        val jdbcTemplate = JdbcTemplate(dataSource)
        jdbcTemplate.execute("alter table post add fulltext key fx_title (title) with parser ngram;")

        val member = Member("username111", "password111", "name111", "qqq@aqw.com")
        val post = Post("aaeqeq", 123, 10, 1, "da", "none", 3, "aa.img")
        memberRepository.save(member)
        val findMember = memberRepository.findByUsername("username111")!!
        post.member=findMember
        postRepository.save(post)

        val pageRequest= PageRequest.of(
            0,10, Sort.by("price").descending()
        )
        val findPostPage = postRepository.findByTitleContaining("eq", pageRequest)
        println(findPostPage.content[0].title)
    }
    @Test
    fun abcd(){
        val pageRequest= PageRequest.of(
            0,10, Sort.by("price").descending()
        )
        postRepository.findAll()
    }
}