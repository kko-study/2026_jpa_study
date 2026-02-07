package homework.chapter_6.bonus1;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

/**
 * 보너스 1: 역방향 지연 로딩
 *
 * 목표: 일대일에서 FK 없는 쪽의 LAZY 로딩 한계 확인
 *
 * 숙제 2에서 Member → Locker는 LAZY로 지연 로딩이 됐습니다.
 * 이번엔 반대로 Locker → Member를 테스트해보세요.
 *
 * 결과: LAZY로 설정했는데도 EAGER처럼 동작함!
 */
class Bonus1Test {

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

    @Test
    @DisplayName("일대일 역방향: FK 없는 쪽(Locker)에서 LAZY 로딩이 동작하는지 확인")
    void oneToOne_역방향_LAZY_테스트() {
        // 1. Locker 저장
        Locker locker = new Locker();
        locker.setName("101번");
        em.persist(locker);

        // 2. Member 저장 (FK 보유)
        Member member = new Member();
        member.setName("홍길동");
        member.setLocker(locker);
        em.persist(member);

        em.flush();
        em.clear();

        // 3. Locker 조회 (FK 없는 쪽)
        System.out.println("=== Locker 조회 시작 ===");
        Locker foundLocker = em.find(Locker.class, locker.getId());
        /*
Hibernate:
    select
        l1_0.locker_id,
        l1_0.name
    from
        locker_bonus1 l1_0
    where
        l1_0.locker_id=?
Hibernate:  // member를 지연 로딩해야 하지만... SELECT 실행됨!
    select
        m1_0.member_id,
        m1_0.locker_id,
        m1_0.name
    from
        member_bonus1 m1_0
    where
        m1_0.locker_id=?
         */
        System.out.println("=== Locker 조회 끝 ===");
        System.out.println();

        // 4. Member 접근
        System.out.println("=== Member 접근 시작 ===");
        String memberName = foundLocker.getMember().getName();
        System.out.println("=== Member 접근 끝: " + memberName + " ===");

        Assertions.assertEquals("홍길동", memberName);

        /*
         * 예상: LAZY이므로 "Locker 조회 끝" 후에 Member SELECT
         * 실제: "Locker 조회 끝" 전에 Member SELECT 실행됨!
         *
         * 이유: FK가 없는 쪽(Locker)은 Member가 있는지 없는지 알 수 없음
         *       → null인지 프록시인지 결정 불가
         *       → 무조건 조회해야 함 (LAZY 무시)
         *
         * 결론: 일대일 양방향에서 FK 없는 쪽은 LAZY가 동작하지 않음
         */
    }
}
