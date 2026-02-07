package hw.h06;

import hw.h06.homework2.Locker;
import hw.h06.homework2.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

/**
 * 숙제 2: 일대일 지연 로딩
 * 목표: @OneToOne에서 LAZY 로딩 동작 확인
 *
 * 시나리오
 * 홍길동 ──── 101번 사물함
 *
 * 실습
 * 1. homework2/Member에서 @OneToOne만 있는 상태 확인
 * 2. Locker "101번" 저장, Member "홍길동" 저장 + setLocker
 * 3. flush/clear 후 Member 조회
 * 4. println("=== Member 조회 끝 ===") 기준으로 Locker SELECT 타이밍 확인
 * 5. @OneToOne(fetch = FetchType.LAZY)로 수정 후 다시 실행하여 비교
 * - getLocker 시점에 select 쿼리 실행
 */
public class H0612 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("h06");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 2. Locker "101번" 저장, Member "홍길동" 저장 + setLocker
        Locker locker = new Locker();
        locker.setName("101번");
        em.persist(locker);

        Member member = new Member();
        member.setName("홍길동");
        member.setLocker(locker);
        em.persist(member);

        // 3. flush/clear 후 Member 조회
        em.flush();
        em.clear();
        Member findMember = em.find(Member.class, member.getId());
        System.out.println("=== Member 조회 끝 ===");

        // 4. println("=== Member 조회 끝 ===") 기준으로 Locker SELECT 타이밍 확인
        System.out.println(findMember.getLocker().getName());

        tx.commit();
        em.close();
        emf.close();
    }
}
