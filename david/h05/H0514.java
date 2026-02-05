package hw.h05;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

/**
 * 문제 1-4: 1차 캐시 문제
 * 목표: 양쪽 다 설정해야 하는 이유 (1차 캐시 동기화)
 *
 * 1. Team, Member 저장
 * 2. member.setTeam(team)만 호출 (team.getMembers().add()는 호출하지 않음)
 * 3. flush 전에 team.getMembers().size() 출력
 * 4. flush/clear 후 Team을 다시 조회
 * 5. team.getMembers().size() 출력
 *
 * 3번과 5번의 결과가 다른 이유는 무엇일까요?
 * 2에서 team 객체(1차 캐시)에는 추가하지 않았으므로 추가되지 않음
 * 4에서 flush/clear 후 다시 조회했을 때 1차 캐시를 FK 기반으로 다시 불러오므로 추가됨
 */
public class H0514 {
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

        // 2. member.setTeam(team)만 호출 (team.getMembers().add()는 호출하지 않음)
        member.setTeam(team);

        // 3. flush 전에 team.getMembers().size() 출력
        System.out.println(team.getMembers().size());

        // 4. flush/clear 후 Team을 다시 조회
        em.flush();
        em.clear();

        Team findTeam = em.find(Team.class, team.getId());

        // 5. team.getMembers().size() 출력
        System.out.println(findTeam.getMembers().size());

        tx.commit();
        em.close();
        emf.close();
    }
}