package com.study.jpa.chapter05

import jakarta.persistence.Persistence

fun assignment0101() {
    val emf = Persistence.createEntityManagerFactory("chapter05")
    val em = emf.createEntityManager()
    val tx = em.transaction

    println("=== 과제 1-1: 연관관계 주인 쪽에서 FK를 설정하면 DB에 저장되는지 확인 ===")

    try {
        tx.begin()

        val teamA = Team(name = "개발팀")
        em.persist(teamA)

        val member1 = Member(name = "홍길동")
        member1.team = teamA
        em.persist(member1)

        em.flush()
        em.clear()

        val foundMember = em.find(Member::class.java, member1.id)

        println("소속 팀: ${foundMember.team?.name}")
    } catch (e: Exception) {
        if (tx.isActive) tx.rollback()
        println("예외 발생: ${e.message}")
        e.printStackTrace()
    }
    em.close()
    emf.close()
}

fun assignment0102() {
    val emf = Persistence.createEntityManagerFactory("chapter05")
    val em = emf.createEntityManager()
    val tx = em.transaction

    println("=== 과제 1-2: 연관관계 변경이 DB에 반영되는지 확인 ===")

    try {
        tx.begin()

        val teamDev = Team(name = "개발팀")
        val teamProduct = Team(name = "기획팀")
        em.persist(teamDev)
        em.persist(teamProduct)

        val member = Member(name = "홍길동")
        member.team = teamDev
        em.persist(member)

        em.flush()
        em.clear()

        val foundMember = em.find(Member::class.java, member.id)
        foundMember.team = teamProduct

        em.flush()
        em.clear()

        val updatedMember = em.find(Member::class.java, member.id)

        println("변경된 소속 팀: ${updatedMember.team?.name}")
    } catch (e: Exception) {
        if (tx.isActive) tx.rollback()
        println("예외 발생: ${e.message}")
        e.printStackTrace()
    }
    em.close()
    emf.close()
}

fun assignment0103() {
    val emf = Persistence.createEntityManagerFactory("chapter05")
    val em = emf.createEntityManager()
    val tx = em.transaction

    println("=== 과제 1-3: 주인이 아닌 쪽에서만 설정하면 DB에 반영 안 됨 ===")

    try {
        tx.begin()

        val team = Team(name = "개발팀")
        em.persist(team)

        val member = Member(name = "홍길동")
        em.persist(member)

        // 주인이 아닌 쪽에서만 설정 (member.team은 설정하지 않음)
        team.members.add(member)

        em.flush()
        em.clear()

        val foundMember = em.find(Member::class.java, member.id)

        println("소속 팀: ${foundMember.team?.name}")  // null이어야 함
    } catch (e: Exception) {
        if (tx.isActive) tx.rollback()
        println("예외 발생: ${e.message}")
        e.printStackTrace()
    }
    em.close()
    emf.close()
}

fun assignment0104() {
    val emf = Persistence.createEntityManagerFactory("chapter05")
    val em = emf.createEntityManager()
    val tx = em.transaction

    println("=== 과제 1-4: 양쪽 다 설정해야 하는 이유 (1차 캐시 동기화) ===")

    try {
        tx.begin()

        val team = Team(name = "개발팀")
        em.persist(team)

        val member = Member(name = "홍길동")
        member.team = team  // 주인 쪽만 설정
        em.persist(member)

        // flush 전: 1차 캐시의 team.members는 비어있음
        println("flush 전 team.members.size: ${team.members.size}")

        em.flush()
        em.clear()

        val foundTeam = em.find(Team::class.java, team.id)

        // flush/clear 후: DB에서 다시 조회하면 연관관계 반영됨
        println("flush/clear 후 team.members.size: ${foundTeam.members.size}")
    } catch (e: Exception) {
        if (tx.isActive) tx.rollback()
        println("예외 발생: ${e.message}")
        e.printStackTrace()
    }
    em.close()
    emf.close()
}

fun main() {
//    assignment0101()
//    assignment0102()
//    assignment0103()
    assignment0104()
}
