package homework.chapter_12.repository

import homework.chapter_12.entity.Team
import org.springframework.data.jpa.repository.JpaRepository

interface TeamRepository : JpaRepository<Team, Long>