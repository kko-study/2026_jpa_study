package com.example.anika.homework.chapter4;

import com.example.anika.homework.chapter4.entity.Member;
import com.example.anika.homework.chapter4.entity.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class Chapter4Application implements CommandLineRunner {

    private final EntityManagerFactory emf;

    public Chapter4Application(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static void main(String[] args) {
        SpringApplication.run(Chapter4Application.class, args);
    }

    @Override
    public void run(String... args) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Member member = new Member();
            member.setUsername("anika");
            member.setAge(99);
            member.setEmail("anika@example.com");
            member.setRole(Role.ADMIN);
            member.setCreatedAt(LocalDate.now());
            member.setDescription("anika.dovob");
            member.setTempData("temp data");

            em.persist(member);

            // 영속성 컨텍스트 초기화 후 조회
            em.flush();
            em.clear();

            // 조회
            Member findMember = em.find(Member.class, member.getId());
            System.out.println("Member: " + findMember);

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }
    }
}
