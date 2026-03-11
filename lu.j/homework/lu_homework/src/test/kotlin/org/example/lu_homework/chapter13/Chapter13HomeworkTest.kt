package org.example.lu_homework.chapter13

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.example.lu_homework.chapter12.Member
import org.example.lu_homework.chapter12.MemberRepository
import org.example.lu_homework.chapter12.Team
import org.example.lu_homework.chapter12.TeamRepository
import org.hibernate.LazyInitializationException
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * 13장 과제 테스트.
 *
 * @SpringBootTest 를 사용합니다 (@DataJpaTest가 아님).
 * @DataJpaTest는 각 테스트를 @Transactional로 감싸므로 트랜잭션 경계를 관찰할 수 없습니다.
 * @SpringBootTest는 트랜잭션을 자동으로 감싸지 않으므로 Service의 트랜잭션 경계를 그대로 관찰합니다.
 */
@SpringBootTest
class Chapter13HomeworkTest {

    @Autowired
    lateinit var memberService: MemberService

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Autowired
    lateinit var teamRepository: TeamRepository

    var memberId: Long = 0

    @BeforeEach
    fun setUp() {
        memberRepository.deleteAll()
        teamRepository.deleteAll()

        val teamA = teamRepository.save(Team("teamA"))
        val member = memberRepository.save(Member("member1", 20, teamA))
        memberId = member.id!!
    }

    @AfterEach
    fun tearDown() {
        memberRepository.deleteAll()
        teamRepository.deleteAll()
    }

    // ===== Scenario A: 변경 감지 (Dirty Checking) =====

    @Test
    @DisplayName("A-1: @Transactional O → save() 없이도 UPDATE 반영")
    fun changeName_withTransaction() {
        memberService.changeName(memberId, "updatedName")

        val found = memberRepository.findById(memberId).orElseThrow()
        assertThat(found.name).isEqualTo("updatedName")
    }

    @Test
    @DisplayName("A-2: @Transactional X → UPDATE 미반영")
    fun changeName_withoutTransaction() {
        memberService.changeNameWithoutTransaction(memberId, "updatedName")

        val found = memberRepository.findById(memberId).orElseThrow()
        assertThat(found.name).isEqualTo("member1") // 변경되지 않음
    }

    // ===== Scenario B: 트랜잭션 밖 지연 로딩 =====

    @Test
    @DisplayName("B-1: findMember → 트랜잭션 밖에서 Lazy 접근 시 LazyInitializationException")
    fun findMember_lazyInitException() {
        val member = memberService.findMember(memberId)

        // 서비스 메서드의 @Transactional 이 끝났으므로 영속성 컨텍스트 종료.
        // 이 시점에서 team 프록시를 초기화하면 예외 발생.
        assertThatThrownBy { member.team.name }
            .isInstanceOf(LazyInitializationException::class.java)
    }

    @Test
    @DisplayName("B-2: findMemberWithTeam → fetch join으로 Lazy 문제 해결")
    fun findMemberWithTeam_fetchJoin() {
        val member = memberService.findMemberWithTeam(memberId)

        // fetch join 으로 Team을 함께 가져왔으므로 트랜잭션 밖에서도 정상 동작
        assertThat(member.team.name).isEqualTo("teamA")
    }


    /**
     * Q1. @Transactional이 영속성 컨텍스트의 생명주기를 결정한다는 것은 무슨 의미인가?
     * - 트랜잭션 시작 = 영속성 컨텍스트 생성
     * - 트랜잭션 커밋 = flush + 영속성 컨텍스트 종료
     * - 이 범위 밖에서 엔티티는 어떤 상태가 되는가?
     *
     * 답.
     * - 준영속상태가 된다. 그래서 변경 감지가 동작하지 않고 lazy 로딩이 불가능함.
     * - 프록시 초기화 시도하면 LazyInitializationException 이 발생함
     *
     * Q2. 변경 감지 vs merge(), 실무에서는 어떤 것을 써야 하나?
     * - merge()의 위험성: 모든 필드를 덮어씌운다는 것은 무슨 의미인가?
     * - 예: 이름만 바꾸려고 merge()를 호출했는데 다른 필드가 null로 덮어씌워진다면?
     *
     * 답.
     * - merge 같은 경우 준영속 엔티티를 통째로 영속성 컨텍스트에 복사하는 방식의 동작이기 떄문에 의도대로 동작하지 않을 수 있을것 같음
     * - merge 는 모든 필드를 덮어씌운다는 부분에서 위험함. 예로 나온 것처럼 null로 덮어씌워질 수 있기 때문
     * - 변경 감지를 쓰는 방식이 안전하고 좋을 것 같음
     *
     * Q3. fetch join 말고 다른 해결 방법은 없나?
     * - @EntityGraph 활용
     * - DTO로 직접 조회 (new 키워드 사용 JPQL)
     * - 각각의 장단점은?
     *
     * 답.
     * - @EntityGraph 의 경우 연관관계를 어떤 범위까지 함께 로딩할지 선언적으로 지정하는 방식
     *   - Repository 에 @EntityGraph(attributePaths=["team"]) 을 하면 JPQL 을 쓰지 않고 함께 조회 가능
     *   - 복잡한 조건이나 복잡한 조인 구조에서는 쓰기 어려운 부분이 단점
     *
     * - DTO 로 직접 조회
     *   - 우리가 가장 비슷하게 많이 쓰고 있는 방법 (한방 쿼리로 해결)
     *   - 이럴 경우 화면에 종속성이 커져서 재사용성이 없어지게 되고 DTO 코드가 많아짐
     *
     *
     * Q4. OSIV=true가 편리한데 실무에서는 왜 끄는가?
     * - OSIV=true이면 영속성 컨텍스트가 API 응답이 끝날 때까지 살아있다 — DB 커넥션도 그때까지 반환되지 않는다면?
     * - 트래픽이 많은 서비스에서 커넥션 풀이 고갈되면 어떤 일이 벌어지는가?
     * - 컨트롤러/뷰에서 의도치 않게 지연 로딩 쿼리가 나가면 성능 문제를 추적하기 어려운 이유는?
     *
     * 답.
     * - OSIV=true 의 장점은 컨트롤러나 뷰에서도 Lazy Loading 이 가능해진다는 점이지만,
     *   뷰에서 만약 데이터를 바꿔버린다거나.. 또는 API 요청이 응답될 때까지 커넥션이 지속될 가능성도 있음
     *   트래픽이 많은 서비스에서 커넥션 풀이 고갈될 경우 응답이 지연되기 시작하고 타임아웃이 발생함.
     *   그리고 컨트롤러나 뷰에서 엔티티의 연관 필드를 접근할 때마다 Lazy 쿼리가 실행되게 될 수 있는데 추가 쿼리가 발생할 수도 있어서 성능 추적도 힘들다.
     *
     * Q5. 반대로, OSIV=true가 적합한 경우는?
     * - 트래픽이 적은 어드민/백오피스 시스템이라면?
     * - 빠르게 프로토타이핑해야 하는 초기 단계라면?
     * - OSIV를 끄면 모든 지연 로딩을 fetch join이나 DTO로 해결해야 하는데, 그 비용 대비 효과가 항상 맞는가?
     *
     * 답.
     * - 백오피스 시스템과 어드민에서는 트래픽이 적기 때문에 OSIV=true 를 써도 괜찮을 것 같다.
     *   유지보수가 단순해질 수 있기 때문에
     * - OSIV 를 껐을 때보다 켰을 때의 단순 조회 개발 속도 같은 부분이 효율적일 수 있기 때문에 적합할 수 있을 것 같다.
     * - 대신 대용량 트래픽 서비스에서는 권장하지 않는다.
     *
     */
}