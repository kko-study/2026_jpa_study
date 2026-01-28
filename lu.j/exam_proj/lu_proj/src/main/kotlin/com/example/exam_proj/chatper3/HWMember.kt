package com.example.exam_proj.chatper3

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "HW_MEMBER")
class HWMember(
    @Id
    @Column(name = "ID")
    var id: Long = 0L,

    @Column(name = "NAME")
    var name: String? = null,
)
