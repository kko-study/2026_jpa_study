package hw.h05;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

/**
 * 숙제 2: toString() 무한루프
 * 요구사항
 * Team과 Member에 서로를 출력하는 toString() 추가:
 *
 * Member.toString()에서 team 출력
 * Team.toString()에서 members 출력
 *
 * 문제 2-1: 무한루프 확인
 * 목표: 양방향 toString()의 무한루프 문제 인식
 *
 * 1. Team, Member 양방향 관계 설정
 * 2. flush/clear 후 Member 조회
 * 3. member.toString() 호출
 * 4. StackOverflowError 발생하는지 확인
 */
public class H0521 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("h05");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 1. Team, Member 양방향 관계 설정
        Team team = new Team();
        team.setName("개발팀");
        em.persist(team);

        Member member = new Member();
        member.setName("홍길동");
        member.setTeam(team);
        team.getMembers().add(member);
        em.persist(member);

        // 2. flush/clear 후 Member 조회
        em.flush();
        em.clear();

        member = em.find(Member.class, member.getId());

        try {
            // 3. member.toString() 호출
            System.out.println(member.toString());
        } catch (StackOverflowError e) {
            // 4. StackOverflowError 발생하는지 확인
            System.out.println(e);
        }

        tx.commit();
        em.close();
        emf.close();
    }
}
