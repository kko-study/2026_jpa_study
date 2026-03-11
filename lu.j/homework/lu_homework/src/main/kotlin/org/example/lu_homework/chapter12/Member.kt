package org.example.lu_homework.chapter12

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
open class Member protected constructor() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(nullable = false)
    lateinit var name: String
        protected set

    var age: Int = 0
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    lateinit var team: Team
        protected set

    constructor(name: String, age: Int, team: Team) : this() {
        this.name = name
        this.age = age
        this.team = team
    }

    fun changeName(name: String) {
        this.name = name
    }
}
