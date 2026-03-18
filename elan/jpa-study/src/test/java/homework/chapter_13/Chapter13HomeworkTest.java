package homework.chapter_13;

import homework.chapter_12.entity.Member;
import homework.chapter_12.entity.Team;
import homework.chapter_12.repository.MemberRepository;
import homework.chapter_12.repository.TeamRepository;
import homework.chapter_13.service.MemberService;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 13장 과제 테스트.
 *
 * @SpringBootTest 를 사용합니다 (@DataJpaTest가 아님).
 * @DataJpaTest는 각 테스트를 @Transactional로 감싸므로 트랜잭션 경계를 관찰할 수 없습니다.
 * @SpringBootTest는 트랜잭션을 자동으로 감싸지 않으므로 Service의 트랜잭션 경계를 그대로 관찰합니다.
 */
@SpringBootTest(classes = Chapter13Application.class)
class Chapter13HomeworkTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    Long memberId;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        teamRepository.deleteAll();

        Team teamA = teamRepository.save(new Team("teamA"));
        Member member = memberRepository.save(new Member("member1", 20, teamA));
        memberId = member.getId();
    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
        teamRepository.deleteAll();
    }

    // ===== Scenario A: 변경 감지 (Dirty Checking) =====

    @Test
    @DisplayName("A-1: @Transactional O → save() 없이도 UPDATE 반영")
    void changeName_withTransaction() {
        memberService.changeName(memberId, "updatedName");

        Member found = memberRepository.findById(memberId).orElseThrow();
        assertThat(found.getName()).isEqualTo("updatedName");
    }

    @Test
    @DisplayName("A-2: @Transactional X → UPDATE 미반영")
    void changeName_withoutTransaction() {
        memberService.changeNameWithoutTransaction(memberId, "updatedName");

        Member found = memberRepository.findById(memberId).orElseThrow();
        assertThat(found.getName()).isEqualTo("member1"); // 변경되지 않음
    }

    // ===== Scenario B: 트랜잭션 밖 지연 로딩 =====

    @Test
    @DisplayName("B-1: findMember → 트랜잭션 밖에서 Lazy 접근 시 LazyInitializationException")
    void findMember_lazyInitException() {
        Member member = memberService.findMember(memberId);

        // 서비스 메서드의 @Transactional 이 끝났으므로 영속성 컨텍스트 종료.
        // 이 시점에서 team 프록시를 초기화하면 예외 발생.
        assertThatThrownBy(() -> member.getTeam().getName())
                .isInstanceOf(LazyInitializationException.class);
    }

    @Test
    @DisplayName("B-2: findMemberWithTeam → fetch join으로 Lazy 문제 해결")
    void findMemberWithTeam_fetchJoin() {
        Member member = memberService.findMemberWithTeam(memberId);

        // fetch join 으로 Team을 함께 가져왔으므로 트랜잭션 밖에서도 정상 동작
        assertThat(member.getTeam().getName()).isEqualTo("teamA");
    }
}
