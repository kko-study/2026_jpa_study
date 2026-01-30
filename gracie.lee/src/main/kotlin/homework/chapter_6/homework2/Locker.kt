package homework.chapter_6.homework2

import jakarta.persistence.*

@Entity(name = "Hw2LockerKt")
@Table(name = "locker_hw2_kt")
class Locker(
    @Id
    @GeneratedValue
    @Column(name = "locker_id")
    val id: Long? = null,

    var name: String? = null
)
