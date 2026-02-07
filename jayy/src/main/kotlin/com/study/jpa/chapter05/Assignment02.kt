package com.study.jpa.chapter05

import jakarta.persistence.Persistence

fun assignment0201() {
    val emf = Persistence.createEntityManagerFactory("chapter05")
    val em = emf.createEntityManager()
    val tx = em.transaction

    println("=== 과제 2-1: 양방향 toString() 무한루프 확인 ===")

    try {
        tx.begin()

        val team = Team(name = "개발팀")
        em.persist(team)

        val member = Member(name = "홍길동")
        member.team = team
        team.members.add(member)
        em.persist(member)

        em.flush()
        em.clear()

        val foundMember = em.find(Member::class.java, member.id)

        // 여기서 StackOverflowError 발생!
        println(foundMember.toString())
    } catch (e: StackOverflowError) {
        println("StackOverflowError 발생! 양방향 toString() 무한루프")
    } catch (e: Exception) {
        if (tx.isActive) tx.rollback()
        println("예외 발생: ${e.message}")
        e.printStackTrace()
    }
    em.close()
    emf.close()
}

fun main() {
    assignment0201()
}
