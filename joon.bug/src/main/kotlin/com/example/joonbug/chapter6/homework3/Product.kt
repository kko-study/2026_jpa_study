package com.example.joonbug.chapter6.homework3

import jakarta.persistence.*

/**
 * 숙제 3: 다대다 중간 엔티티 - Product 엔티티
 */
@Entity(name = "Hw3Product")
@Table(name = "hw3_product")
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var name: String = "",

    @OneToMany(mappedBy = "product")
    var orders: MutableList<Order> = mutableListOf()
)