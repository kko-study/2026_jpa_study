package homework.chapter_6.homework2

import jakarta.persistence.*

@Entity(name = "Hw2MemberKt")
@Table(name = "member_hw2_kt")
class Member(
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    val id: Long? = null,

    var name: String? = null,

    @OneToOne  // <- fetch = FetchType.LAZY 추가해서 지연 로딩 확인!
    @JoinColumn(name = "locker_id")
    var locker: Locker? = null
)
