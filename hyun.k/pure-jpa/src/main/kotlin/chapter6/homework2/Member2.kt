package chapter6.homework2

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne

@Entity(name = "hw2_member")
class Member2(
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    var id: Long? = null,

    var name: String? = null,

    // @OneToOne  // <- fetch = FetchType.LAZY 추가해서 지연 로딩 확인!
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locker_id")
    var locker2: Locker2? = null
)