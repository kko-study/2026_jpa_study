package com.example.joonbug

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * 3장. 영속성 관리 - 회원 등록, 조회, 수정 예제
 */
@Component
@Order(3)
class JpaMain(
    private val entityManagerFactory: EntityManagerFactory
) : CommandLineRunner {

    override fun run(vararg args: String) {
        val em = entityManagerFactory.createEntityManager()
        val tx = em.transaction
        println("==== JPA MAIN ====")
        try {
            tx.begin()

            // 회원 저장
            val member = Member(id = "1", name = "홍길동", age = 20)
            em.persist(member)
            println("회원 저장: id=${member.id}, name=${member.name}")

            // 저장한 회원 조회
            val foundMember = em.find(Member::class.java, "1")
            println("회원 조회: id=${foundMember.id}, name=${foundMember.name}")

            // 회원 이름 변경 (변경 감지 - Dirty Checking)
            foundMember.name = "김철수"
            println("회원 이름 변경됨: ${foundMember.name}")
            println("member: ${member.id}, name=${member.name}, age=${member.age}")
            println("foundMember: ${foundMember.id}, name=${foundMember.name}, age=${foundMember.age}")

            tx.commit()
        } catch (e: Exception) {
            if (tx.isActive) {
                tx.rollback()
            }
            throw e
        } finally {
            em.close()
        }
    }
}