package com.example.exam_proj.chapter4

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Lob
import jakarta.persistence.Table
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import jakarta.persistence.Transient
import java.util.Date

@Entity
@Table(name = "members")
class HomeWork4Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    @Column(nullable = false, length = 50, name = "user_name")
    var userName: String = ""

    @Column
    var age: Int? = null

    @Column(length = 100, unique = true)
    var email: String? = null

    @Enumerated(EnumType.STRING)
    var role: RoleType? = null

    @Temporal(TemporalType.DATE)
    var createdAt: Date? = null

    @Lob
    var description: String? = null

    @Transient
    var tempData: String? = null
}

enum class RoleType {
    ADMIN, USER
}