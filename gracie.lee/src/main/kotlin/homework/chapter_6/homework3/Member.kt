package homework.chapter_6.homework3

import jakarta.persistence.*

@Entity(name = "Hw3MemberKt")
@Table(name = "member_hw3_kt")
class Member(
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    val id: Long? = null,

    var name: String? = null,

    @OneToMany(mappedBy = "member")  // <- Order.member 필드를 가리킴 (읽기 전용)
    val orders: MutableList<Order> = mutableListOf()
)
