package homework.chapter_5;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class JpaMain {
	/**
	 * ## 문제목표
	 * 1-1	연관관계 주인 쪽에서 FK 설정 시 DB 저장 확인
	 * 1-2	연관관계 변경이 DB에 반영되는지 확인
	 * 1-3	주인이 아닌 쪽에서만 설정하면 DB 반영 안 됨 확인
	 * 1-4	양쪽 다 설정해야 하는 이유 (1차 캐시 동기화)
	 * 2-1	양방향 toString() 무한루프 문제 인식
	 */
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			/**
			 * 연관관계 주인 쪽에서 FK 설정 시 DB 저장 확인
			 */
			Team team = new Team();
			team.setId("team1");
			team.setName("팀1");
			em.persist(team);

			Member member = new Member();
			member.setId("member1");
			member.setUsername("회원1");
			member.setTeam(team);
			em.persist(member);

			Member findMember = em.find(Member.class, "member1");
			System.out.println("### " + findMember);

			/**
			 * 연관관계 변경이 DB에 반영되는지 확인
			 */
			member.setTeam(null);
			System.out.println("### " + findMember);

			/**
			 * 주인이 아닌 쪽에서만 설정하면 DB 반영 안 됨 확인
			 */
			team.getMembers().add(member);
			System.out.println("### " + findMember);

			/**
			 * 양쪽 다 설정해야 하는 이유 (1차 캐시 동기화)
			 */
			Team team2 = new Team();
			team2.setId("team2");
			team2.setName("팀2");
			em.persist(team2);

			Member member2 = new Member();
			member2.setId("member2");
			member2.setUsername("회원2");
			member2.setTeam(team2);
			em.persist(member2);

			// 1차 캐시 조회
			Team findTeam2 = em.find(Team.class, "team2");
			System.out.println("### " + findTeam2);

			// 양쪽 다 설정
			Member member3 = new Member();
			member3.setId("member3");
			member3.setUsername("회원3");
			member3.setTeam(team2);
			team2.getMembers().add(member3);
			em.persist(member3);

			System.out.println("### " + findTeam2);

			/**
			 * 	양방향 toString() 무한루프 문제 인식
			 */
			System.out.println(member.toString());

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			em.close();
			emf.close();
		}
	}
}
