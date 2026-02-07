package com.study.jpa.chapter03

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.Persistence
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Assignment01Test {

    private lateinit var emf: EntityManagerFactory
    private lateinit var em: EntityManager

    @BeforeAll
    fun setUpAll() {
        emf = Persistence.createEntityManagerFactory("chapter03")
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
    @DisplayName("과제1: 회원 등록, 조회, 수정 - 변경 감지로 김철수가 저장되어야 함")
    fun `회원 등록 후 수정하면 변경 감지로 DB에 반영된다`() {
        val tx = em.transaction
        val id = 1L

        // given: 회원 등록 및 수정
        tx.begin()
        em.persist(Member(id = id, name = "홍길동"))
        // 저장한 회원 조회
        val member = em.find(Member::class.java, id)
        // 회원 이름을 "김철수"로 변경
        member.name = "김철수"
        tx.commit()

        // when: 영속성 컨텍스트 초기화 후 DB에서 직접 조회
        em.clear()
        val foundMember = em.find(Member::class.java, id)

        // then: DB에 "김철수"가 저장되어 있어야 함
        assertEquals("김철수", foundMember.name)
    }
}
