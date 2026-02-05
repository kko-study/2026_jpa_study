package hw.h05;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

/**
 *
 * 문제 2-2: 해결 (선택)
 * toString()에서 연관 엔티티를 제외하거나 ID만 출력하도록 수정
 */
public class H0522 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("h05");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Team team = new Team();
        team.setName("개발팀");
        em.persist(team);

        Member member = new Member();
        member.setName("홍길동");
        member.setTeam(team);
        team.getMembers().add(member);
        em.persist(member);

        em.flush();
        em.clear();

        member = em.find(Member.class, member.getId());

        try {
            System.out.println(team);
        } catch (StackOverflowError e) {
            System.out.println(e);
        }

        tx.commit();
        em.close();
        emf.close();
    }
}
