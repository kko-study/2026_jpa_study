package kr.hoi9un.jpastudy.hw3;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.w3c.dom.ls.LSOutput;

public class CacheTest {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-study");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();
		// TODO: 새로운 회원을 생성하여 영속 상태로 만들기
        Member member = new Member(2L, "심사임당");
        em.persist(member);


        // Member3 member = new Member3(2L, "홍길동");
		// em.persist(member); // SQL: INSERT (커밋 시점에 실행)

		// TODO: 같은 id로 두 번 조회하여 동일성(==) 비교
        Member found1 = em.find(Member.class, 2L);
        Member found2 = em.find(Member.class, 2L);
        System.out.println("1 == 2 is same? " + (found1 == found2));
        System.out.println(found1);

		// TODO: 영속성 컨텍스트 초기화(clear) 후 다시 조회
        em.clear();
        Member found3 = em.find(Member.class, 2L);

		// em.clear();
		// Member3 find3 = em.find(Member3.class, 2L); // SQL: SELECT

		// TODO: 초기화 전후 동일성 비교 및 결과 출력
        System.out.println("1 == 3 is same? " + (found1 == found3));
        System.out.println(found3);

		tx.commit();
		em.close();
		emf.close();
	}

}
