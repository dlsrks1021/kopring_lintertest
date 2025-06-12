package io.kckim.kopring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
class KopringApplication

fun main(args: Array<String>) {
    runApplication<KopringApplication>(*args)
}
