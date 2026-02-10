package com.example.anika.homework.chapter8;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.transaction.Transactional;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class Chapter8Test {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;

    @BeforeAll
    static void setUpFactory() {
        emf = Persistence.createEntityManagerFactory("jpabook");
    }

    @AfterAll
    static void closeFactory() {
        if (emf != null) {
            emf.close();
        }
    }

    @BeforeEach
    void setUp() {
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();
        setUpData();
    }

    @AfterEach
    void tearDown() {
        if (tx != null && tx.isActive()) {
            tx.rollback();
        }
        if (em != null) {
            em.close();
        }
    }

    /**
     * 데이터 준비
     */
    private void setUpData() {
        Team teamA = new Team(10L, "team-A");
        em.persist(teamA);

        Member member1 = new Member(1L, "member-1", teamA);
        em.persist(member1);

        em.flush();
        em.clear();
    }

    /**
     * 3-1) 기본 실험 코드
     */
    @Test
    void test_case_1() {
        Member m = em.find(Member.class, 1L);   // select Member SQL

        Team teamProxy = m.getTeam();
        System.out.println("1) class = " + teamProxy.getClass());

        em.flush();
        em.clear(); // m, teamProxy 모두 detached 상태

        // System.out.println("3) name = " + teamProxy.getName());
        System.out.println("2) class = " + teamProxy.getClass());
        assertThrows(LazyInitializationException.class, () -> System.out.println("3) name = " + teamProxy.getName()));
    }

    /**
     * 3-2) 추가 케이스 A: clear 이후 "새로" getReference 비교
     */
    @Test
    void test_case_2() {
        Member m = em.find(Member.class, 1L);
        Long teamId = m.getTeam().getId();

        em.flush();
        em.clear();

        Team ref = em.getReference(Team.class, teamId); // 새 프록시 획득
        System.out.println("A) class = " + ref.getClass());
        System.out.println("B) name = " + ref.getName());   // select Team SQL
    }

    /**
     * 3-3) 추가 케이스 B: 트랜잭션 경계 밖에서 접근
     * 옵션 2) 직접 clear + close 유사 상황 재현
     */
    @Test
    @Transactional
    void lazyInitializationException_after_clear_on_proxy() {
        Member m = em.find(Member.class, 1L);
        Team teamProxy = m.getTeam(); // LAZY → 프록시

        em.flush();
        em.clear(); // m, teamProxy 모두 detached 상태

        assertThrows(LazyInitializationException.class, () -> {
            teamProxy.getName(); // detached 프록시 초기화 시도 → 예외
        });
    }

    /**
     * 3-4) 아래 코드에서 위험한 줄은?
     */
    @Test
    @Transactional
    void test() {
        Member m = em.find(Member.class, 1L);
        Team t = m.getTeam();
        em.clear();

        // t.getName();
        // 초기화되지 않은 프록시에 접근 시 LazyInitializationException 발생
        assertThrows(LazyInitializationException.class, () -> {
            t.getName();
        });
    }
}
