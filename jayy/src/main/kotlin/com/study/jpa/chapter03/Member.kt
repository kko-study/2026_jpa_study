package com.study.jpa.chapter03

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "MEMBER")
class Member(
    @Id
    @Column(name = "ID")
    val id: Long,

    @Column(name = "NAME")
    var name: String,
)
