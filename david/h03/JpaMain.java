package hw.h03;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

/**
 * 1. 회원 등록, 조회, 수정을 수행하는 전체 코드를 작성하세요.
 *
 * 요구사항:
 * EntityManagerFactory와 EntityManager 생성
 * 회원(id=1L, name="홍길동") 저장
 * 저장한 회원 조회
 * 회원 이름을 "김철수"로 변경 (변경 감지 활용)
 * 트랜잭션 커밋 및 자원 정리
 */
public class JpaMain {
    public static void main(String[] args) {
        // EntityManagerFactory와 EntityManager 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("h03");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        // 회원(id=1L, name="홍길동") 저장
        Member member = new Member();
        member.setId(1L);
        member.setName("홍길동");
        em.persist(member);

        // 저장한 회원 조회
        Member findMember = em.find(Member.class, 1L);
        System.out.println("조회한 회원: " + findMember.getName());

        // 회원 이름을 "김철수"로 변경 (변경 감지 활용)
        findMember.setName("김철수");

        // 트랜잭션 커밋 및 자원 정리
        transaction.commit();
        em.close();
        emf.close();
    }
}
