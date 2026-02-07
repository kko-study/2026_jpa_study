package homework.chapter_6.homework3;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 숙제 3: 다대다 중간 엔티티
 *
 * 목표: 다대다 관계를 중간 엔티티로 풀어서 추가 속성 관리
 *
 * 시나리오:
 *   홍길동 ──┬── 주문1 (운동화 3개, 1/15)
 *            └── 주문2 (운동화 2개, 1/20)  ← 같은 상품 재주문!
 */
class Homework3Test {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;

    @BeforeAll
    static void setUpFactory() {
        emf = Persistence.createEntityManagerFactory("jpabook");
    }

    @AfterAll
    static void closeFactory() {
        if (emf != null) emf.close();
    }

    @BeforeEach
    void setUp() {
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();
    }

    @AfterEach
    void tearDown() {
        if (tx.isActive()) tx.rollback();
        if (em != null) em.close();
    }

    @Test
    @DisplayName("다대다 중간 엔티티: 같은 상품 재주문 가능, 추가 속성(count, orderDate) 저장")
    void 다대다_중간엔티티_테스트() {
        // 유저 저장
        Member member = new Member();
        member.setName("홍길동");
        em.persist(member);

        // 제품 저장
        Product product = new Product();
        product.setName("운동화");
        em.persist(product);

        // 주문 저장 (같은 회원, 같은 상품, 다른 수량/날짜)
        Order order1 = new Order();
        order1.setMember(member);
        order1.setProduct(product);
        order1.setCount(1);
        order1.setOrderDate(LocalDateTime.of(2026, 2, 3, 0, 0));
        em.persist(order1);

        Order order2 = new Order();
        order2.setMember(member);
        order2.setProduct(product);
        order2.setCount(1);
        order2.setOrderDate(LocalDateTime.of(2024, 2, 3, 0, 30));
        em.persist(order2);

        em.flush();
        em.clear();

        // Order 테이블 적재 성공 체크 및 재주문 가능 확인
        List<Order> orders = em.createQuery(
                "SELECT o FROM Hw3Order o WHERE o.member.id = :memberId", Order.class)
                .setParameter("memberId", member.getId())
                .getResultList();

        assertEquals(2, orders.size(), "같은 회원이 같은 상품을 여러번 주문할 수 있어야 함");

        // 참고. 이 방법은 캐시 메모리 사용이라서 DB 조회가 아님
//        Member foundMember = em.find(Member.class, member.getId());
//        List<Order> orders = foundMember.getOrders();  // @OneToMany(mappedBy = "member")

        /*
         * 중간 엔티티의 장점:
         * 1. 같은 (Member, Product) 조합으로 여러 주문 가능 (재주문)
         * 2. 추가 속성(count, orderDate) 저장 가능
         * 3. @ManyToMany는 이런 추가 속성을 저장할 수 없음
         *
         * 테이블 구조:
         *   orders_hw3 (order_id, member_id, product_id, count, order_date)
         *   - PK: order_id (대리 키)
         *   - FK: member_id → member_hw3
         *   - FK: product_id → product_hw3
         */

//        Hibernate:
//        CREATE TABLE orders_hw3 (
//                count INTEGER NOT NULL,
//                member_id BIGINT,              -- FK
//                orderDate TIMESTAMP(6),
//                order_id BIGINT NOT NULL,      -- PK
//                product_id BIGINT,             -- FK
//                PRIMARY KEY (order_id)
//        )


    }
}
