package homework.chapter_12.repository;

import homework.chapter_12.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // ===== Q1. 쿼리 메서드 =====

    List<Member> findByName(String name);

    List<Member> findByNameAndAgeGreaterThan(String name, int age);

    List<Member> findByNameStartingWith(String prefix);

    // ===== Q2. @Query =====

    @Query("select m from Member m join m.team t where t.name = :teamName")
    List<Member> findByTeamName(@Param("teamName") String teamName);

    @Query("select m from Member m where m.name like concat('%', :keyword, '%')")
    List<Member> findByNameContaining(@Param("keyword") String keyword);
}
