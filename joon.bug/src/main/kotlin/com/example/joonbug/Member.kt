package com.example.joonbug

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "MEMBER")
class Member(
    @Id
    @Column(name = "ID")
    var id: String = "",

    @Column(name = "NAME")
    var name: String? = null,

    @Column(name = "AGE")
    var age: Int? = null
)