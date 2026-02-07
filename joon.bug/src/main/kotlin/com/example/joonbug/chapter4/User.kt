package com.example.joonbug.chapter4

import jakarta.persistence.*

/**
 * 4장. 엔티티 매핑 - DDL 제약조건 테스트용 User 엔티티
 *
 * CHECK 제약조건 (DDL):
 * ALTER TABLE users ADD CONSTRAINT chk_age CHECK (age >= 0 AND age <= 150)
 */
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
    var loginId: String = "",

    @Column(unique = true)
    var email: String? = null,

    @Column
    var name: String? = null,

    // CHECK 제약조건: age >= 0 AND age <= 150 (DDL로 별도 추가 필요)
    @Column
    var age: Int? = null
)