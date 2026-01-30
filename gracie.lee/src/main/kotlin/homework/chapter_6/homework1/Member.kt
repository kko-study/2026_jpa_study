package homework.chapter_6.homework1

import jakarta.persistence.*

@Entity(name = "Hw1MemberKt")
@Table(name = "member_hw1_kt")
class Member(
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    val id: Long? = null,

    var name: String? = null
)
// 일대다 단방향: Member는 Team을 참조하지 않음!
// (Team -> Member 방향만 존재)
