package com.example.transactiontest

import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.InitBinder

@Component
@Aspect
class TestControllerAdvice(val testJdbcRepository: TestJdbcRepository) {
    @Before(value = "execution(* com.example.transactiontest.TestController.*(..))")
    fun truncate() {
        testJdbcRepository.truncate()
    }
}