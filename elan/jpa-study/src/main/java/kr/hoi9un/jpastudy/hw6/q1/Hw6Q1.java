package kr.hoi9un.jpastudy.hw6.q1;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class Hw6Q1 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-study");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // 숙제 1: @JoinColumn 토글
            // 1) Team의 @JoinColumn이 주석 처리된 상태 확인
            // 2) Team "개발팀" 저장
            Team 개발팀 = new Team("개발팀");
            em.persist(개발팀);
            // 3) Member "홍길동", "김철수" 저장
            Member 홍길동 = new Member("홍길동");
            em.persist(홍길동);
            Member 김철수 = new Member("김철수");
            em.persist(김철수);
            // 4) team.getMembers().add(...) 두 번 호출
            개발팀.getMembers().add(홍길동);
            개발팀.getMembers().add(김철수);
            // 5) flush
            em.flush();
            // 6) SQL 로그 확인
            // 7) @JoinColumn(name = "team_id") 주석 해제 후 다시 실행
            // 매핑 테이블에서 member 테이블에 team_id 필드가 생성됨.
            // 8) SQL 로그 비교 (조인 테이블 생성, UPDATE 쿼리 여부)
//            # 첫번째
//            insert
//                    into
//            team_hw1_member_hw1
//                    (Hw1Team_team_id,members_member_id)
//            values
//                    (?,?)
//            # 두번째
//            [Hibernate]
//            update
//                    member_hw1
//            set
//            team_id=?
//                    where
//            member_id=?

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
