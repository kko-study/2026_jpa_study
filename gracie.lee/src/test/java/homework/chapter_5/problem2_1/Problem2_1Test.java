package homework.chapter_5.problem2_1;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 문제 2-1: toString() 무한루프
 *
 * 목표: 양방향 toString()의 무한루프 문제 인식
 */
class Problem2_1Test {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;

    @BeforeAll
    static void setUpFactory() {
        emf = Persistence.createEntityManagerFactory("jpabook");
    }

    @AfterAll
    static void closeFactory() {
        if (emf != null) emf.close();
    }

    @BeforeEach
    void setUp() {
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();
    }

    @AfterEach
    void tearDown() {
        if (tx.isActive()) tx.rollback();
        if (em != null) em.close();
    }

    /**
     * StackOverflowError 발생 원리:
     *
     * 1. 교착상태(Deadlock) 아님 - 두 스레드가 서로의 락을 기다리는 것과 다름 (싱글 스레드)
     * 2. 힙(Heap) 메모리 아님 - 객체 저장 공간, 가득 차면 OutOfMemoryError
     * 3. 스택(Stack) 메모리 - 메서드 호출 정보가 쌓이는 공간, 가득 차면 StackOverflowError
     *
     * 발생 과정:
     * - member.toString() 호출 → team 필드 출력 시 team.toString() 호출
     * - team.toString() 호출 → members 컬렉션 출력 시 각 member.toString() 호출
     * - 무한 재귀 발생 → 스택 프레임이 무한히 쌓임 → 스택 공간 초과 → StackOverflowError
     *
     * [스택]
     * ├─ member.toString()  ← 스택 프레임 1
     * ├─ team.toString()    ← 스택 프레임 2
     * ├─ member.toString()  ← 스택 프레임 3
     * ├─ team.toString()    ← 스택 프레임 4
     * ├─ ... (수천 개 쌓임)
     * └─ 스택 공간 초과 → StackOverflowError
     */
    @Test
    @DisplayName("문제 2-1: toString() 호출 시 StackOverflowError 발생")
    void toString_무한루프() {
        // 1. Team, Member 양방향 관계 설정
        Team team = new Team("개발팀");
        em.persist(team);

        Member member = new Member("홍길동");

        member.setTeam(team);
        team.getMembers().add(member);
        em.persist(member);
        System.out.println("1. Team, Member 양방향 관계 설정 완료");

        // 2. flush/clear 후 Member 조회
        em.flush();
        em.clear();

        Member foundMember = em.find(Member.class, member.getId());
        System.out.println("아직 lazy loading 초기화 전");
        foundMember.getTeam().getMembers().size();  // Lazy Loading 초기화
        System.out.println("2. Member 조회 및 Lazy Loading 초기화");
        System.out.println();

        // 4. StackOverflowError 발생하는지 확인
        assertThrows(StackOverflowError.class, () -> {
            System.out.println(foundMember.toString());
        });

//        1월 31, 2026 8:48:30 오후 org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl$PoolState stop
//        INFO: HHH10001008: Cleaning up connection pool [jdbc:h2:mem:test]

    }
}
