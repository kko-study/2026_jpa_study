package com.study.jpa.chapter06.homework2

import jakarta.persistence.Persistence

/**
 * lazy loading 확인
 * - EAGER으로 설정했는데 로직상 조회할 필요가 없었다면 낭비 일수도
 * - Locker 프록시 객체가 먼저 만들어지고, 실제 Locker 데이터를 사용하는 시점에 select 쿼리 실행
 * 보너스 1 내용과 연관
 * - 반대의 경우, locker 조회 시 member 존재의 여부와 id를 알 수 없음.
 * - 그런데 proxy 객체를 만들려면 최소한 id는 알아야 하기 때문에
 * - 그래서 테이블을 직접 조회하고 즉시 로딩하게 됨
 */
fun homework2() {
    val emf = Persistence.createEntityManagerFactory("chapter06-hw2")
    val em = emf.createEntityManager()
    val tx = em.transaction

    println("=== 과제 2: @OneToOne 지연 로딩 확인 ===")

    try {
        tx.begin()

        val locker = Locker(name = "101번")
        em.persist(locker)

        val member = Member(name = "홍길동")
        member.locker = locker
        em.persist(member)

        em.flush()
        em.clear()

        val foundMember = em.find(Member::class.java, member.id)

        // Locker 접근 시점에 SELECT 발생하는지 확인
        println("Locker 이름: ${foundMember.locker?.name}")

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
    homework2()
}
