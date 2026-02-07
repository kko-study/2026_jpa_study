package com.example.joonbug.chapter5

import jakarta.persistence.*

/**
 * 5장. 다대일 양방향 - Team 엔티티 (일 쪽, 연관관계 주인 X)
 */
@Entity
@Table(name = "chapter5_team")
class Team(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var name: String = "",

    @OneToMany(mappedBy = "team")
    var members: MutableList<Member5> = mutableListOf()
) {
    /**
     * 무한루프 발생하는 toString (문제 2-1용)
     * Team.toStringWithLoop() -> Member.toStringWithLoop() -> Team.toStringWithLoop() -> ...
     */
    fun toStringWithLoop(): String {
        return "Team(id=$id, name='$name', members=${members.map { it.toStringWithLoop() }})"
    }

    /**
     * 해결된 toString (문제 2-2용)
     * 연관 엔티티는 ID만 출력하거나 제외
     */
    override fun toString(): String {
        return "Team(id=$id, name='$name', memberIds=${members.map { it.id }})"
    }
}