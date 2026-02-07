package kr.hoi9un.jpastudy.hw3;

import jakarta.persistence.*;

public final class JpaMain {
	public static void main(String[] args) {
		// TODO: EntityManagerFactory와 EntityManager 생성

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-study");
        EntityManager em = emf.createEntityManager();

        // TODO: 트랜잭션 시작
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        // TODO: 회원(id=1L, name="홍길동") 저장
        Member member = new Member(1L, "홍길동");
        em.persist(member);

		// TODO: 저장한 회원 조회
        Member found = em.find(Member.class, 1L);
        System.out.println("id: " + found.getId() + ", name: " + found.getName());
        // TODO: 회원 이름을 "김철수"로 변경 (변경 감지 활용)
        found.setName("김철수");
        System.out.println("id: " + found.getId() + ", name: " + found.getName());

		// TODO: 트랜잭션 커밋 및 자원 정리
        tx.commit();
        em.close();
        emf.close();
	}
}
