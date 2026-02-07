package com.study.jpa.chapter06.homework1

import jakarta.persistence.Persistence

/**
 * 보너스 3 내용과 연관?
 * 주석 있을 떄(다대다로 취급) - 조인 테이블이 생성됨 (insert 3개 + 조인 테이블 insert 2개)
 * - @OneToMany만 있고 @JoinColumn이 없으면, JPA는 다대다처럼 취급
 * 주석 없을 떄(일대다 단방향) - 외래 키가 Member 테이블에 생성됨 (insert 3개 + update 2개)
 * - 다대일이면 update 2개가 추가로 발생하지 않았을 것
 */
fun homework1() {
    val emf = Persistence.createEntityManagerFactory("chapter06-hw1")
    val em = emf.createEntityManager()
    val tx = em.transaction

    println("=== 과제 1: @JoinColumn 유무에 따른 일대다 단방향 동작 차이 ===")

    try {
        tx.begin()

        val team = Team(name = "개발팀")
        em.persist(team)

        val member1 = Member(name = "홍길동")
        val member2 = Member(name = "김철수")
        em.persist(member1)
        em.persist(member2)

        team.members.add(member1)
        team.members.add(member2)

        println("---- SQL 로그 확인 ----")
        em.flush()

        tx.commit()
    } catch (e: Exception) {
        if (tx.isActive) tx.rollback()
        println("예외 발생: ${e.message}")
        e.printStackTrace()
    }
    em.close()
    emf.close()
}

fun main() {
    homework1()
}
