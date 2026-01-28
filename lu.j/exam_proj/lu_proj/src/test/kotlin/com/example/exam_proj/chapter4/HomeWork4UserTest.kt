package com.example.exam_proj.chapter4

import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.test.context.TestPropertySource
import org.hibernate.exception.ConstraintViolationException

@DataJpaTest
@TestPropertySource(
    properties = [
        "spring.jpa.hibernate.ddl-auto=create"
    ]
)
class HomeWork4UserTest {
    @Autowired
    lateinit var entityManager: EntityManager

    @Test
    fun duplicateLoginIdThrowsException() {
        val user1 = HomeWork4User().apply {
            loginId = "login-1"
            email = "user1@example.com"
            name = "Alice"
            age = 20
        }
        val user2 = HomeWork4User().apply {
            loginId = "login-1"
            email = "user2@example.com"
            name = "John"
            age = 30
        }

        entityManager.persist(user1)
        entityManager.persist(user2)

        assertThrows(ConstraintViolationException::class.java) {
            entityManager.flush()
        }
    }
}
