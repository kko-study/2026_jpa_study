package homework.chapter_13;

import homework.chapter_12.entity.Member;
import homework.chapter_12.entity.Team;
import homework.chapter_12.repository.MemberRepository;
import homework.chapter_12.repository.TeamRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 13장 보너스 테스트 — OSIV=true에서 Lazy 로딩이 컨트롤러에서 동작하는지 확인.
 *
 * spring.jpa.open-in-view=true를 프로퍼티로 오버라이드합니다.
 * 기본 application.yml에서는 false로 설정되어 있지만,
 * 이 테스트에서만 true로 변경하여 OSIV의 효과를 관찰합니다.
 */
@SpringBootTest(
        classes = Chapter13Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.jpa.open-in-view=true"
)
@AutoConfigureMockMvc
class Chapter13BonusTest {

    @Autowired
    MockMvc mockMvc;

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

    @Test
    @DisplayName("보너스: OSIV=true → 컨트롤러에서 Lazy 로딩 정상 동작")
    void osivTrue_lazyLoadingInController() throws Exception {
        // MemberService.findMember()는 Team을 fetch join 하지 않음.
        // 하지만 OSIV=true이므로 컨트롤러에서 getTeam().getName() 호출 시에도
        // 영속성 컨텍스트가 살아있어서 Lazy 로딩이 정상 동작합니다.
        mockMvc.perform(get("/members/{id}", memberId))
                .andExpect(status().isOk())
                .andExpect(content().string("member1 - teamA"));
    }
}
