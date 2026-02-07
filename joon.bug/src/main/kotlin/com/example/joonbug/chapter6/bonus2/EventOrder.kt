package com.example.joonbug.chapter6.bonus2

import com.example.joonbug.chapter6.homework3.Member
import com.example.joonbug.chapter6.homework3.Product
import jakarta.persistence.*
import java.time.LocalDate

/**
 * 보너스 2: 복합 키로 1인 1회 구매 제약 구현
 *
 * (회원, 상품)을 복합 키로 사용하여 같은 회원이 같은 상품을
 * 두 번 구매할 수 없도록 비즈니스 제약 구현
 */
@Entity(name = "HwBonus2EventOrder")
@Table(name = "hw_bonus2_event_order")
@IdClass(EventOrderId::class)
class EventOrder(
    @Id
    @ManyToOne
    @JoinColumn(name = "member_id")
    var member: Member? = null,

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id")
    var product: Product? = null,

    var count: Int = 0,

    var orderDate: LocalDate? = null
)