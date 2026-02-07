package hw.h05;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

/**
 * 문제 1-2: FK 업데이트 확인
 * 목표: 연관관계 변경이 DB에 반영되는지 확인
 *
 * 1. Team "개발팀", "기획팀" 저장
 * 2. Member "홍길동"을 "개발팀"에 소속시킴
 * 3. flush/clear
 * 4. Member를 다시 조회해서 member.setTeam(기획팀) 호출
 * 5. flush/clear 후 Member를 다시 조회
 * 6. member.getTeam()이 "기획팀"으로 변경됐는지 확인
 */
public class H0512 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("h05");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 1. Team "개발팀", "기획팀" 저장
        Team devTeam = new Team();
        devTeam.setName("개발팀");
        em.persist(devTeam);

        Team planTeam = new Team();
        planTeam.setName("기획팀");
        em.persist(planTeam);

        // 2. Member "홍길동"을 "개발팀"에 소속시킴
        Member member = new Member();
        member.setName("홍길동");
        member.setTeam(devTeam);
        em.persist(member);

        // 3. flush/clear
        em.flush();
        em.clear();

        // 4. Member를 다시 조회해서 member.setTeam(기획팀) 호출
        Member findMember = em.find(Member.class, member.getId());
        Team findPlanTeam = em.find(Team.class, planTeam.getId());
        System.out.println(findMember.getTeam().getName());

        findMember.setTeam(findPlanTeam);

        // 5. flush/clear 후 Member를 다시 조회
        em.flush();
        em.clear();

        Member findMember2 = em.find(Member.class, member.getId());

        // 6. member.getTeam()이 "기획팀"으로 변경됐는지 확인
        System.out.println(findMember2.getTeam().getName());

        tx.commit();
        em.close();
        emf.close();
    }
}