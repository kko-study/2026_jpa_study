package com.example.joonbug

import jakarta.persistence.EntityManagerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * 3장. 영속성 관리 - 1차 캐시와 동일성 보장 테스트
 */
@Component
@Order(4)
class CacheTest(
    private val entityManagerFactory: EntityManagerFactory
) : CommandLineRunner {

    override fun run(vararg args: String) {
        val em = entityManagerFactory.createEntityManager()
        val tx = em.transaction
        tx.begin()

        // 1. 새로운 회원을 생성하여 영속 상태로 만들기
        val member = Member(id = "100", name = "테스트회원", age = 25)
        em.persist(member)
        // 예상 SQL: INSERT

        println("===== 1차 캐시 테스트 =====")

        // 2. 같은 id로 두 번 조회하여 동일성(===) 비교
        val member1 = em.find(Member::class.java, "100")
        // 예상 SQL: 없음 - 1차 캐시에서 조회

        val member2 = em.find(Member::class.java, "100")
        // 예상 SQL: 없음 - 1차 캐시에서 조회

        println("member1 === member2: ${member1 === member2}")  // true

        println("\n===== 영속성 컨텍스트 초기화 테스트 =====")

        // 3. 영속성 컨텍스트 초기화 전 참조 저장
        val beforeClear = em.find(Member::class.java, "100")
        // 예상 SQL: 없음 - 1차 캐시에서 조회

        em.flush()
        em.clear()
        println("영속성 컨텍스트 초기화됨 (clear)")

        // 4. 초기화 후 다시 조회
        val afterClear = em.find(Member::class.java, "100")
        // 예상 SQL: SELECT - DB에서 조회

        // 5. 초기화 전후의 엔티티가 같은 객체인지 비교
        println("beforeClear === afterClear: ${beforeClear === afterClear}")  // false

        tx.commit()
        em.close()
    }
}