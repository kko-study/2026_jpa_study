package com.example.joonbug.chapter5

import jakarta.persistence.*

/**
 * 5장. 다대일 양방향 - Member 엔티티 (다 쪽, 연관관계 주인)
 */
@Entity
@Table(name = "chapter5_member")
class Member5(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var name: String = "",

    @ManyToOne
    @JoinColumn(name = "team_id")
    var team: Team? = null
) {
    /**
     * 무한루프 발생하는 toString (문제 2-1용)
     * Member.toStringWithLoop() -> Team.toStringWithLoop() -> Member.toStringWithLoop() -> ...
     */
    fun toStringWithLoop(): String {
        return "Member5(id=$id, name='$name', team=${team?.toStringWithLoop()})"
    }

    /**
     * 해결된 toString (문제 2-2용)
     * 연관 엔티티는 ID만 출력하거나 제외
     */
    override fun toString(): String {
        return "Member5(id=$id, name='$name', teamId=${team?.id})"
    }
}