package homework.chapter_13

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["homework.chapter_12", "homework.chapter_13"])
class Chapter13Application

fun main(args: Array<String>) {
    runApplication<Chapter13Application>(*args)
}