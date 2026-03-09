package chapter7

import common.JpaUtil
import jakarta.persistence.EntityManager

fun main() {
    JpaUtil.executeInTransaction { em -> scenario(em) }
}

private fun scenario(em: EntityManager) {
    // ### 3-1. 데이터 준비
    val normalItem = NormalItem().apply {
        name = "USB Cable"
        basePrice = 9900
        sku = "USB-001"
        stockQuantity = 100
    }

    val subscriptionItem = SubscriptionItem().apply {
        name = "Music Sub"
        basePrice = 7900
        billingCycleDays = 30
        trialDays = 7
    }

    val order = Order().apply {
        orderNo = "ORD-20260204-0001"
    }

    val orderItem1 = OrderItem().apply {
        item = normalItem
        itemNameSnapshot = normalItem.name!!
        unitPriceSnapshot = normalItem.basePrice
        quantity = 2
        this.order = order
    }
    val option1 = OrderItemOption().apply {
        orderItem = orderItem1
        key = "color"
        value = "black"
    }
    val option2 = OrderItemOption().apply {
        orderItem = orderItem1
        key = "size"
        value = "M"
    }
    orderItem1.options.addAll(listOf(option1, option2))

    val orderItem2 = OrderItem().apply {
        item = subscriptionItem
        itemNameSnapshot = subscriptionItem.name!!
        unitPriceSnapshot = subscriptionItem.basePrice
        quantity = 1
        this.order = order
    }
    val option3 = OrderItemOption().apply {
        orderItem = orderItem2
        key = "trial"
        value = "true"
    }
    orderItem2.options.add(option3)

    em.persist(normalItem)
    em.persist(subscriptionItem)
    em.persist(order)
    em.persist(orderItem1)
    em.persist(orderItem2)
    em.persist(option1)
    em.persist(option2)
    em.persist(option3)

    // ### 3-2. 데이터 조회 및 검증
    em.flush()
    em.clear()
    // 제출/ddl.sql 파일에 저장.

    // 3-3. 검증 2: 부모 타입(Item) 기준 다형성 조회 SQL 확인
    println("========= 3-3 시작 =========")
    val items: List<Item> = em.createQuery("select i from Item i order by i.id", Item::class.java)
        .resultList
    println("========= 3-3 종료 =========")

    // 3-4. 검증 3: 주문 1건 조회 시 OrderItem / Item 조회 방식 확인
    em.clear()
    println("========= 3-4 시작 =========")
    println("========= Order 조회 =========")
    val findOrder = em.find(Order::class.java, order.id!!)
    println("========= getOrderItems 접근 =========")
    val orderItems = findOrder.orderItems
    orderItems.forEach { oi ->
        println("========= getItem 접근 =========")
        val item = oi.item
    }
    println("========= 3-4 종료 =========")
}