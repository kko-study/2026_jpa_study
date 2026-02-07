package com.example.exam_proj.chapter6

import com.example.exam_proj.chapter5.Chapter5Member
import com.example.exam_proj.chapter5.Chapter5Team
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import kotlin.test.assertEquals

@SpringBootTest
class Chapter6SolutionTests@Autowired constructor(
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
        val member = Chapter6Member(name = "홍길동")
        val member2 = Chapter6Member(name = "김철수")
        val team = Chapter6Team(name = "개발팀")

        em.persist(team)
        team.getMembers().add(member)
        team.getMembers().add(member2)
        em.persist(member)
        em.persist(member2)

        em.flush()
        // JoinColumn 을 주석처리 했을 때
//        Hibernate: insert into team (name,team_id) values (?,default)
//        Hibernate: insert into chapter5_member (name,member_id) values (?,default)
//        Hibernate: insert into chapter5_member (name,member_id) values (?,default)
//        Hibernate: insert into team_members (chapter6_team_team_id,members_member_id) values (?,?)

        // joinColumn 주석처리 안했을 때
//        Hibernate: insert into team (name,team_id) values (?,default)
//        Hibernate: insert into chapter5_member (name,member_id) values (?,default)
//        Hibernate: insert into chapter5_member (name,member_id) values (?,default)
//        Hibernate: update chapter5_member set team_id=? where member_id=?
//        Hibernate: update chapter5_member set team_id=? where member_id=?
    }

    @Test
    fun solution2() = runInTx { em ->
        val member = Chapter6Member("홍길동")
        val locker = Chapter6Locker(name = "101번")

        em.persist(member)
        member.locker = locker
        em.persist(locker)
        em.flush()
        em.clear()

        val foundMember = em.find(Chapter6Member::class.java, member.id)
        println("==== Member 조회 끝=====")
        assertEquals(foundMember.locker?.name, "101번")

//        Hibernate: insert into chapter5_member (locker_id,name,member_id) values (?,?,default)
//        Hibernate: insert into locker (name,locker_id) values (?,default)
//        Hibernate: update chapter5_member set locker_id=?,name=? where member_id=?
//        Hibernate: select cm1_0.member_id,l1_0.locker_id,l1_0.name,cm1_0.name from chapter5_member cm1_0 left join locker l1_0 on l1_0.locker_id=cm1_0.locker_id where cm1_0.member_id=?
//        ==== Member 조회 끝=====

        // LAZY 로 변경한 뒤 실행
//        Hibernate: insert into chapter5_member (locker_id,name,member_id) values (?,?,default)
//        Hibernate: insert into locker (name,locker_id) values (?,default)
//        Hibernate: update chapter5_member set locker_id=?,name=? where member_id=?
//        ==== Member 조회 끝=====
//        Hibernate: select cl1_0.locker_id,cl1_0.name from locker cl1_0 where cl1_0.locker_id=?
    }

    @Test
    fun solution3() = runInTx { em ->
        val member = Chapter6Member("홍길동")
        val product = Chapter6Product("운동화")
        val order1 = Chapter6Order("주문1")
        val order2 = Chapter6Order("주문2")

        em.persist(member)
        em.persist(product)

        order1.member = member
        order1.product = product
        order1.count = 3
        order1.orderDate = LocalDate.of(2026, 1, 15)

        order2.member = member
        order2.product = product
        order2.count = 2
        order2.orderDate = LocalDate.of(2026, 1, 20)

        em.persist(order1)
        em.persist(order2)

        em.flush()
        em.clear()

        val orders = em.createQuery(
            "select o from Chapter6Order o order by o.orderDate",
            Chapter6Order::class.java
        ).resultList

        assertEquals(2, orders.size)
        assertEquals(3, orders[0].count)
        assertEquals(LocalDate.of(2026, 1, 15), orders[0].orderDate)
        assertEquals(2, orders[1].count)
        assertEquals(LocalDate.of(2026, 1, 20), orders[1].orderDate)
    }
}
