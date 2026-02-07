package com.study.jpa.chapter03

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
    @DisplayName("과제2: 1차 캐시 동일성 - 같은 id 조회 시 동일 객체 반환")
    fun `같은 id로 두 번 조회하면 동일한 객체를 반환한다`() {
        val tx = em.transaction
        val id = 2L

        // given: 회원 영속 상태로 만들기
        tx.begin()
        em.persist(Member(id = id, name = "테스트유저"))

        // when: 같은 id로 1차 캐시에서 두 번 조회
        val findMember1 = em.find(Member::class.java, id)
        val findMember2 = em.find(Member::class.java, id)

        // then: 동일성(===) 보장
        assertSame(findMember1, findMember2)
        tx.commit()
    }

    @Test
    @DisplayName("과제2: 영속성 컨텍스트 초기화 후 다른 객체 반환")
    fun `영속성 컨텍스트 초기화 후 조회하면 다른 객체를 반환한다`() {
        val tx = em.transaction
        val id = 3L

        // given: 회원 저장 및 조회
        tx.begin()
        em.persist(Member(id = id, name = "테스트유저"))
        val beforeClear = em.find(Member::class.java, id)
        tx.commit()

        // when: 영속성 컨텍스트 초기화 후 DB에서 조회
        em.clear()
        val afterClear = em.find(Member::class.java, id)

        // then: 초기화 전후 엔티티는 다른 객체
        assertNotSame(beforeClear, afterClear)
        // 하지만 데이터는 동일
        assertEquals(beforeClear.name, afterClear.name)
    }
}
