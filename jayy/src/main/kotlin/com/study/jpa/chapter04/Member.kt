package com.study.jpa.chapter04

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "members")
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "user_name", length = 50, nullable = false)
    var username: String? = null,

    @Column(length = 100, unique = true)
    var email: String? = null,

    @Enumerated(EnumType.STRING)
    var role: Role? = null,

    @Temporal(TemporalType.DATE)
    var createdAt: LocalDate? = null,

    @Lob
    var description: String? = null,

    @Transient
    var tempData: String? = null
) {
    enum class Role {
        USER, ADMIN
    }
}
