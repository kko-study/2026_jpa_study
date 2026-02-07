package kr.hoi9un.jpastudy.hw6.q2;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class Hw6Q2 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-study");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // 숙제 2: 일대일 지연 로딩
            // 1) Member의 @OneToOne만 있는 상태 확인
            // 2) Locker "101번" 저장
            Locker locker = new Locker("101번");
            em.persist(locker);
            // 3) Member "홍길동" 저장, member.setLocker(locker) 호출
            Member 홍길동 = new Member("홍길동");
            홍길동.setLocker(locker);
            em.persist(홍길동);
            Long 홍길동id = 홍길동.getId();
            // 4) flush/clear
            em.flush();
            em.clear();
            // 5) Member 조회
            Member 다시홍길동 = em.find(Member.class, 홍길동id);
            // 6) println("=== Member 조회 끝 ===")
            System.out.println("=== Member 조회 끝 ===");
            // 7) member.getLocker().getName() 호출
            String lockerName = 다시홍길동.getLocker().getName();
            System.out.println("locker: " + lockerName);
            // 8) SQL 로그에서 Locker SELECT 타이밍 확인
            // 9) @OneToOne(fetch = FetchType.LAZY)로 변경 후 재실행
//            lazy
//            === Member 조회 끝 ===
//            [Hibernate]
//            select
//            l1_0.locker_id,
//                    l1_0.name
//            from
//            locker_hw2 l1_0
//            where
//            l1_0.locker_id=?
//            locker: 101번
            // 10) SELECT 타이밍 변화 확인

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
