package com.study.jpa.chapter04

import jakarta.persistence.*

@Entity
@Table(
    name = "users",
    uniqueConstraints = [
        UniqueConstraint(name = "uk_name_age", columnNames = ["name", "age"])
    ]
)
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(unique = true)
    var loginId: String? = null,

    @Column(unique = true)
    var email: String? = null,

    var name: String? = null,

    // CHECK 제약조건: age >= 0 AND age <= 150
    var age: Int? = null
)
