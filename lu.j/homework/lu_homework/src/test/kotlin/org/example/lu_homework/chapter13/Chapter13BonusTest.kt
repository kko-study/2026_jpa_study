package org.example.lu_homework.chapter13

import org.example.lu_homework.chapter12.Member
import org.example.lu_homework.chapter12.MemberRepository
import org.example.lu_homework.chapter12.Team
import org.example.lu_homework.chapter12.TeamRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

/**
 * 13장 보너스 테스트 — OSIV=true에서 Lazy 로딩이 컨트롤러에서 동작하는지 확인.
 *
 * spring.jpa.open-in-view=true를 프로퍼티로 오버라이드합니다.
 * 기본 application.yml에서는 false로 설정되어 있지만,
 * 이 테스트에서만 true로 변경하여 OSIV의 효과를 관찰합니다.
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["spring.jpa.open-in-view=true"]
)
@AutoConfigureMockMvc
class Chapter13BonusTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Autowired
    lateinit var teamRepository: TeamRepository

    var memberId: Long? = null

    @BeforeEach
    fun setUp() {
        memberRepository.deleteAll()
        teamRepository.deleteAll()

        val teamA = teamRepository.save(Team("teamA"))
        val member = memberRepository.save(Member("member1", 20, teamA))
        memberId = member.id
    }

    @AfterEach
    fun tearDown() {
        memberRepository.deleteAll()
        teamRepository.deleteAll()
    }

    @Test
    @DisplayName("보너스: OSIV=true → 컨트롤러에서 Lazy 로딩 정상 동작")
    fun osivTrue_lazyLoadingInController() {
        // MemberService.findMember()는 Team을 fetch join 하지 않음.
        // 하지만 OSIV=true이므로 컨트롤러에서 getTeam().getName() 호출 시에도
        // 영속성 컨텍스트가 살아있어서 Lazy 로딩이 정상 동작합니다.
        mockMvc.get("/members/{id}", memberId)
            .andExpect {
                status { isOk() }
                content { string("member1 - teamA") }
            }
    }
}