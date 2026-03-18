package chapter3

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity(name = "member")
data class Member(
    @Id
    val id: Long,
    var name: String


)