package com.example.exam_proj.chapter7

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest
class Chapter7SolutionTests @Autowired constructor(
    private val entityManagerFactory: EntityManagerFactory,
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
    fun chapter7Assignment() = runInTx { em ->
        val normalItem = NormalItem(
            name = "USB Cable",
            basePrice = 9900,
            sku = "USB-001",
            stockQuantity = 100,
        )
        val subscriptionItem = SubscriptionItem(
            name = "Music Sub",
            basePrice = 7900,
            billingCycleDays = 30,
            trialDays = 7,
        )
        em.persist(normalItem)
        em.persist(subscriptionItem)

        val order = Order(orderNo = "ORD-20260204-0001")
        val orderItem1 = OrderItem(
            itemNameSnapshot = normalItem.name,
            unitPriceSnapshot = normalItem.basePrice,
            quantity = 2,
        )
        orderItem1.item = normalItem
        orderItem1.options.add(OrderItemOption("color", "black"))
        orderItem1.options.add(OrderItemOption("size", "M"))
        orderItem1.options.add(OrderItemOption("giftWrap", "true"))

        val orderItem2 = OrderItem(
            itemNameSnapshot = subscriptionItem.name,
            unitPriceSnapshot = subscriptionItem.basePrice,
            quantity = 1,
        )
        orderItem2.item = subscriptionItem
        orderItem2.options.add(OrderItemOption("trial", "true"))

        order.addOrderItem(orderItem1)
        order.addOrderItem(orderItem2)
        em.persist(order)

        em.flush()
        em.clear()

        val baseEntityTableCount = em.createNativeQuery(
            "select count(*) from information_schema.tables where table_name = 'BASE_ENTITY'"
        ).singleResult as Number
        assertEquals(0, baseEntityTableCount.toInt())

        val orderColumns = em.createNativeQuery(
            "select column_name from information_schema.columns where table_name = 'CHAPTER7_ORDER'"
        ).resultList
        assertTrue(orderColumns.contains("CREATED_AT"))
        assertTrue(orderColumns.contains("UPDATED_AT"))

        val items = em.createQuery(
            "select i from Item i order by i.id",
            Item::class.java,
        ).resultList
        assertEquals(2, items.size)

        val foundOrder = em.find(Order::class.java, order.id)
        println("=== Order 조회 완료 ===")
        val orderItems = foundOrder.getOrderItems()
        println("=== OrderItem 접근 완료 ===")
        assertEquals(2, orderItems.size)
    }
}
