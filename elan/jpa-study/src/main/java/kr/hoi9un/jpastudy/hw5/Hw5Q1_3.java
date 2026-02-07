package kr.hoi9un.jpastudy.hw5;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class Hw5Q1_3 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-study");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // 문제 1-3: 연관관계 주인의 중요성
            // 1) Team, Member 저장
            Team5 개발팀 = new Team5("개발팀");
            em.persist(개발팀);
            Member5 홍길동 = new Member5("홍길동");
            em.persist(홍길동);
            Long 홍길동id = 홍길동.getId();

            // 2) team.getMembers().add(member)만 호출 (member.setTeam은 호출하지 않음)
            개발팀.getMembers().add(홍길동);
            // 3) flush/clear 후 Member 다시 조회
            em.flush();
            em.clear();
            Member5 다시홍길동 = em.find(Member5.class, 홍길동id);
            // 4) member.getTeam()이 null인지 확인
            System.out.println("다시홍길동: " + 다시홍길동);
            // 5) 왜 null인지 이유 정리
            // 연관관계의 주인만 참조(FK)에 대한 쓰기/수정이 가능, 주인이 아니면 읽기만 가능
            // 여기서 연관관계의 주인은 member5 고 team5 는 mappedBy 로 주인이 아님.

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
