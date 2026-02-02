package homework.chapter_6.homework1.homework1_joincolumn;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

/**
 * 숙제 1: @JoinColumn 토글
 *
 * 목표: @JoinColumn 유무에 따른 일대다 단방향 동작 차이 확인
 *
 * 실행 방법:
 * 1. Team.java에서 @JoinColumn 주석 상태로 테스트 실행 → 조인 테이블 생성 확인
 * 2. @JoinColumn(name = "team_id") 주석 해제 후 다시 실행 → UPDATE 쿼리 확인
 */
class Homework1Test {

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
    @DisplayName("일대다 단방향: Team에 Member 추가 후 SQL 로그 확인")
    void oneToMany_단방향_SQL_확인() {

        // 1. Team "개발팀" 저장
        Team team = new Team("개발팀");
        em.persist(team);

        // 2. Member "홍길동", "김철수" 저장
        Member member1 = new Member("홍길동");
        Member member2 = new Member("김철수");
        em.persist(member1);
        em.persist(member2);

        // 3. team.getMembers().add() 호출
        team.getMembers().add(member1);
        team.getMembers().add(member2);

        // 4. flush - SQL 확인!
        System.out.println("=== flush 시작 (SQL 로그 확인!) ===");
        em.flush();
        System.out.println("=== flush 끝 ===");
        System.out.println();

        /* 예상

  @JoinColumn 있음 (homework1_joincolumn)
  INSERT team ...
  INSERT member ... (x2)
  UPDATE member SET team_id=? WHERE member_id=?  (x2) //  FK 업데이트


         */

        // 결과
//        Hibernate:
//    /* insert for
//        homework.chapter_6.homework1.homework1_joincolumn.Team */insert
//                into
//        team_hw1_join (name, team_id)
//        values
//                (?, ?)
//        Hibernate:
//    /* insert for
//        homework.chapter_6.homework1.homework1_joincolumn.Member */insert
//                into
//        member_hw1_join (name, member_id)
//        values
//                (?, ?)
//        Hibernate:
//    /* insert for
//        homework.chapter_6.homework1.homework1_joincolumn.Member */insert
//                into
//        member_hw1_join (name, member_id)
//        values
//                (?, ?)
//        Hibernate:
//        update
//                member_hw1_join
//        set
//        team_id=?
//                where
//        member_id=?
//        Hibernate:
//        update
//        member_hw1_join
//                set
//        team_id=?
//                where
//        member_id=?
    }
}
