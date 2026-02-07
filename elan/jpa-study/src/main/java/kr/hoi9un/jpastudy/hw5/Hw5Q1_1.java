package kr.hoi9un.jpastudy.hw5;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class Hw5Q1_1 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-study");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // 문제 1-1: FK 저장 확인
            // 1) Team "개발팀" 저장
            Team5 team = new Team5("개발팀");
            em.persist(team);
            // 2) Member "홍길동" 저장
            Member5 member = new Member5("홍길동");
            em.persist(member);
            Long memberId = member.getId();
            // 3) member.setTeam(team) 호출
            member.setTeam(team);
            // 4) flush/clear 후 Member 다시 조회
            em.flush();
            em.clear();
            Member5 m2 = em.find(Member5.class, memberId);

            // 5) member.getTeam()이 "개발팀"인지 확인
            System.out.println("개발팀 확인: " + m2);
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
