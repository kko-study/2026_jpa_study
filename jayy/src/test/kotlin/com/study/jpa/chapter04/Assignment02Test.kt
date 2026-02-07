package com.study.jpa.chapter04

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.Persistence
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Assignment02Test {

    private lateinit var emf: EntityManagerFactory
    private lateinit var em: EntityManager

    @BeforeAll
    fun setUpAll() {
        emf = Persistence.createEntityManagerFactory("chapter04")
    }

    @AfterAll
    fun tearDownAll() {
        emf.close()
    }

    @BeforeEach
    fun setUp() {
        em = emf.createEntityManager()
    }

    @AfterEach
    fun tearDown() {
        em.close()
    }

    @Test
    @DisplayName("과제2: User 중복 loginId 저장 시 예외 발생")
    fun `같은 loginId로 두 번 저장하면 예외가 발생한다`() {
        val tx = em.transaction

        tx.begin()
        val user1 = User(
            loginId = "duplicate_test",
            email = "user1@example.com",
            name = "김철수",
            age = 25
        )
        em.persist(user1)
        em.clear()
        tx.commit()

        // persist() 호출에서 UNIQUE 제약조건 위반 예외가 발생한다
        val tx2 = em.transaction
        tx2.begin()
        val user2 = User(
            loginId = "duplicate_test",
            email = "user2@example.com",
            name = "박영희",
            age = 30
        )

        assertThrows<Exception> {
            em.persist(user2)
        }

        em.close()
    }
}
