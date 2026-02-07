package com.study.jpa.chapter05

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class Team(
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "team_id")
    var id: Long? = null,

    val name: String,

    @OneToMany(mappedBy = "team")
    var members: MutableList<Member> = mutableListOf()
) {
    /** 순환참조로 무한루프 발생 */
//    override fun toString(): String {
//        return "Team(id=$id, name='$name', member ids=$members)"
//    }

    override fun toString(): String {
        return "Team(id=$id, name='$name', member ids=${members.map { it.id }})"
    }
}
