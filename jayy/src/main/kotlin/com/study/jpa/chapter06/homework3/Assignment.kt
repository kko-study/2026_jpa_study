package com.study.jpa.chapter06.homework3

import jakarta.persistence.Persistence
import java.time.LocalDateTime

fun homework3() {
    val emf = Persistence.createEntityManagerFactory("chapter06-hw3")
    val em = emf.createEntityManager()
    val tx = em.transaction

    println("=== 과제 3: 다대다 관계를 중간 엔티티로 풀기 ===")

    try {
        tx.begin()

        val member = Member(name = "홍길동")
        em.persist(member)

        val product = Product(name = "운동화")
        em.persist(product)

        val order1 = Order(
            member = member,
            product = product,
            count = 3,
            orderDate = LocalDateTime.of(2025, 1, 15, 10, 0)
        )
        em.persist(order1)

        val order2 = Order(
            member = member,
            product = product,
            count = 2,
            orderDate = LocalDateTime.of(2025, 1, 20, 14, 30)
        )
        em.persist(order2)

        em.flush()
        em.clear()

        val orders = em.createQuery("SELECT o FROM Hw3OrderKt o", Order::class.java).resultList

        println("\n=== 주문 목록 (총 ${orders.size}건) ===")
        orders.forEach { order ->
            println("주문ID: ${order.id}, 회원: ${order.member?.name}, 상품: ${order.product?.name}, 수량: ${order.count}, 일자: ${order.orderDate}")
        }

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
    homework3()
}
