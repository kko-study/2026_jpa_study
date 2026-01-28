package com.example.exam_proj.chatper3

import jakarta.persistence.EntityManagerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class HomeWorkRunner1(
    private val entityManagerFactory: EntityManagerFactory,
) : CommandLineRunner {
    override fun run(vararg args: String) {
        val em = entityManagerFactory.createEntityManager()
        val tx = em.transaction

        try {
            tx.begin()

            val member = HWMember()
            member.id = 1L
            member.name = "홍길동"
            em.persist(member)
            val searchMember = em.find(HWMember::class.java, 1L)
            println("저장된 회원 이름1: ${searchMember.name}")

            member.name = "김철수"

            val searchMember2 = em.find(HWMember::class.java, 1L)
            println("저장된 회원 이름2: ${searchMember2.name}")

            tx.commit()
        } catch (ex: Exception) {
            if (tx.isActive) {
                tx.rollback()
            }
            throw ex
        } finally {
            em.close()
        }
    }
}