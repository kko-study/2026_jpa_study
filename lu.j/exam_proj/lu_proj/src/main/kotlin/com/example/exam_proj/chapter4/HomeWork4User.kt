package com.example.exam_proj.chapter4

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

@Entity
@Table(
    uniqueConstraints = [
        UniqueConstraint(
            name = "NAME_AGE_CONSTRAINT",
            columnNames = ["name", "age"]
        )
    ]
)
class HomeWork4User {
    // IDENTITY로 할 경우 persist 순간에 flush 가 실행되어 SEQUENCE 로 바꿔 flush 할 때 에러 나도록 함.
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Int = 0

    @Column(unique = true)
    var loginId: String = ""

    @Column(unique = true)
    var email: String = ""

    @Column(nullable = false)
    var name: String = ""

    @Column(nullable = false)
    // 제약조건 0 이상 150 이하
    @Min(0)
    @Max(150)
    var age: Int = 0
}