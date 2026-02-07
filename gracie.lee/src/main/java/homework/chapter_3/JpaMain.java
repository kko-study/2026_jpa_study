package homework.chapter_3;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

/**
 * 3장 숙제 1번: 회원 등록, 조회, 수정을 수행하는 전체 코드
 */
public class JpaMain {
    public static void main(String[] args) {
        // EntityManagerFactory와 EntityManager 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // 회원(id=1L, name="홍길동") 저장 (생성자로 필수값 주입)
            Member member = new Member(1L, "홍길동");
            em.persist(member);
            System.out.println("=== 회원 저장 완료 ===");

            // 저장한 회원 조회
            Member findMember = em.find(Member.class, 1L);
            System.out.println("조회한 회원: id=" + findMember.getId() + ", name=" + findMember.getName());

            // 회원 이름을 "김철수"로 변경 (변경 감지 활용)
            // JPA는 엔티티의 변경을 감지하여 트랜잭션 커밋 시점에 UPDATE SQL을 자동 생성
            findMember.setName("김철수");
            System.out.println("=== 회원 이름 변경 (변경 감지) ===");

            // 트랜잭션 커밋
            tx.commit();
            System.out.println("=== 트랜잭션 커밋 완료 ===");

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            // 자원 정리
            em.close();
            emf.close();
        }
    }
}