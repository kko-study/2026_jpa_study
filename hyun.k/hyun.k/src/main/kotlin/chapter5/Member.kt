package chapter5

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity(name = "member")
data class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null, // val로 만들면 위임이 불가능해짐.

    @Column(length = 50, nullable = false)
    val name: String,

    @ManyToOne
    @JoinColumn(name = "team_id")
    var team: Team? = null,

) {

    // kotlin에서는 setter가 자동 생성되기 때문에 setTeam으로 메서드명 지으면 JVM 시그니처에서 충돌남.
    fun applyTeam(team: Team) {
        if (this.team != null) {
            this.team!!.members.remove(this)
        }

        this.team = team
        team.members.add(this)
    }
}
