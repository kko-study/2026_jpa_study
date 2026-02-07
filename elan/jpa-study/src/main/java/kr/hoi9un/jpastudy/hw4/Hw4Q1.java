package kr.hoi9un.jpastudy.hw4;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class Hw4Q1 {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-study");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
        tx.begin();
        Member4 m = new Member4("엘란");
        em.persist(m);
        tx.commit();
        em.close();
        emf.close();
	}
}
