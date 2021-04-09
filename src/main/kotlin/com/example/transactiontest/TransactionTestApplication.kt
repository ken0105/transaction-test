package com.example.transactiontest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
class TransactionTestApplication

fun main(args: Array<String>) {
    runApplication<TransactionTestApplication>(*args)
}
