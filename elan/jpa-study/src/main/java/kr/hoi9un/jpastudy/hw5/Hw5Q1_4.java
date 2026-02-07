package kr.hoi9un.jpastudy.hw5;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class Hw5Q1_4 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-study");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // 문제 1-4: 1차 캐시 문제
            // 1) Team, Member 저장
            Team5 개발팀 = new Team5("개발팀");
            em.persist(개발팀);
            Long 개발팀id = 개발팀.getId();
            Member5 홍길동 = new Member5("홍길동");
            em.persist(홍길동);
            Member5 둘리 = new Member5("둘리");
            em.persist(둘리);

            // 2) member.setTeam(team)만 호출 (team.getMembers().add는 호출하지 않음)
            홍길동.setTeam(개발팀);
            둘리.setTeam(개발팀);
            // 3) flush 전에 team.getMembers().size() 출력
            System.out.println("3. 개발팀 members: " + 개발팀.getMembers().size());
            // 4) flush/clear 후 Team을 다시 조회
            em.flush();
            em.clear();
            Team5 다시개발팀 = em.find(Team5.class, 개발팀id);

            // 5) team.getMembers().size() 출력
            System.out.println("5. 다시개발팀 members: " + 다시개발팀.getMembers().size());
            // 6) 3번과 5번의 결과가 다른 이유를 정리
            // 3에서는 순수 객체의 결과로 0이 나왔고, 5에서는 캐시가 비워져서 질의를 통한 조인쿼리가 수행되어 값이 나옴.

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Error: " + e);
        } finally {
            em.close();
            emf.close();
        }
    }
}
