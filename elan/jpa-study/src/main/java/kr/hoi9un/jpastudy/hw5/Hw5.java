package kr.hoi9un.jpastudy.hw5;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class Hw5 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-study");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Team5 team = new Team5("개발팀");
            em.persist(team);
            Member5 member = new Member5("홍길동");
            em.persist(member);
            member.setTeam(team);
            System.out.println("member: " + member);
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
