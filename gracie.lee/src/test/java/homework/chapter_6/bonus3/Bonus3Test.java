package homework.chapter_6.bonus3;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 보너스 3: 다대일 양방향 vs 일대다 단방향 쿼리 비교
 *
 * 목표: 연관관계 주인 위치에 따른 쿼리 수 차이 확인
 *
 * 숙제1 (일대다 단방향): Team이 연관관계 주인
 * - INSERT team_hw1 (1개)
 * - INSERT member_hw1 (2개)
 * - UPDATE member_hw1 SET team_id (2개)  ← 추가 쿼리!
 * - 총 5개 쿼리
 *
 * 보너스3 (다대일 양방향): Member가 연관관계 주인
 * - INSERT team_bonus3 (1개)
 * - INSERT member_bonus3 with team_id (2개)  ← FK 포함!
 * - 총 3개 쿼리
 *
 * 결론: FK가 있는 쪽(Member)을 연관관계 주인으로 설정하면 UPDATE 쿼리 불필요
 */
class Bonus3Test {

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
    @DisplayName("다대일 양방향: Member가 FK 관리 → UPDATE 쿼리 없음")
    void 다대일_양방향_쿼리_확인() {
        // 1. Team 저장
        Team team = new Team();
        team.setName("개발팀");
        em.persist(team);

        System.out.println("=== Team INSERT 완료 ===");

        // 2. Member 저장 - team 설정 (연관관계 주인)
        Member member1 = new Member();
        member1.setName("회원1");
        member1.setTeam(team);  // FK 직접 설정 → INSERT에 team_id 포함

        Member member2 = new Member();
        member2.setName("회원2");
        member2.setTeam(team);  // FK 직접 설정 → INSERT에 team_id 포함

        em.persist(member1);
        em.persist(member2);

        System.out.println("=== Member INSERT 완료 (team_id 포함) ===");

        // 3. flush - 실제 SQL 실행
        System.out.println("\n========== flush 시작 ==========");
        em.flush();
//Hibernate:
//    /* insert for
//        homework.chapter_6.bonus3.Team */insert
//                into
//        team_bonus3 (name, team_id)
//        values
//                (?, ?)
//Hibernate:
//    /* insert for
//        homework.chapter_6.bonus3.Member */insert
//                into
//        member_bonus3 (name, team_id, member_id)
//        values
//                (?, ?, ?)
//Hibernate:
//    /* insert for
//        homework.chapter_6.bonus3.Member */insert
//                into
//        member_bonus3 (name, team_id, member_id)
//        values
//                (?, ?, ?)
        System.out.println("========== flush 끝 ==========\n");

        /*
         * 예상 쿼리 (숙제1 일대다 단방향과 비교):
         *
         * [숙제1 - 일대다 단방향]
         * INSERT INTO team_hw1 (name, team_id) VALUES (?, ?)
         * INSERT INTO member_hw1 (name, member_id) VALUES (?, ?)  ← team_id 없음!
         * INSERT INTO member_hw1 (name, member_id) VALUES (?, ?)
         * UPDATE member_hw1 SET team_id=? WHERE member_id=?  ← 추가 UPDATE
         * UPDATE member_hw1 SET team_id=? WHERE member_id=?  ← 추가 UPDATE
         *
         * [보너스3 - 다대일 양방향]
         * INSERT INTO team_bonus3 (name, team_id) VALUES (?, ?)
         * INSERT INTO member_bonus3 (name, team_id, member_id) VALUES (?, ?, ?)  ← team_id 포함!
         * INSERT INTO member_bonus3 (name, team_id, member_id) VALUES (?, ?, ?)  ← team_id 포함!
         *
         * 결론: 다대일 양방향을 쓰면 UPDATE 쿼리 2개 절약!
         */

        em.clear();

        // 4. 검증
        Team foundTeam = em.find(Team.class, team.getId());
        assertEquals(2, foundTeam.getMembers().size());
    }
}
