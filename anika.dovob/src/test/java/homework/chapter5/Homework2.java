package homework.chapter5;

import com.example.anika.homework.chapter5.Homework;
import com.example.anika.homework.chapter5.entity.Member;
import com.example.anika.homework.chapter5.entity.Team;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    @DisplayName("문제 2-1: 무한루프 확인 - 양방향 toString()의 무한루프 문제")
    void test1() {
        // 1. Team, Member 양방향 관계 설정
        Team team = new Team("개발팀");
        Member member = new Member("홍길동");

        em.persist(team);
        em.persist(member);

        team.addMember(member);

        // 2. flush/clear 후 Member 조회
        em.flush();
        em.clear();

        Member foundMember = em.find(Member.class, member.getId());

        // 3. member.toString() 호출
        // 4. StackOverflowError 발생하는지 확인
        assertThatThrownBy(() -> {
            String result = foundMember.toString();
            System.out.println(result);
        }).isInstanceOf(StackOverflowError.class);
    }

    @Test
    @DisplayName("문제 2-2: 해결 (선택)")
    @Disabled
    void test2() {
        // 1. Team, Member 양방향 관계 설정
        Team team = new Team("개발팀");
        Member member = new Member("홍길동");

        em.persist(team);
        em.persist(member);

        team.addMember(member);

        // 2. flush/clear 후 Member 조회
        em.flush();
        em.clear();

        Member foundMember = em.find(Member.class, member.getId());

        // 3. member.toString() 호출
        // 4. StackOverflowError 발생 안하는지 확인
        String result = foundMember.toString();
        assertThat(result).isNotNull();
    }
}
