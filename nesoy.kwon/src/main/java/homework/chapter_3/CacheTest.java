package homework.chapter_3;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

/**
 * 중간에 clear를 하면 준영속 상태로 전환되기 때문에 그 이후 객체 변경이 반영이 안될 수 있다.
 */
public class CacheTest {
	public static void main(String[] args) {
		commitAfterClear(args);
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();
		/**
		 * 새로운 회원을 생성하여 영속 상태로 만들기
		 */
		Member member = new Member();
		member.setId("1");
		member.setUsername("철수");
		em.persist(member);

		/**
		 * 같은 id로 두 번 조회하여 동일성(==) 비교
		 */
		Member findMember = em.find(Member.class, "1");
		Member findMember2 = em.find(Member.class, "1");
		/**
		 * 같은 멤버임
		 * findMember==findMember2=true
		 */
		System.out.println("findMember==findMember2=" + (findMember == findMember2));


		/**
		 * 영속성 컨텍스트를 초기화(clear) 후 다시 조회
		 * Select * from Member where id == 1
		 */
		em.clear();
		Member memberAfterClear = em.find(Member.class, "1");

		/**
		 * 초기화 전후의 엔티티가 같은 객체인지 비교하고 결과 출력
		 * memberAfterClear==findMember=false
		 * memberAfterClear==findMember2=false
		 */
		System.out.println("memberAfterClear==findMember=" + (memberAfterClear == findMember));
		System.out.println("memberAfterClear==findMember2=" + (memberAfterClear == findMember2));

		tx.commit();
		em.close();
		emf.close();
	}

	/**
	 * clear 전에 commit을 해도 결과는 동일하다.
	 * 왜냐하면 새로 조회한 객체를 만들었기 때문에 동일성이 깨지기 때문
	 * @param args
	 */
	public static void commitAfterClear(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();
		/**
		 * 새로운 회원을 생성하여 영속 상태로 만들기
		 */
		Member member = new Member();
		member.setId("1");
		member.setUsername("철수");
		em.persist(member);

		/**
		 * 같은 id로 두 번 조회하여 동일성(==) 비교
		 */
		Member findMember = em.find(Member.class, "1");
		Member findMember2 = em.find(Member.class, "1");
		/**
		 * 같은 멤버임
		 * findMember==findMember2=true
		 */
		System.out.println("findMember==findMember2=" + (findMember == findMember2));

		/**
		 * 커밋을 clear 전에 해도 동일한 결과를 가져온다.
		 */
		tx.commit();

		/**
		 * 영속성 컨텍스트를 초기화(clear) 후 다시 조회
		 * Select * from Member where id == 1
		 */
		em.clear();
		Member memberAfterClear = em.find(Member.class, "1");

		/**
		 * 초기화 전후의 엔티티가 같은 객체인지 비교하고 결과 출력
		 * memberAfterClear==findMember=false
		 * memberAfterClear==findMember2=false
		 */
		System.out.println("memberAfterClear==findMember=" + (memberAfterClear == findMember));
		System.out.println("memberAfterClear==findMember2=" + (memberAfterClear == findMember2));

		em.close();
		emf.close();
	}
}