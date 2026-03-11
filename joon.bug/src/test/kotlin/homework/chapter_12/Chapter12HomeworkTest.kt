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
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
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
        teamA = teamRepository.save(Team(name = "teamA"))
        teamB = teamRepository.save(Team(name = "teamB"))

        memberRepository.save(Member(name = "member1", age = 10, team = teamA))
        memberRepository.save(Member(name = "member2", age = 20, team = teamA))
        memberRepository.save(Member(name = "member3", age = 30, team = teamB))
        memberRepository.save(Member(name = "member4", age = 40, team = teamB))
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