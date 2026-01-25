package com.example.joonbug

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JoonBugApplication

fun main(args: Array<String>) {
    runApplication<JoonBugApplication>(*args)
}