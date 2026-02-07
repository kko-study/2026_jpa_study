package com.example.exam_proj.chapter6

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "TEAM")
class Chapter6Team(
    @Column(name = "NAME")
    private var name: String? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEAM_ID")
    var id: Long = 0L

    @OneToMany
    @JoinColumn(name = "TEAM_ID")
    private var members: MutableList<Chapter6Member> = mutableListOf()

    fun getMembers(): MutableList<Chapter6Member> {
        return members
    }

    fun getName(): String? = name
}
