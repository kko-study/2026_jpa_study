package chapter6.homework2

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity(name = "hw2_locker")
class Locker2(
    @Id
    @GeneratedValue
    @Column(name = "locker_id")
    var id: Long? = null,

    var name: String? = null
)
