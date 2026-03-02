package homework.chapter_12;

import homework.chapter_12.entity.Member;
import homework.chapter_12.entity.Team;
import homework.chapter_12.repository.MemberRepository;
import homework.chapter_12.repository.TeamRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = Chapter12Application.class)
class Chapter12HomeworkTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    Team teamA;
    Team teamB;

    @BeforeEach
    void setUp() {
        teamA = teamRepository.save(new Team("teamA"));
        teamB = teamRepository.save(new Team("teamB"));

        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 20, teamA));
        memberRepository.save(new Member("member3", 30, teamB));
        memberRepository.save(new Member("member4", 40, teamB));
    }

    // ===== Q1. 쿼리 메서드 =====

    @Test
    @DisplayName("Q1-1: findByName — 이름으로 회원 조회")
    void findByName() {
        List<Member> result = memberRepository.findByName("member1");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("member1");
    }

    @Test
    @DisplayName("Q1-2: findByNameAndAgeGreaterThan — 이름 + 나이 초과 조건")
    void findByNameAndAgeGreaterThan() {
        // member1(age=10) 은 age > 5 이므로 포함
        List<Member> result = memberRepository.findByNameAndAgeGreaterThan("member1", 5);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("member1");

        // member1(age=10) 은 age > 15 이므로 미포함
        List<Member> empty = memberRepository.findByNameAndAgeGreaterThan("member1", 15);
        assertThat(empty).isEmpty();
    }

    @Test
    @DisplayName("Q1-3: findByNameStartingWith — 이름 접두어로 조회")
    void findByNameStartingWith() {
        List<Member> result = memberRepository.findByNameStartingWith("member");

        assertThat(result).hasSize(4);
    }

    // ===== Q2. @Query =====

    @Test
    @DisplayName("Q2-1: findByTeamName — 팀 이름으로 회원 조회 (@Query)")
    void findByTeamName() {
        List<Member> result = memberRepository.findByTeamName("teamA");

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Member::getName)
                .containsExactlyInAnyOrder("member1", "member2");
    }

    @Test
    @DisplayName("Q2-2: findByNameContaining — 이름에 키워드 포함 (@Query)")
    void findByNameContaining() {
        List<Member> result = memberRepository.findByNameContaining("member");

        assertThat(result).hasSize(4);
    }
}
