package homework.chapter_6.homework1.homework1_nojoincolumn;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

        /* 예상

  @JoinColumn 없음 (homework1_nojoincolumn)
  INSERT team_ ...
  INSERT member_ ... (x2)
  INSERT team_member ...  (x2) //  조인 테이블에 삽입

         */

        // 결과
//        Hibernate:
//    /* insert for
//        homework.chapter_6.homework1.homework1_nojoincolumn.Team */insert
//                into
//        team_hw1_nojoin (name, team_id)
//        values
//                (?, ?)
//        Hibernate:
//    /* insert for
//        homework.chapter_6.homework1.homework1_nojoincolumn.Member */insert
//                into
//        member_hw1_nojoin (name, member_id)
//        values
//                (?, ?)
//        Hibernate:
//    /* insert for
//        homework.chapter_6.homework1.homework1_nojoincolumn.Member */insert
//                into
//        member_hw1_nojoin (name, member_id)
//        values
//                (?, ?)
//        Hibernate:
//    /* insert for
//        homework.chapter_6.homework1.homework1_nojoincolumn.Team.members */insert
//                into
//        team_hw1_nojoin_member_hw1_nojoin (Hw1NoJoinTeam_team_id, members_member_id)
//        values
//                (?, ?)
//        Hibernate:
//    /* insert for
//        homework.chapter_6.homework1.homework1_nojoincolumn.Team.members */insert
//                into
//        team_hw1_nojoin_member_hw1_nojoin (Hw1NoJoinTeam_team_id, members_member_id)
//        values
//                (?, ?)
//=== flush 끝 ===

    }
}
