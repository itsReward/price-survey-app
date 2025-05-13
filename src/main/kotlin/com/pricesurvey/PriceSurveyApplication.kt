package com.pricesurvey

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class PriceSurveyApplication

fun main(args: Array<String>) {
    runApplication<PriceSurveyApplication>(*args)
}