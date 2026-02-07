package com.study.jpa.chapter06.homework3

import jakarta.persistence.*

@Entity(name = "Hw3ProductKt")
@Table(name = "product_hw3_kt")
class Product(
    @Id
    @GeneratedValue
    @Column(name = "product_id")
    val id: Long? = null,

    var name: String? = null
)
