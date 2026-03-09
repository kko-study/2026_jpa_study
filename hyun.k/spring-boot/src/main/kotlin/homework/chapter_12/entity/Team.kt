package homework.chapter_12.entity

import jakarta.persistence.*

@Entity
class Team(
    @Column(nullable = false)
    val name: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
)