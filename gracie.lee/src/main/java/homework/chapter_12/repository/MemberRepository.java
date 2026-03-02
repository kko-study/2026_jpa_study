package homework.chapter_12.repository;

import homework.chapter_12.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 12장 과제: Spring Data JPA Repository
 *
 * Q1 (쿼리 메서드): 메서드 이름만으로 쿼리가 자동 생성됩니다.
 *     아래 3개 메서드가 어떻게 동작하는지 확인하세요.
 *
 * Q2 (@Query): 아래 2개 메서드에 @Query 어노테이션으로 JPQL을 직접 작성하세요.
 *     현재는 메서드 이름 기반 파싱으로도 동작하지만,
 *     실무에서 복잡한 쿼리를 작성할 때는 @Query를 사용합니다.
 *     직접 JPQL을 작성하는 연습을 해보세요!
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

    // ===== Q1. 쿼리 메서드 (메서드 이름으로 쿼리 생성) =====
    // 아래 메서드들은 선언만으로 Spring Data JPA가 자동으로 쿼리를 만들어줍니다.
    // 메서드 이름의 규칙을 관찰하세요: findBy + 필드명 + 조건키워드

    /** Q1-1: 이름이 정확히 일치하는 회원 목록 조회 */
    List<Member> findByName(String name);

    /** Q1-2: 이름이 일치하고, 나이가 특정 값 초과인 회원 목록 */
    List<Member> findByNameAndAgeGreaterThan(String name, int age);

    /** Q1-3: 이름이 특정 문자열로 시작하는 회원 목록 */
    List<Member> findByNameStartingWith(String prefix);

    // ===== Q2. @Query를 활용한 JPQL 직접 작성 =====
    // TODO: 아래 두 메서드에 @Query 어노테이션을 추가하여 JPQL을 직접 작성하세요!
    //
    // 힌트 1: @Query("select m from Member m join m.team t where t.name = :teamName")
    // 힌트 2: LIKE 검색 시 %를 concat으로 연결 — "select m from Member m where m.name like concat('%', :keyword, '%')"

    /** Q2-1: 특정 팀 이름에 소속된 회원 목록을 조회 */
    List<Member> findByTeamName(String teamName);

    /** Q2-2: 이름에 특정 문자열이 포함된 회원 목록 조회 */
    List<Member> findByNameContaining(String keyword);
}
