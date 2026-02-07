package com.example.joonbug.chapter6.homework3

import jakarta.persistence.*

/**
 * 숙제 3: 다대다 중간 엔티티 - Member 엔티티
 */
@Entity(name = "Hw3Member")
@Table(name = "hw3_member")
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var name: String = "",

    @OneToMany(mappedBy = "member")
    var orders: MutableList<Order> = mutableListOf()
)