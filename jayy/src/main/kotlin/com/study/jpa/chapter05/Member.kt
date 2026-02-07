package com.study.jpa.chapter05

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    val name: String,

    @ManyToOne
    @JoinColumn(name = "team_id")
    var team: Team? = null,
) {
    override fun toString(): String {
        return "Member(id=$id, name='$name', team=$team)"
    }
}
