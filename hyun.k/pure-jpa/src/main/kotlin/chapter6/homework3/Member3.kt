package chapter6.homework3

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity(name = "hw3_member")
@Table(name = "member_hw3_kt")
data class Member3(
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    val id: Long? = null,

    var name: String? = null,

    @OneToMany(mappedBy = "member")  // <- Order.member 필드를 가리킴 (읽기 전용)
    val orders: MutableList<Order> = mutableListOf()
)