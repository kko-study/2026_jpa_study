package homework.chapter_12.entity

import jakarta.persistence.*

@Entity
class Member(
    @Column(nullable = false)
    var name: String,

    val age: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    val team: Team? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
)