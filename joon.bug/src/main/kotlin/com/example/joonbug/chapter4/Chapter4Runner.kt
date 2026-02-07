package com.example.joonbug.chapter4

import jakarta.persistence.EntityManagerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.time.LocalDate

/**
 * 4장. 엔티티 매핑 테스트
 */
@Component
@Order(5)
class Chapter4Runner(
    private val entityManagerFactory: EntityManagerFactory
) : CommandLineRunner {

    override fun run(vararg args: String) {
        println("\n===== Chapter 4: 엔티티 매핑 테스트 =====")
        testMember4()
        testUserUniqueConstraint()
    }

    private fun testMember4() {
        val em = entityManagerFactory.createEntityManager()
        val tx = em.transaction

        try {
            tx.begin()
            println("\n----- Member4 엔티티 테스트 -----")

            val member = Member4(
                userName = "홍길동",
                age = 30,
                email = "hong@test.com",
                role = RoleType.ADMIN,
                createdAt = LocalDate.now(),
                description = "긴 설명 텍스트...",
                tempData = "이 데이터는 DB에 저장되지 않음"
            )
            em.persist(member)
            println("Member4 저장: id=${member.id}, userName=${member.userName}, role=${member.role}")
            println("tempData (Transient): ${member.tempData}")

            em.flush()
            em.clear()

            val found = em.find(Member4::class.java, member.id)
            println("Member4 조회: id=${found.id}, userName=${found.userName}")
            println("tempData 조회 (null 예상): ${found.tempData}")

            em.remove(found)
            tx.commit()
        } catch (e: Exception) {
            if (tx.isActive) tx.rollback()
            throw e
        } finally {
            em.close()
        }
    }

    private fun testUserUniqueConstraint() {
        val em = entityManagerFactory.createEntityManager()
        val tx = em.transaction

        try {
            tx.begin()
            println("\n----- User UNIQUE 제약조건 테스트 -----")

            val user1 = User(loginId = "user001", email = "user1@test.com", name = "김철수", age = 25)
            em.persist(user1)
            println("User1 저장: loginId=${user1.loginId}")

            em.flush()

            // 같은 loginId로 두 번 저장 시도
            val user2 = User(loginId = "user001", email = "user2@test.com", name = "이영희", age = 30)
            em.persist(user2)

            em.flush() // 여기서 예외 발생 예상

            tx.commit()
        } catch (e: Exception) {
            println("예외 발생 (UNIQUE 제약조건 위반): ${e.cause?.message ?: e.message}")
            if (tx.isActive) tx.rollback()
        } finally {
            em.close()
        }

        // 정리: user1 삭제
        val em2 = entityManagerFactory.createEntityManager()
        val tx2 = em2.transaction
        try {
            tx2.begin()
            val user = em2.createQuery("select u from User u where u.loginId = :loginId", User::class.java)
                .setParameter("loginId", "user001")
                .resultList
                .firstOrNull()
            if (user != null) {
                em2.remove(user)
            }
            tx2.commit()
        } catch (e: Exception) {
            if (tx2.isActive) tx2.rollback()
        } finally {
            em2.close()
        }
    }
}