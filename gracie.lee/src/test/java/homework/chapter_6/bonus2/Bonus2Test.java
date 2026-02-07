package homework.chapter_6.bonus2;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 보너스 2: 복합 키
 *
 * 목표: 복합 키로 비즈니스 제약(1인 1회 구매) 구현
 *
 * 숙제3:   일반 상품 → 재주문 가능 (대리 키)
 * 보너스2: 이벤트 상품 → 1인 1회만 (복합 키)
 *
 * 대안: @UniqueConstraint 사용 (더 간단함)
 * @Table(uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "product_id"}))
 * - PK는 대리 키(id) 사용
 * - (member_id, product_id) 조합에 유니크 제약조건만 추가
 * - @IdClass, EventOrderId 클래스 불필요
 */
class Bonus2Test {

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
    @DisplayName("복합 키: 같은 회원이 같은 이벤트 상품을 2번 구매 시도하면 예외 발생")
    void 복합키_중복_구매_제한() {
        // 1. Member "홍길동" 저장
        Member member = new Member();
        member.setName("홍길동");
        em.persist(member);

        // 2. Product "창립기념 한정판" 저장
        Product product = new Product();
        product.setName("창립기념 한정판");
        em.persist(product);

        // 3. 첫 번째 주문 - 성공
        EventOrder order1 = new EventOrder();
        order1.setMember(member);
        order1.setProduct(product);
        order1.setOrderDate(LocalDateTime.of(2024, 1, 15, 10, 0));
        em.persist(order1);

        em.flush();
        em.clear();

        // 4. 두 번째 주문 시도 - 같은 (회원, 상품) 조합
        EventOrder order2 = new EventOrder();
        order2.setMember(em.find(Member.class, member.getId()));
        order2.setProduct(em.find(Product.class, product.getId()));
        order2.setOrderDate(LocalDateTime.of(2024, 1, 20, 14, 30));

        // persist 시도 → 예외 발생!
        assertThrows(Exception.class, () -> {
            em.persist(order2);
            em.flush();  // flush 시점에 PK 중복 예외 발생
        });

        /*
         * 복합 키의 효과:
         * PK = (member_id, product_id)
         * → 같은 조합으로 2번 INSERT 불가
         * → DB 레벨에서 1인 1회 구매 제약 보장
         *
         * 숙제3(대리 키)와의 차이:
         * - 숙제3: PK = order_id (자동 생성) → 재주문 가능
         * - 보너스2: PK = (member_id, product_id) → 재주문 불가
         */
    }
}
