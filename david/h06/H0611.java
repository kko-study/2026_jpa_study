package hw.h06;

import hw.h06.homework1.Member;
import hw.h06.homework1.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

/**
 * 숙제 1: @JoinColumn 토글
 * 목표: @JoinColumn 유무에 따른 일대다 단방향 동작 차이 확인
 *
 * 시나리오
 * 개발팀 ──┬── 홍길동
 *          └── 김철수
 *
 * 실습
 * 1. homework1/Team의 @JoinColumn이 주석 처리된 상태 확인
 * 2. Team "개발팀" 저장, Member "홍길동","김철수" 저장, team.getMembers().add() 호출, flush
 * 3. Hibernate SQL 로그 확인 (어떤 테이블이 생기고, 어떤 쿼리가 나가는지)
 * 4. @JoinColumn(name = "TEAM_ID") 주석 해제 후 다시 실행
 * 5. SQL 로그 비교:
 * - @JoinColumn 없음 > join table 생성 / flush 후 연관관계 insert
 * - @JoinColumn 있음 > MEMBER table FK 필드 추가 / flush 후 FK update
 */
public class H0611 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("h06");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 1. Team "개발팀" 저장
        Team team = new Team();
        team.setName("개발팀");
        em.persist(team);

        // 2. Member "홍길동", "김철수" 저장
        Member member1 = new Member();
        member1.setName("홍길동");
        em.persist(member1);

        Member member2 = new Member();
        member2.setName("김철수");
        em.persist(member2);

        // 3. team.getMembers().add(홍길동), team.getMembers().add(김철수) 호출
        team.getMembers().add(member1);
        team.getMembers().add(member2);

        // 4. flush - Hibernate SQL 로그 확인
        System.out.println("====flush====");
        em.flush();

        tx.commit();
        em.close();
        emf.close();
    }
}
