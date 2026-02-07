package kr.hoi9un.jpastudy.hw5;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class Hw5Q1_2 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-study");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // 문제 1-2: FK 업데이트 확인
            // 1) Team "개발팀", "기획팀" 저장
            Team5 개발팀 = new Team5("개발팀");
            em.persist(개발팀);
            Team5 기획팀 = new Team5("기획팀");
            em.persist(기획팀);

            // 2) Member "홍길동"을 "개발팀"에 소속시킴
            Member5 홍길동 = new Member5("홍길동");
            em.persist(홍길동);
            홍길동.setTeam(개발팀);
            Long 홍길동_id = 홍길동.getId();
            System.out.println("홍길동: " + 홍길동);
            // 3) flush/clear
            em.flush();
            em.clear();
            // 4) Member를 다시 조회해서 member.setTeam(기획팀) 호출
            Member5 다시홍길동 = em.find(Member5.class, 홍길동_id);
            다시홍길동.setTeam(기획팀);
            // 5) flush/clear 후 Member를 다시 조회
            em.flush();
            em.clear();
            // 6) member.getTeam()이 "기획팀"으로 변경됐는지 확인
            System.out.println("다시홍길동: " + 다시홍길동);

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
