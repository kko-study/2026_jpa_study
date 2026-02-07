package homework.chapter_6.homework2.homework2_eager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

/**
 * 숙제 2: 일대일 EAGER 로딩 (기본값)
 *
 * 목표: @OneToOne 기본값(EAGER)에서 로딩 동작 확인
 *
 * 결과: Member 조회 시 Locker도 JOIN으로 함께 가져옴
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
    @DisplayName("일대일 EAGER: Member 조회 시 Locker도 함께 조회")
    void oneToOne_EAGER_로딩() {
        // 1. Locker "101번" 저장
        Locker locker = new Locker("101번");
        em.persist(locker);

        // 2. Member "홍길동" 저장, locker 연결
        Member member = new Member("홍길동");
        member.setLocker(locker);
        em.persist(member);

        em.flush();
        em.clear();

        // 4. Member 조회
        System.out.println("=== Member 조회 시작 ===");
        Member foundMember = em.find(Member.class, member.getId());
        /*
        Hibernate:
    select
        m1_0.member_id,
        l1_0.locker_id,
        l1_0.name,
        m1_0.name
    from
        member_hw2_eager m1_0
    left join
        locker_hw2_eager l1_0
            on l1_0.locker_id=m1_0.locker_id
    where
        m1_0.member_id=?
         */
        System.out.println("=== Member 조회 끝 ===");
        System.out.println();

        System.out.println("=== Locker 접근 시작 ===");
        String lockerName = foundMember.getLocker().getName();
        System.out.println("=== Locker 접근 끝: " + lockerName + " ===");
        Assertions.assertEquals("101번", lockerName);
        // EAGER 결과:
        // Member 조회 시 LEFT JOIN으로 Locker도 함께 가져옴
        // "Member 조회 끝" 전에 Locker SELECT 완료
        // "Locker 접근 시작" 후 추가 쿼리 없음
    }
}
