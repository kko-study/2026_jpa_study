package hw.h03;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

/**
 * 2. 1차 캐시와 동일성 보장을 테스트하는 코드를 작성하세요.
 *
 * 요구사항:
 * 새로운 회원을 생성하여 영속 상태로 만들기
 * 같은 id로 두 번 조회하여 동일성(==) 비교
 * 영속성 컨텍스트를 초기화(clear) 후 다시 조회
 * 초기화 전후의 엔티티가 같은 객체인지 비교하고 결과 출력
 * 각 단계마다 주석으로 예상되는 SQL 쿼리 여부 표시
 */
public class CacheTest {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("h03");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        // 새로운 회원을 생성하여 영속 상태로 만들기
        Member member = new Member();
        member.setId(1L);
        member.setName("name");
        em.persist(member);

        // 같은 id로 두 번 조회하여 동일성(==) 비교
        Member findMember1 = em.find(Member.class, 1L);
        Member findMember2 = em.find(Member.class, 1L);
        System.out.println("동일성 비교 1 : " + (findMember1 == findMember2));  // true

        // 영속성 컨텍스트를 초기화(clear) 후 다시 조회
        em.flush();
        em.clear();
        Member findMember3 = em.find(Member.class, 1L);

        // 초기화 전후의 엔티티가 같은 객체인지 비교하고 결과 출력
        System.out.println("동일성 비교 2 : " + (findMember1 == findMember3));  // false

        transaction.commit();
        em.close();
        emf.close();
    }
}
