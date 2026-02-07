package com.example.anika.homework.chapter3;

import com.example.anika.homework.chapter3.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class JpaMain {

    /**
     * 요구사항:
     * - EntityManagerFactory와 EntityManager 생성
     * - 회원(id=1L, name="홍길동") 저장
     * - 저장한 회원 조회
     * - 회원 이름을 "김철수"로 변경 (변경 감지 활용)
     * - 트랜잭션 커밋 및 자원 정리
     */
    public static void main(String[] args) {
        // EntityManagerFactory와 EntityManager 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // 회원(id=1L, name="홍길동") 저장
            Member member = new Member();
            member.setId(1L);
            member.setName("홍길동");
            em.persist(member);
            System.out.println("저장 - 회원 ID: " + member.getId() + ", 이름: " + member.getName());

            // 저장한 회원 조회
            Member findMember = em.find(Member.class, 1L);
            System.out.println("조회 - 회원 ID: " + findMember.getId() + ", 이름: " + findMember.getName());

            // 회원 이름을 "김철수"로 변경 (변경 감지 활용)
            findMember.setName("김철수");
            System.out.println("변경 - 회원 ID: " + findMember.getId() + ", 이름: " + findMember.getName());

            // 트랜잭션 커밋
            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            // 자원 정리
            em.close();
        }

        emf.close();
    }
}
