package kr.hoi9un.jpastudy.hw5;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class Hw5Q2_2 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-study");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // 문제 2-2: toString() 무한루프 해결 (선택)
            // 1) Team, Member 생성 후 양방향 관계 설정
            Team5 개발팀 = new Team5("개발팀");
            em.persist(개발팀);
            Member5 홍길동 = new Member5("홍길동");
            em.persist(홍길동);
            Long 홍길동id = 홍길동.getId();
            홍길동.setTeam(개발팀);
            // 2) flush/clear
            em.flush();
            em.clear();
            Member5 다시홍길동 = em.find(Member5.class, 홍길동id);
            // 3) toString()에서 연관 엔티티 제외 또는 ID만 출력하도록 수정
            System.out.println("다시홍길동: " + 다시홍길동);
            // 4) 다시 조회 후 toString() 호출해 정상 출력 확인

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
