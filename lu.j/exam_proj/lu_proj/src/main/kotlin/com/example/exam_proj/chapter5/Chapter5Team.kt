package com.example.exam_proj.chapter5

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "TEAM")
class Chapter5Team(
    @Column(name = "NAME")
    private var name: String? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEAM_ID")
    var id: Long = 0L

    @OneToMany(mappedBy = "team")
    private var members: MutableList<Chapter5Member> = mutableListOf()

    fun getMembers(): MutableList<Chapter5Member> {
        return members
    }

    fun getName(): String? = name

    override fun toString(): String {
        return "Chapter5Team(members=$members)"
    }
}
