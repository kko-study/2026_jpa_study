package com.example.joonbug

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(2)
class JpaRunner(
    private val entityManagerFactory: EntityManagerFactory
) : CommandLineRunner {
    override fun run(vararg args: String) {
        val em = entityManagerFactory.createEntityManager()
        val tx = em.transaction

        try {
            tx.begin()
            logic(em)
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

    private fun logic(em: EntityManager) {
        val member = Member(id = "id1", name = "JoonBug", age = 30)
        em.persist(member)

        member.age = 31

        println("==== JpaRunner =====")
        val found = em.find(Member::class.java, "id1")
        println("findMember = ${found.name}, age = ${found.age}")

        val members = em.createQuery("select m from Member m", Member::class.java)
            .resultList
        println("member.size = ${members.size}")

        em.remove(member)
    }
}