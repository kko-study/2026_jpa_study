package chapter6.homework3

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity(name = "Hw3OrderKt")
@Table(name = "orders_hw3_kt")
data class Order(
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "member_id")
    var member: Member3? = null,  // 다대다를 중간 엔티티로 풀기: Member -> Order

    @ManyToOne
    @JoinColumn(name = "product_id")
    var product: Product? = null,  // 다대다를 중간 엔티티로 풀기: Product -> Order

    var count: Int = 0,              // 주문 수량

    var orderDate: LocalDateTime? = null  // 주문 일자
)