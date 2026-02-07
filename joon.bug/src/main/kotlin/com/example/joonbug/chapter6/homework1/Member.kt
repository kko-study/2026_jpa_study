package com.example.joonbug.chapter6.homework1

import jakarta.persistence.*

/**
 * 숙제 1: 일대다 단방향 - Member 엔티티
 *
 * 일대다 단방향이므로 Member는 Team을 참조하지 않음
 */
@Entity(name = "Hw1Member")
@Table(name = "hw1_member")
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var name: String = ""
)