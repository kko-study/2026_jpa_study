package homework.chapter_6;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.time.LocalDateTime;

public class JpaMain {
	/**
	 * 1	@joincolumn 유무에 따른 일대다 단방향 동작 차이 확인
	 * 2	@OnetoOne에서 LAZY 로딩 동작 확인
	 * 3	다대다 관계를 중간 엔티티로 풀어서 추가 속성 관리
	 * 보너스1	일대일에서 FK 없는 쪽의 LAZY 로딩 한계 확인
	 * 보너스2	복합 키로 비즈니스 제약(1인 1회 구매) 구현
	 * 보너스3	일대다 단방향 vs 다대일 양방향 쿼리 효율 비교
	 */
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();

			/**
			 * @JoinColumn 없는 경우 - 일대다 단방향
			 * -> JPA가 FK 위치를 모르니까, 중간 테이블을 자동 생성
			 */
			Team team = new Team();
			team.setId("team1");
			team.setName("팀1");
			em.persist(team);

			Member member = new Member();
			member.setId("member1");
			member.setUsername("회원1");
			em.persist(member);

			/**
			 *     create table Team_Member (
			 *         Team_TEAM_ID varchar(255) not null,
			 *         members_MEMBER_ID varchar(255) not null unique
			 *     )
			 */

			/**
			 * @OnetoOne에서 LAZY 로딩 동작 확인
			 */
			Locker locker = new Locker();
			locker.setName("1번 라커");
			em.persist(locker);

			Member memberWithLocker = new Member();
			memberWithLocker.setId("member2");
			memberWithLocker.setUsername("클로드");
			memberWithLocker.setLocker(locker);
			em.persist(memberWithLocker);

			em.flush();
			em.clear();

			/**
			 * Lazy 동작함
			 * -> member 테이블에 locker_id 컬럼이 있으니, 조회 시 FK값이 있는지 바로 알 수 있음 → 프록시 생성 가능 → LAZY 동작
			 */
			Member findMember = em.find(Member.class, memberWithLocker.getId());
			System.out.println("### Member + Locker");
			System.out.println("#### Locker class: " + findMember.getLocker().getClass().getName());
			System.out.println("#### Locker name: " + findMember.getLocker().getName());

			/**
			 * Lazy 동작하지 않음 (일대일에서 FK 없는 쪽의 LAZY 로딩 한계 확인)
			 * -> locker 테이블에 member_id 컬럼이 없으니, 조회 시 FK값이 없으니 프록시 생성 불가 → EAGER 동작
			 */
			System.out.println("### Locker");
			Locker findLocker = em.find(Locker.class, locker.getId());
			System.out.println("#### Member class: " + findLocker.getMember().getClass());
			System.out.println("#### Member name: " + findLocker.getMember().getUsername());


			/**
			 * 다대다 관계를 중간 엔티티로 풀어서 추가 속성 관리
			 */
			Member member3 = new Member();
			member3.setId("member3");
			member3.setUsername("쿠팡");
			em.persist(member3);

			Product product1 = new Product();
			product1.setId("product1");
			product1.setName("아이폰");
			em.persist(product1);

			MemberProduct memberProduct = new MemberProduct();
			memberProduct.setMember(member3);
			memberProduct.setProduct(product1);
			memberProduct.setOrderAmount(10);
			em.persist(memberProduct);

			/**
			 *복합 키로 비즈니스 제약(1인 1회 구매) 구현
			 */
			Order order = new Order();
			order.setMember(member3);
			order.setProduct(product1);
			order.setOrderedAt(LocalDateTime.now());
			order.setPrice(3000);
			em.persist(order);

			em.flush();
			em.clear();

			/**
			 * 주문 실패
			 */
			Order reorder = new Order();
			reorder.setMember(member3);
			reorder.setProduct(product1);
			reorder.setOrderedAt(LocalDateTime.now());
			reorder.setPrice(3000);
			em.persist(reorder);

			em.flush();
			em.clear();

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
