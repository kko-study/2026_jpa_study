package org.example.lu_homework.chapter12

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
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
        assertThat(result.map { it.name })
            .containsExactlyInAnyOrder("member1", "member2")
    }

    @Test
    @DisplayName("Q2-2: findByNameContaining — 이름에 키워드 포함 (@Query)")
    fun findByNameContaining() {
        val result = memberRepository.findByNameContaining("member")

        assertThat(result).hasSize(4)
    }

    /**
     * 생각해볼 것
     *
     * Q1. 쿼리 메서드 vs @Query 실무에서 어떤 기준으로 선택할까?
     * - 쿼리 메서드 이름이 길어지면 가독성이 어떻게 되는가?
     * - 예) findByNameAndAgeGreaterThanAndTeamNameOrderByAgeDesc 괜찮은가?
     *
     * 답.
     * 쿼리 메서드랑 @Query 의 선택 기준은 쿼리 메서드는 메서드 이름만 보고도 어떤 조회인지 알 수 있어서 장점이 있음
     * 그러나, 너무 긴 메서드 이름은 가독성이 떨어지게 되므로 조건이 단순한 조회는 쿼리 메서드, 조인이 들어가고 조건이 길어지면 @Query 를 쓰는게 좋다 생각
     * @Query 보다도 QueryDsl 을 쓰는 편이 더 좋을 때도 있을거라 생각함
     *
     *
     * Q2. Spring Data JPA 가 인터페이스 만으로 동작하는 원리는?
     * - 구현 클래스를 안 만들었는데 어떻게 실행되는가?
     * - 힌트 : 프록시, SimpleJpaRepository
     *
     * 답.
     * Spring Data 가 런타임 시점에 프록시 객체를 생성해서 실제 구현을 대신 해주고 있기 때문임
     * 그리고 프록시 객체의 기본 구현체가 SimpleJpaRepository 이다.
     *
     * 예를 들어 MemberRepository extends JpaRepository<Member, Long> 을 선언하면
     * 내부적으로 SimpleJpaRepository<Member, Long> 이 생성되고 save, findById, findAll 같은 CRUD 메서드는 이 클래스의 구현을 사용한다.
     *
     * 쿼리 메서드를 쓴 경우엔 메서드의 이름을 분석해서 JPQL 로 생성하고 실행하게 된다.
     *
     *
     * [같이 이야기 하고 싶은 내용]
     * - Pageable 은 좋아보인다. 쓰고 싶음
     * - 운영툴에서 먼저 써보면 좋지 않을까? CRUD 를 다 해볼 수 있을거 같음.
     * - 명세(Specification) 를 쓸까? 실무에서는 복잡도만 올릴 뿐 QueryDsl 을 쓰지 않을까? 싶은데 어떻게 생각하는지.
     */
}
