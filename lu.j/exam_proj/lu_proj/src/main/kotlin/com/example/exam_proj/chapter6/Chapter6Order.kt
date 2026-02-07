package com.example.exam_proj.chapter6

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "CHAPTER6_ORDER")
class Chapter6Order(
    @Column(name = "NAME")
    var name: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    var id: Long = 0L

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    var member: Chapter6Member? = null

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    var product: Chapter6Product? = null

    @Column(name = "ORDER_COUNT")
    var count: Int = 0

    @Column(name = "ORDER_DATE")
    var orderDate: LocalDate? = null
}
