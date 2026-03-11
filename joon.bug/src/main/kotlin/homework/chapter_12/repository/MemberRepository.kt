package homework.chapter_12.repository

import homework.chapter_12.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * 12장 과제: Spring Data JPA Repository
 *
 * Q1: 메서드 이름만으로 쿼리를 생성하는 쿼리 메서드 3개를 작성하세요.
 * Q2: @Query 어노테이션으로 JPQL을 직접 작성하는 메서드 2개를 작성하세요.
 *
 * 테스트: Chapter12HomeworkTest 가 모두 통과하면 완료!
 */
interface MemberRepository : JpaRepository<Member, Long> {

    // ===== Q1. 쿼리 메서드 (메서드 이름으로 쿼리 생성) =====
    // 힌트: findBy + 필드명 + 조건키워드 (And, GreaterThan, StartingWith)

    // Q1-1: 이름이 정확히 일치하는 회원 목록 조회
    fun findByName(name: String): List<Member>

    // Q1-2: 이름이 일치하고, 나이가 특정 값 초과인 회원 목록
    fun findByNameAndAgeGreaterThan(name: String, age: Int): List<Member>

    // Q1-3: 이름이 특정 문자열로 시작하는 회원 목록
    fun findByNameStartingWith(prefix: String): List<Member>

    // ===== Q2. @Query를 활용한 JPQL 직접 작성 =====
    // 힌트 1: @Query("select m from Member m join m.team t where t.name = :teamName")
    // 힌트 2: LIKE — concat('%', :keyword, '%')

    // Q2-1: 특정 팀 이름에 소속된 회원 목록을 조회 (메서드명: findByTeamName)
    @Query("select m from Member m join m.team t where t.name = :teamName")
    fun findByTeamName(@Param("teamName") teamName: String): List<Member>

    // Q2-2: 이름에 특정 문자열이 포함된 회원 목록 조회 (메서드명: findByNameContaining)
    @Query("select m from Member m where m.name like concat('%', :keyword, '%')")
    fun findByNameContaining(@Param("keyword") keyword: String): List<Member>
}