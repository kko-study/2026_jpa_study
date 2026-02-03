package homework.chapter5;

import com.example.anika.homework.chapter5.Homework;
import com.example.anika.homework.chapter5.entity.Member;
import com.example.anika.homework.chapter5.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

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
    @DisplayName("문제 1-1: FK 저장 확인 - 연관관계 주인 쪽에서 FK를 설정하면 DB에 저장된다")
    void test1() {
        // 1. Team "개발팀" 저장
        Team team = new Team("개발팀");
        em.persist(team);

        // 2. Member "홍길동" 저장
        Member member = new Member("홍길동");
        em.persist(member);

        // 3. member.setTeam(team) 호출
        member.setTeam(team);

        // 4. flush/clear 후 Member를 다시 조회
        em.flush();
        em.clear();

        Member foundMember = em.find(Member.class, member.getId());

        // 5. member.getTeam()이 "개발팀"인지 확인
        assertThat(foundMember.getTeam().getName()).isEqualTo("개발팀");
    }

    @Test
    @DisplayName("문제 1-2: FK 업데이트 확인 - 연관관계 변경이 DB에 반영된다")
    void test2() {
        // 1. Team "개발팀", "기획팀" 저장
        Team team1 = new Team("개발팀");
        Team team2 = new Team("기획팀");
        em.persist(team1);
        em.persist(team2);

        // 2. Member "홍길동"을 "개발팀"에 소속시킴
        Member member = new Member("홍길동");
        member.setTeam(team1);
        em.persist(member);

        // 3. flush/clear
        em.flush();
        em.clear();

        // 4. Member를 다시 조회해서 member.setTeam(기획팀) 호출
        Member foundMember = em.find(Member.class, member.getId());
        Team foundTeam2 = em.find(Team.class, team2.getId());
        foundMember.setTeam(foundTeam2);

        // 5. flush/clear 후 Member를 다시 조회
        em.flush();
        em.clear();

        Member updatedMember = em.find(Member.class, member.getId());

        // 6. member.getTeam()이 "기획팀"으로 변경됐는지 확인
        assertThat(updatedMember.getTeam().getName()).isEqualTo("기획팀");
    }

    @Test
    @DisplayName("문제 1-3: 연관관계 주인의 중요성 - 주인이 아닌 쪽에서만 설정하면 DB에 반영 안 됨")
    void test3() {
        // 1. Team, Member 저장
        Team team = new Team("개발팀");
        Member member = new Member("홍길동");
        em.persist(team);
        em.persist(member);

        // 2. team.getMembers().add(member)만 호출 (member.setTeam은 호출하지 않음)
        team.getMembers().add(member);

        // 3. flush/clear 후 Member를 다시 조회
        em.flush();
        em.clear();

        Member foundMember = em.find(Member.class, member.getId());

        // 4. member.getTeam()이 null인지 확인
        assertThat(foundMember.getTeam()).isNull();
    }

    @Test
    @DisplayName("문제 1-4: 1차 캐시 문제 - 양쪽 다 설정해야 하는 이유")
    void test4() {
        // 1. Team, Member 저장
        Team team = new Team("개발팀");
        Member member = new Member("홍길동");
        em.persist(team);
        em.persist(member);

        // 2. member.setTeam(team)만 호출 (team.getMembers().add()는 호출하지 않음)
        member.setTeam(team);

        // 3. flush 전에 team.getMembers().size()
        int sizeBeforeFlush = team.getMembers().size();

        // 4. flush/clear 후 Team을 다시 조회
        em.flush();
        em.clear();

        Team foundTeam = em.find(Team.class, team.getId());

        // 5. team.getMembers().size()
        int sizeAfterFlush = foundTeam.getMembers().size();

        // 3번과 5번의 결과가 다름을 확인
        assertThat(sizeBeforeFlush).isEqualTo(0);
        assertThat(sizeAfterFlush).isEqualTo(1);
    }
}
