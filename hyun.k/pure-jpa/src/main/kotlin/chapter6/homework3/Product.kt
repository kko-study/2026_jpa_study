package chapter6.homework3

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity(name = "Hw3ProductKt")
@Table(name = "product_hw3_kt")
data class Product(
    @Id
    @GeneratedValue
    @Column(name = "product_id")
    val id: Long? = null,

    var name: String? = null
)