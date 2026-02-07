package hw.h05;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

/**
 * 문제 1-3: 연관관계 주인의 중요성
 * 목표: 주인이 아닌 쪽에서만 설정하면 DB에 반영 안 됨을 확인
 *
 * 1. Team, Member 저장
 * 2. team.getMembers().add(member)만 호출 (member.setTeam은 호출하지 않음)
 * 3. flush/clear 후 Member를 다시 조회
 * 4. member.getTeam()이 null인지 확인
 *
 * 왜 null일까요?
 * 연관관계의 주인이 Member.team인데 member.setTeam을 호출하지 않고 team.getMembers().add(member)만 수행해서 외래키 저장되지 않음
 */
public class H0513 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("h05");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 1. Team, Member 저장
        Team team = new Team();
        team.setName("개발팀");
        em.persist(team);

        Member member = new Member();
        member.setName("홍길동");
        em.persist(member);

        // 2. team.getMembers().add(member)만 호출 (member.setTeam은 호출하지 않음)
        team.getMembers().add(member);

        // 3. flush/clear 후 Member를 다시 조회
        em.flush();
        em.clear();

        Member findMember = em.find(Member.class, member.getId());

        // 4. member.getTeam()이 null인지 확인
        System.out.println(findMember.getName());
        System.out.println(findMember.getTeam());

        tx.commit();
        em.close();
        emf.close();
    }
}