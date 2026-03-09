package chapter6.homework1

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany

@Entity(name = "team")
data class Team(

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column
    val name: String? = null,

    @OneToMany
    @JoinColumn(name = "team_id")
    val members: MutableList<Member> = mutableListOf(),
)
