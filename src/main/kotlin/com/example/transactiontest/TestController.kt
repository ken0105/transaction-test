package com.example.transactiontest

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Transactional
class TestController(
        private val testJdbcRepository: TestJdbcRepository,
        private val testJpaRepository: TestJpaRepository
) {
    @RequestMapping("/test1")
    fun startTransaction1(): HttpEntity<HttpStatus> {
        // JPA INSERT →　JDBC　SELECT
        testJpaRepository.save(Test(1, "inserted from Jpa"))
        return try {
            testJdbcRepository.findById(1)
            HttpEntity(HttpStatus.OK)
        } catch (e: EmptyResultDataAccessException) {
            HttpEntity(HttpStatus.CONFLICT)
        }
    }

    @RequestMapping("/test2")
    fun startTransaction2(): HttpEntity<HttpStatus> {
        // JDBC INSERT →　JPA　SELECT
        testJdbcRepository.insert()
        return if (testJpaRepository.findById(1).isEmpty) {
            HttpEntity(HttpStatus.CONFLICT)
        } else {
            HttpEntity(HttpStatus.OK)
        }
    }

    @RequestMapping("/test3")
    fun startTransaction3(): HttpEntity<HttpStatus> {
        // JPA INSERT → JDBC UPDATE
        testJpaRepository.save(Test(1, "inserted from Jpa"))
        testJdbcRepository.update(Test(1, "updated from Jdbc"))

        return try {
            val content = testJdbcRepository.findById(1)?.content
            if (content == "updated from Jdbc") {
                HttpEntity(HttpStatus.OK)
            } else {
                HttpEntity(HttpStatus.CONFLICT)
            }
        } catch (e: EmptyResultDataAccessException) {
            HttpEntity(HttpStatus.CONFLICT)
        }
    }

    @RequestMapping("/test4")
    fun startTransaction4(): HttpEntity<HttpStatus> {
        // JDBC INSERT → JPA UPDATE
        testJdbcRepository.insert()
        testJpaRepository.save(Test(1, "updated from Jpa"))

        val content = testJpaRepository.findById(1).orElse(Test(99, "Error")).content
        return if (content == "updated from Jpa") {
            HttpEntity(HttpStatus.OK)
        } else {
            println(content)
            HttpEntity(HttpStatus.CONFLICT)
        }
    }


}