package homework.chapter_6.homework2.homework2_lazy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

/**
 * 숙제 2: 일대일 LAZY 로딩
 *
 * 목표: @OneToOne(fetch = LAZY)에서 로딩 동작 확인
 *
 * 결과: Member만 먼저 조회, Locker는 실제 접근 시 조회
 */
class Homework2Test {

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
    @DisplayName("일대일 LAZY: Member 조회 후 Locker 접근 시 별도 조회")
    void oneToOne_LAZY_로딩() {
        Locker locker = new Locker("101번");
        em.persist(locker);

        Member member = new Member("홍길동");
        member.setLocker(locker);
        em.persist(member);

        em.flush();
        em.clear();

        System.out.println("=== Member 조회 시작 ===");
        Member foundMember = em.find(Member.class, member.getId());
        /*
        Hibernate:
    select
        m1_0.member_id,
        m1_0.locker_id,
        m1_0.name
    from
        member_hw2_lazy m1_0
    where
        m1_0.member_id=?
         */
        System.out.println("=== Member 조회 끝 ===");

        System.out.println("=== Locker 접근 시작 ===");
        String lockerName = foundMember.getLocker().getName();
        /*
        Hibernate:
    select
        l1_0.locker_id,
        l1_0.name
    from
        locker_hw2_lazy l1_0
    where
        l1_0.locker_id=?
         */
        System.out.println("=== Locker 접근 끝: " + lockerName + " ===");
        Assertions.assertEquals("101번", lockerName);

        // LAZY 결과:
        // Member만 먼저 조회 (JOIN 없음)
        //  "Member 조회 끝" 시점에는 Locker 미조회
        //  "Locker 접근 시작" 후에 Locker SELECT 실행
    }
}
