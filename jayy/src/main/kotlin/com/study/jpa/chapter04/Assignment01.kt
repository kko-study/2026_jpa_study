package com.study.jpa.chapter04

import jakarta.persistence.Persistence
import java.time.LocalDate

fun assignment01() {
    val emf = Persistence.createEntityManagerFactory("chapter04")
    val em = emf.createEntityManager()
    val tx = em.transaction

    try {
        tx.begin()

        println("=== 과제 1번: Member 엔티티 테스트 ===")

        // Member 생성 및 저장
        val member = Member(
            username = "홍길동",
            email = "hong@example.com",
            role = Member.Role.ADMIN,
            createdAt = LocalDate.now(),
            description = "이것은 긴 설명 텍스트입니다. CLOB 타입으로 저장됩니다.",
            tempData = "이 데이터는 DB에 저장되지 않습니다"
        )

        tx.commit()
    } catch (e: Exception) {
        if (tx.isActive) tx.rollback()
        println("예외 발생: ${e.message}")
        e.printStackTrace()
    } finally {
        em.close()
    }

    emf.close()
}

fun main() = assignment01()
