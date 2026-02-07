package homework.chapter_3;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class JpaMain {
	public static void main(String[] args) {
		/**
		 * EntityManagerFactory와 EntityManager 생성
		 */
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			Member member = new Member();
			member.setId("1");
			member.setUsername("홍길동");

			saveMember(member, em);
			Member getMember = getMember(member, em);

			/**
			 * 회원 이름을 "김철수"로 변경 (변경 감지 활용)
			 */
			getMember.setUsername("김철수");

			/**
			 * 트랜잭션 커밋 및 자원 정리
			 */
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			em.close();
			emf.close();
		}
	}

	/**
	 * 회원(id=1L, name="홍길동") 저장
	 */
	private static void saveMember(
			Member member,
			EntityManager em
	) {
		em.persist(member);
	}

	/**
	 * 저장한 회원 조회
	 */
	private static Member getMember(
			Member member,
			EntityManager em
	) {
		return em.find(Member.class, member.getId());
	}
}
