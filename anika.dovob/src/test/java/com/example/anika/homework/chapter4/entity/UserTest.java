package com.example.anika.homework.chapter4.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;

    @BeforeAll
    static void setUpFactory() {
        emf = Persistence.createEntityManagerFactory("jpabook");
    }

    @AfterAll
    static void closeFactory() {
        if (emf != null) {
            emf.close();
        }
    }

    @BeforeEach
    void setUp() {
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();
    }

    @AfterEach
    void tearDown() {
        if (tx != null && tx.isActive()) {
            tx.rollback();
        }
        if (em != null) {
            em.close();
        }
    }

    @Test
    @DisplayName("같은 loginId로 두 번 저장하면 예외가 발생한다.")
    void uniqueLoginId() {
        // given
        User user1 = new User("user", "test1@example.com", "홍길동", 25);
        User user2 = new User("user", "test2@example.com", "김철수", 30);

        // when
        em.persist(user1);
        em.flush();

        // then
        assertThatThrownBy(() -> {
            em.persist(user2);
            em.flush();
        }).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("같은 email로 두 번 저장하면 예외가 발생한다.")
    void uniqueEmail() {
        // given
        User user1 = new User("user1", "test1@example.com", "홍길동", 25);
        User user2 = new User("user2", "test1@example.com", "김철수", 30);

        // when
        em.persist(user1);
        em.flush();

        // then
        assertThatThrownBy(() -> {
            em.persist(user2);
            em.flush();
        }).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("같은 name + age 조합으로 두 번 저장하면 예외가 발생한다.")
    void uniqueNameAndAge() {
        // given
        User user1 = new User("user1", "test1@example.com", "홍길동", 25);
        User user2 = new User("user2", "test2@example.com", "홍길동", 25);

        // when
        em.persist(user1);
        em.flush();

        // then
        assertThatThrownBy(() -> {
            em.persist(user2);
            em.flush();
        }).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("age가 0 미만이면 예외가 발생한다.")
    void checkAge() {
        // given
        User user = new User("user1", "test1@example.com", "홍길동", -1);

        // when & then
        assertThatThrownBy(() -> {
            em.persist(user);
            em.flush();
        }).isInstanceOf(ConstraintViolationException.class);
    }
}
