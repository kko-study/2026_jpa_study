package com.study.jpa.chapter06.homework1

import jakarta.persistence.*

@Entity(name = "Hw1TeamKt")
@Table(name = "team_hw1_kt")
class Team(
    @Id
    @GeneratedValue
    @Column(name = "team_id")
    val id: Long? = null,

    var name: String? = null,

    @OneToMany
    // @JoinColumn(name = "team_id")  <- 주석 해제/처리해서 SQL 차이 확인!
    val members: MutableList<Member> = mutableListOf()
)
