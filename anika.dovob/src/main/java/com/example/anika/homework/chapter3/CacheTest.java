package com.example.anika.homework.chapter3;

import com.example.anika.homework.chapter3.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class CacheTest {

    /**
     * 요구사항:
     * - 새로운 회원을 생성하여 영속 상태로 만들기
     * - 같은 id로 두 번 조회하여 동일성(==) 비교
     * - 영속성 컨텍스트를 초기화(clear) 후 다시 조회
     * - 초기화 전후의 엔티티가 같은 객체인지 비교하고 결과 출력
     * - 각 단계마다 주석으로 예상되는 SQL 쿼리 여부 표시
     */
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // 새로운 회원을 생성하여 영속 상태로 만들기
            // SQL 쿼리: INSERT 쓰기 지연 SQL 저장소에 저장
            Member member = new Member();
            member.setId(1L);
            member.setName("홍길동");
            em.persist(member);

            // 같은 id로 두 번 조회하여 동일성(==) 비교
            // SQL 쿼리: 없음 (1차 캐시에서 조회)
            Member findMember1 = em.find(Member.class, 1L);
            Member findMember2 = em.find(Member.class, 1L);
            System.out.println("findMember1 == findMember2: " + (findMember1 == findMember2));

            // 영속성 컨텍스트를 초기화(clear) 후 다시 조회
            // SQL 쿼리: INSERT 실행 (쓰기 지연)
            em.flush();
            em.clear();

            // SQL 쿼리: SELECT 실행 (영속성 컨텍스트 초기화로 1차 캐시에 없어서 DB에서 조회)
            Member findMember3 = em.find(Member.class, 1L);

            // 초기화 전후의 엔티티가 같은 객체인지 비교하고 결과 출력
            System.out.println("findMember1 == findMember3: " + (findMember1 == findMember3));

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
