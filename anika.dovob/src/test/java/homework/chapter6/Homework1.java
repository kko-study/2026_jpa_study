package homework.chapter6;

import com.example.anika.homework.chapter6.Homework;
import com.example.anika.homework.chapter6.homework1.Member;
import com.example.anika.homework.chapter6.homework1.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Homework.class)
class Homework1 {

    @Autowired
    private EntityManagerFactory emf;

    private EntityManager em;
    private EntityTransaction tx;

    @BeforeEach
    void setUp() {
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();
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

    @Test
    @DisplayName("숙제 1: @JoinColumn 유무에 따른 일대다 단방향 동작 차이 확인")
    void test1() {
        // Team "개발팀" 저장
        Team team = new Team();
        team.setName("개발팀");
        em.persist(team);

        // Member "홍길동", "김철수" 저장
        Member member1 = new Member();
        member1.setName("홍길동");
        em.persist(member1);

        Member member2 = new Member();
        member2.setName("김철수");
        em.persist(member2);

        // team.getMembers().add(홍길동), team.getMembers().add(김철수) 호출
        team.getMembers().add(member1);
        team.getMembers().add(member2);

        // flush - SQL 로그 확인
        em.flush();
        em.clear();

        // @JoinColumn 없을 때: 조인 테이블 INSERT
        /**
         * Hibernate:
         *     insert
         *     into
         *         team_hw1
         *         (name, team_id)
         *     values
         *         ("개발팀", 1)
         * Hibernate:
         *     insert
         *     into
         *         member_hw1
         *         (name, member_id)
         *     values
         *         ("홍길동", 1)
         * Hibernate:
         *     insert
         *     into
         *         member_hw1
         *         (name, member_id)
         *     values
         *         ("김철수", 2)
         * Hibernate:
         *     insert
         *     into
         *         team_hw1_members
         *         (hw1_team_team_id, members_member_id)
         *     values
         *         (1, 1)
         * Hibernate:
         *     insert
         *     into
         *         team_hw1_members
         *         (hw1_team_team_id, members_member_id)
         *     values
         *         (1, 2)
         */


        // @JoinColumn 있을 때: UPDATE 쿼리
        /**
         * Hibernate:
         *     insert
         *     into
         *         team_hw1
         *         (name, team_id)
         *     values
         *         ("개발팀", 1)
         * Hibernate:
         *     insert
         *     into
         *         member_hw1
         *         (name, member_id)
         *     values
         *         ("홍길동", 1)
         * Hibernate:
         *     insert
         *     into
         *         member_hw1
         *         (name, member_id)
         *     values
         *         ("김철수", 2)
         * Hibernate:
         *     update
         *         member_hw1
         *     set
         *         team_id=1
         *     where
         *         member_id=1
         * Hibernate:
         *     update
         *         member_hw1
         *     set
         *         team_id=1
         *     where
         *         member_id=2
         */
    }
}
