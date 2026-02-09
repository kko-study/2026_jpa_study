package com.example.joonbug.chpater8

import jakarta.persistence.*

@Entity
@Table(name = "ch8_team")
class Ch8Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    var name: String = ""

    // toString에서 연관관계 접근하지 않음
    override fun toString(): String {
        return "Team(id=$id, name='$name')"
    }

    // equals/hashCode에서 연관관계 접근하지 않음
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Ch8Team) return false
        return id != null && id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}

@Entity
@Table(name = "ch8_member")
class Ch8Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    var username: String = ""

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    var team: Ch8Team? = null

    // toString에서 team 접근하지 않음 (LAZY 프록시 초기화 방지)
    override fun toString(): String {
        return "Member(id=$id, username='$username')"
    }

    // equals/hashCode에서 연관관계 접근하지 않음
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Ch8Member) return false
        return id != null && id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}