package com.example.joonbug.chapter4

import jakarta.persistence.*
import java.time.LocalDate

enum class RoleType {
    USER, ADMIN
}

/**
 * 4장. 엔티티 매핑 - Member 엔티티
 */
@Entity
@Table(name = "members")
class Member4(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "user_name", length = 50, nullable = false)
    var userName: String = "",

    @Column
    var age: Int? = null,

    @Column(length = 100, unique = true)
    var email: String? = null,

    @Enumerated(EnumType.STRING)
    var role: RoleType? = null,

    // LocalDate는 자동으로 DATE 타입 매핑 (@Temporal 불필요)
    var createdAt: LocalDate? = null,

    @Lob
    var description: String? = null,

    @Transient
    var tempData: String? = null
)