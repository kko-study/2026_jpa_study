package homework.chapter_8;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;


public class JpaMain {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();

			Team team = new Team("Team A");
			em.persist(team);

			Member member = new Member("member-1", team);
			em.persist(member);

			em.flush();
			em.clear();

			/**
			 * 3-1
			 */

			Member m = em.find(Member.class, member.getId());

			Team teamProxy = m.getTeam();
			System.out.println("### 1. Team class = " + teamProxy.getClass());

			em.flush();
			em.clear();

			/**
			 * 준영속성 상태라서 조회가 안됨.
			 */
			System.out.println("### 2. Team class = " + teamProxy.getClass());
//			System.out.println("### 3. Team name = " + teamProxy.getName());

			/**
			 * 3-2 clear 이후 "새로" getReference 비교
			 */
			Member m2 = em.find(Member.class, member.getId());
			Long teamId = m2.getTeam().getId();

			em.flush();
			em.clear();

			Team ref = em.getReference(Team.class, teamId);
			/**
			 * Proxy
			 */
			System.out.println("A) class = " + ref.getClass());
			/**
			 * Entity
			 */
			System.out.println("B) name = " + ref.getName());

			/**
			 *
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
}
