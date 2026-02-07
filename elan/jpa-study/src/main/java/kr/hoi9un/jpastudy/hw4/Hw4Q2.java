package kr.hoi9un.jpastudy.hw4;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class Hw4Q2 {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-study");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();

			User user1 = new User("same_id", "1@test.com", "젊은_엘란", 20);
			em.persist(user1);

			User user2 = new User("same_id", "2@test.com", "늙은_엘란", 30);
			em.persist(user2);

			tx.commit();
		} catch (Exception e) {
			System.out.println("Error: " + e);
			if (tx.isActive()) {
				tx.rollback();
			}
		} finally {
			em.close();
			emf.close();
		}
	}
}
