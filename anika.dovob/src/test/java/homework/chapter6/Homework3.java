package homework.chapter6;

import com.example.anika.homework.chapter6.Homework;
import com.example.anika.homework.chapter6.homework3.LimitedOrder;
import com.example.anika.homework.chapter6.homework3.Member;
import com.example.anika.homework.chapter6.homework3.Order;
import com.example.anika.homework.chapter6.homework3.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = Homework.class)
class Homework3 {

    @Autowired
    private EntityManagerFactory emf;

    private EntityManager em;
    private EntityTransaction tx;

    @BeforeEach
    void setUp() {
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();
    }

    @AfterEach
    void tearDown() {
        if (tx != null && tx.isActive()) {
            tx.rollback();
        }
        if (em != null) {
            em.close();
        }
    }

    @Test
    @DisplayName("숙제3: 다대다 관계를 중간 엔티티로 풀어서 추가 속성 관리")
    void test1() {
        // Member "홍길동" 저장
        Member member = new Member();
        member.setName("홍길동");
        em.persist(member);

        // Product "운동화" 저장
        Product product = new Product();
        product.setName("운동화");
        em.persist(product);

        // 주문1: 홍길동, 운동화, 3개, 1/15
        Order order1 = new Order();
        order1.setMember(member);
        order1.setProduct(product);
        order1.setCount(3);
        order1.setOrderDate(LocalDateTime.of(2026, 1, 15, 0, 0));
        em.persist(order1);

        // 주문2: 홍길동, 운동화, 2개, 1/20
        Order order2 = new Order();
        order2.setMember(member);
        order2.setProduct(product);
        order2.setCount(2);
        order2.setOrderDate(LocalDateTime.of(2026, 1, 20, 0, 0));
        em.persist(order2);

        // flush/clear
        em.flush();
        em.clear();

        // Order 테이블에 2개 row가 저장됐는가?
        // 각 Order에 count, orderDate가 제대로 들어있는가?
        Order foundOrder1 = em.find(Order.class, order1.getId());
        assertThat(foundOrder1.getCount()).isEqualTo(3);
        assertThat(foundOrder1.getOrderDate().getMonthValue()).isEqualTo(1);
        assertThat(foundOrder1.getOrderDate().getDayOfMonth()).isEqualTo(15);

        Order foundOrder2 = em.find(Order.class, order2.getId());
        assertThat(foundOrder2.getCount()).isEqualTo(2);
        assertThat(foundOrder2.getOrderDate().getMonthValue()).isEqualTo(1);
        assertThat(foundOrder2.getOrderDate().getDayOfMonth()).isEqualTo(20);
    }

    @Test
    @DisplayName("보너스 2: 복합 키 - 복합 키로 비즈니스 제약(1인 1회 구매) 구현")
    void bonus2() {
        Member member = new Member();
        member.setName("홍길동");
        em.persist(member);

        Product product = new Product();
        product.setName("한정판");
        em.persist(product);

        em.flush();

        // 홍길동이 한정판을 2번 구매 시도
        LimitedOrder order1 = new LimitedOrder();
        order1.setMember(member);
        order1.setProduct(product);
        order1.setCount(1);
        em.persist(order1);
        em.flush();

        LimitedOrder order2 = new LimitedOrder();
        order2.setMember(member);
        order2.setProduct(product);
        order2.setCount(1);

        // 복합 키 중복으로 EntityExistsException 발생
        assertThatThrownBy(() -> em.persist(order2))
                .isInstanceOf(jakarta.persistence.EntityExistsException.class);
    }
}
