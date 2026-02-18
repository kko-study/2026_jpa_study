package com.example.exam_proj

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.EntityTransaction
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class CommandRunner(
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
        val member = Member(id = "id1", name = "Alice", age = 25)
        em.persist(member)

        member.age = 40

        println("====CommandRunner=====")
        val found = em.find(Member::class.java, "id1")
        println("findMember = ${found.name}, age = ${found.age}")

        val members = em.createQuery("select m from Member m", Member::class.java)
            .resultList
        val memberSize = members.size
        println("member.size = ${memberSize}")

        em.remove(member)
    }
}
