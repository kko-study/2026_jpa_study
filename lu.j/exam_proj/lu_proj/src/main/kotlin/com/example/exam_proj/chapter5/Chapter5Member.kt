package com.example.exam_proj.chapter5

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "CHAPTER5_MEMBER")
class Chapter5Member(
    @Column(name = "NAME")
    private var name: String? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    var id: Long = 0L

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private var team: Chapter5Team? = null

    fun setTeam(team: Chapter5Team?) {
        this.team = team
    }

    fun getTeam(): Chapter5Team? = team

    override fun toString(): String {
        return "Chapter5Member(team=$team)"
    }
}
