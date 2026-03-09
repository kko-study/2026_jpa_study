package homework.chapter_12

import homework.chapter_12.entity.Member
import homework.chapter_12.entity.Team
import homework.chapter_12.repository.MemberRepository
import homework.chapter_12.repository.TeamRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration

@DataJpaTest
@ContextConfiguration(classes = [Chapter12Application::class])
class Chapter12HomeworkTest {

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Autowired
    lateinit var teamRepository: TeamRepository

    lateinit var teamA: Team
    lateinit var teamB: Team

    @BeforeEach
    fun setUp() {
        teamA = teamRepository.save(Team("teamA"))
        teamB = teamRepository.save(Team("teamB"))

        memberRepository.save(Member("member1", 10, teamA))
        memberRepository.save(Member("member2", 20, teamA))
        memberRepository.save(Member("member3", 30, teamB))
        memberRepository.save(Member("member4", 40, teamB))
    }

    // ===== Q1. 쿼리 메서드 =====

    @Test
    @DisplayName("Q1-1: findByName — 이름으로 회원 조회")
    fun findByName() {
        val result = memberRepository.findByName("member1")

        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("member1")
    }

    @Test
    @DisplayName("Q1-2: findByNameAndAgeGreaterThan — 이름 + 나이 초과 조건")
    fun findByNameAndAgeGreaterThan() {
        // member1(age=10) 은 age > 5 이므로 포함
        val result = memberRepository.findByNameAndAgeGreaterThan("member1", 5)
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("member1")

        // member1(age=10) 은 age > 15 이므로 미포함
        val empty = memberRepository.findByNameAndAgeGreaterThan("member1", 15)
        assertThat(empty).isEmpty()
    }

    @Test
    @DisplayName("Q1-3: findByNameStartingWith — 이름 접두어로 조회")
    fun findByNameStartingWith() {
        val result = memberRepository.findByNameStartingWith("member")

        assertThat(result).hasSize(4)
    }

    // ===== Q2. @Query =====

    @Test
    @DisplayName("Q2-1: findByTeamName — 팀 이름으로 회원 조회 (@Query)")
    fun findByTeamName() {
        val result = memberRepository.findByTeamName("teamA")

        assertThat(result).hasSize(2)
        assertThat(result).extracting("name")
            .containsExactlyInAnyOrder("member1", "member2")
    }

    @Test
    @DisplayName("Q2-2: findByNameContaining — 이름에 키워드 포함 (@Query)")
    fun findByNameContaining() {
        val result = memberRepository.findByNameContaining("member")

        assertThat(result).hasSize(4)
    }
}