package com.example.joonbug.chapter6.homework3

import jakarta.persistence.*
import java.time.LocalDate

/**
 * 숙제 3: 다대다 중간 엔티티 - Order 엔티티
 *
 * Member와 Product의 다대다 관계를 풀어낸 중간 엔티티
 * 추가 속성: count(수량), orderDate(주문일)
 *
 * 같은 회원이 같은 상품을 여러 번 주문 가능 (대리 키 사용)
 */
@Entity(name = "Hw3Order")
@Table(name = "hw3_order")
class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "member_id")
    var member: Member? = null,

    @ManyToOne
    @JoinColumn(name = "product_id")
    var product: Product? = null,

    var count: Int = 0,

    var orderDate: LocalDate? = null
)