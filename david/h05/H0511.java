package hw.h05;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

/**
 * 숙제 1: 다대일 양방향 구현
 * 요구사항
 * Team과 Member 엔티티를 생성하고, 다대일 양방향 관계를 설정하세요.
 *
 * Team 엔티티:
 *
 * id: 기본키, 자동 생성
 * name: 팀 이름
 * members: Member 리스트 (@OneToMany, mappedBy)
 * Member 엔티티:
 *
 * id: 기본키, 자동 생성
 * name: 회원 이름
 * team: Team 참조 (@ManyToOne, @JoinColumn)
 * 문제 1-1: FK 저장 확인
 * 목표: 연관관계 주인 쪽에서 FK를 설정하면 DB에 저장되는지 확인
 *
 * 1. Team "개발팀" 저장
 * 2. Member "홍길동" 저장
 * 3. member.setTeam(team) 호출
 * 4. flush/clear 후 Member를 다시 조회
 * 5. member.getTeam()이 "개발팀"인지 확인
 */
public class H0511 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("h05");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // Team "개발팀" 저장
        Team team = new Team();
        team.setName("개발팀");
        em.persist(team);

        // Member "홍길동" 저장
        Member member = new Member();
        member.setName("홍길동");
        em.persist(member);

        // member.setTeam(team) 호출
        member.setTeam(team);

        // flush/clear 후 Member를 다시 조회
        em.flush();
        em.clear();
        Member findMember = em.find(Member.class, member.getId());

        // member.getTeam()이 "개발팀"인지 확인
        System.out.println(findMember.getName());
        System.out.println(findMember.getTeam().getName());

        tx.commit();
        em.close();
        emf.close();
    }
}