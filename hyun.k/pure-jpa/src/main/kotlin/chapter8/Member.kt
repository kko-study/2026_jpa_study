package chapter8

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity(name = "ch8_member")
class Member() {
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(length = 50, nullable = false)
    var username: String = ""

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    var team: Team? = null

    // > 주의: `toString()`, `equals()/hashCode()`에서 연관관계를 접근하지 않도록 주의
    override fun equals(other: Any?): Boolean {
        if (other !is Member) return false
        return id != null && id == other.id
    }

    override fun hashCode(): Int = id?.hashCode() ?: 0

    override fun toString(): String = "Member(id=$id, username=$username)"
}
