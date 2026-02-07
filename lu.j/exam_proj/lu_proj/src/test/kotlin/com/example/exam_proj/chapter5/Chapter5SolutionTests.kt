package com.example.exam_proj.chapter5

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@SpringBootTest
class Chapter5SolutionTests @Autowired constructor(
    private val entityManagerFactory: EntityManagerFactory
) {
    private fun runInTx(block: (EntityManager) -> Unit) {
        val em = entityManagerFactory.createEntityManager()
        val tx = em.transaction
        try {
            tx.begin()
            block(em)
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

    @Test
    fun solution1() = runInTx { em ->
        val member = Chapter5Member(name = "홍길동")
        val team = Chapter5Team(name = "개발팀")

        em.persist(team)
        member.setTeam(team)
        em.persist(member)

        em.flush()

        val foundTeam = member.getTeam()
        assertEquals(foundTeam?.getName(), "개발팀")
    }

    @Test
    fun solution2() = runInTx { em ->
        val member = Chapter5Member(name = "홍길동")
        val team = Chapter5Team(name = "개발팀")
        val team2 = Chapter5Team(name = "기획팀")

        em.persist(team)
        member.setTeam(team)
        em.persist(member)

        em.flush()

        val member2 = em.find(Chapter5Member::class.java, member.id)
        em.persist(team2)
        member2.setTeam(team2)
        em.persist(member2)

        em.flush()

        val foundMember = em.find(Chapter5Member::class.java, member.id)
        assertEquals(foundMember.getTeam()?.getName(), "기획팀")
    }

    @Test
    fun solution3() = runInTx { em ->
        val member = Chapter5Member(name = "홍길동")
        val team = Chapter5Team(name = "개발팀")

        em.persist(team)
        team.getMembers().add(member)
        em.persist(member)

        em.flush()

        val member2 = em.find(Chapter5Member::class.java, member.id)
        assertNull(member2.getTeam())

        // null 인 이유, team 엔티티는 연관관계 주인이 아니기 때문에 읽기만 가능하다.
    }

    @Test
    fun solution4() = runInTx { em ->
        val member = Chapter5Member(name = "홍길동")
        val team = Chapter5Team(name = "개발팀")

        em.persist(team)
        member.setTeam(team)
        em.persist(member)

        println("team's member size = ${team.getMembers().size}")

        em.flush()
        em.clear()

        val team2 = em.find(Chapter5Team::class.java, team.id)

        println("after flush team's member size = ${team2.getMembers().size}")

        assertNotEquals(team.getMembers().size, team2.getMembers().size)

        // member.setTeam(team) -> FK 설정됨
        // team.getMembers.size -> 컬렉션에는 반영 안됨.
        // em.flush() -> DB에 반영
        // em.clear() -> 1차 캐시 삭제
        // em.find() -> DB 에서 다시 불러오면 1개 추가된 것으로 확인됨
    }

    @Test
    fun solution6() = runInTx { em ->
        val member = Chapter5Member(name = "홍길동")
        val team = Chapter5Team(name = "개발팀")

        // 양방향 관계 설정
        member.setTeam(team)
        team.getMembers().add(member)

        em.persist(team)
        em.persist(member)

        em.flush()
        em.clear()

        val foundMember = em.find(Chapter5Member::class.java, member.id)
        assertThrows<StackOverflowError> { foundMember.toString() }
    }
}
