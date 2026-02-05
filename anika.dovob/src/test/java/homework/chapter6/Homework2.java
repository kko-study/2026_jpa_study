package homework.chapter6;

import com.example.anika.homework.chapter6.Homework;
import com.example.anika.homework.chapter6.homework2.Locker;
import com.example.anika.homework.chapter6.homework2.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Homework.class)
class Homework2 {

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
    @DisplayName("숙제2: @OneToOne에서 LAZY 로딩 동작 확인")
    void test1() {
        // Locker "101번" 저장
        Locker locker = new Locker();
        locker.setName("101번");
        em.persist(locker);

        // Member "홍길동" 저장, member.setLocker(locker) 호출
        Member member = new Member();
        member.setName("홍길동");
        member.setLocker(locker);
        em.persist(member);

        // flush/clear
        em.flush();
        em.clear();

        /**
         * FetchType.EAGER 일 때의 Hibernate SQL 로그
         *
         * Hibernate:
         *     select
         *         m1_0.member_id,
         *         l1_0.locker_id,
         *         l1_0.name,
         *         m1_0.name
         *     from
         *         member_hw2 m1_0
         *     left join
         *         locker_hw2 l1_0
         *             on l1_0.locker_id=m1_0.locker_id
         *     where
         *         m1_0.member_id=1
         */
        // Member 조회
        Member foundMember = em.find(Member.class, member.getId());
        System.out.println("=== Member 조회 끝 ===");

        // member.getLocker().getName() 호출
        foundMember.getLocker().getName();

        /**
         * FetchType.LAZY 일 때의 Hibernate SQL 로그
         *
         * Hibernate:
         *     select
         *         l1_0.locker_id,
         *         l1_0.name
         *     from
         *         locker_hw2 l1_0
         *     where
         *         l1_0.locker_id=1
         */
    }

    @Test
    @DisplayName("보너스1: 역방향 지연 로딩 - 일대일에서 FK 없는 쪽의 LAZY 로딩 한계 확인")
    @Disabled
    void bonus1() {
        // Locker, Member 저장 (Member가 FK 보유)
        Locker locker = new Locker();
        locker.setName("101번");
        em.persist(locker);

        Member member = new Member();
        member.setName("홍길동");
        member.setLocker(locker);
        em.persist(member);

        // flush/clear
        em.flush();
        em.clear();

        // Locker 조회
        Locker foundLocker = em.find(Locker.class, locker.getId());
        /**
         * Hibernate:
         *     select
         *         l1_0.locker_id,
         *         l1_0.name
         *     from
         *         locker_hw2 l1_0
         *     where
         *         l1_0.locker_id=1
         *
         * Hibernate:
         *     select
         *         m1_0.member_id,
         *         l1_0.locker_id,
         *         l1_0.name,
         *         m1_0.name
         *     from
         *         member_hw2 m1_0
         *     left join
         *         locker_hw2 l1_0
         *             on l1_0.locker_id=m1_0.locker_id
         *     where
         *         m1_0.locker_id=1
         */
        System.out.println("=== Locker 조회 끝 ===");

        // locker.getMember().getName() 호출
//        String memberName = foundLocker.getMember().getName();
    }
}
