package hw.h04;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

/**
 * 2. DDL 자동 생성 기능을 활용한 제약조건 설정과 테스트 코드를 작성하세요.
 *
 * 요구사항:
 * User 엔티티 생성
 * loginId: UNIQUE 제약조건
 * email: UNIQUE 제약조건
 * name + age 복합 UNIQUE 제약조건 (테이블 레벨)
 * age: 0 이상 150 이하 (CHECK 제약조건은 주석으로 표시)
 * persistence.xml의 hibernate.hbm2ddl.auto 설정 포함
 * 테스트: 같은 loginId로 두 번 저장 시도하여 예외 확인
 */
public class UserTest {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hw04");
        EntityManager em = emf.createEntityManager();

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        User user1 = new User("user001", "user1@test.com", "aaa", 20);
        em.persist(user1);
        transaction.commit();

        transaction = em.getTransaction();
        transaction.begin();
        try {
            User user2 = new User("user001", "user2@test.com", "bbb", 20);
            em.persist(user2);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            System.out.println("동일 userId");
        }

        transaction = em.getTransaction();
        transaction.begin();
        try {
            User user4 = new User("user002", "user4@test.com", "bbb", 20);
            em.persist(user4);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            System.out.println("동일 name, age");
        }

        em.close();
        emf.close();
    }
}
