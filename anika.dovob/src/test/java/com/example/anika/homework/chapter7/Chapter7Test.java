package com.example.anika.homework.chapter7;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.List;

class Chapter7Test {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;

    private NormalItem normalItem;
    private SubscriptionItem subscriptionItem;
    private Order order;

    @BeforeAll
    static void setUpFactory() {
        emf = Persistence.createEntityManagerFactory("jpabook");
    }

    @AfterAll
    static void closeFactory() {
        if (emf != null) {
            emf.close();
        }
    }

    @BeforeEach
    void setUp() {
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();
        setUpData();
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

    /**
     * 3-1. 데이터 준비
     */
    private void setUpData() {
        normalItem = new NormalItem("USB Cable", 9900, "USB-001", 100);
        subscriptionItem = new SubscriptionItem("Music Sub", 7900, 30, 7);
        em.persist(normalItem);
        em.persist(subscriptionItem);

        order = new Order("ORD-20260204-0001");

        OrderItem orderItem1 = new OrderItem(normalItem, 2);
        orderItem1.addOption(new ItemOption("color", "black"));
        orderItem1.addOption(new ItemOption("size", "M"));

        OrderItem orderItem2 = new OrderItem(subscriptionItem, 1);
        orderItem2.addOption(new ItemOption("trial", "true"));

        order.addOrderItem(orderItem1);
        order.addOrderItem(orderItem2);

        em.persist(order);
        em.flush();
        em.clear();
    }

    /**
     * 3-2. @MappedSuperclass 반영 확인
     */
    @Test
    void test2() {
        NormalItem found = em.find(NormalItem.class, normalItem.getId());

        // 실제 생성된 테이블에 `created_at`, `updated_at`(또는 본인 네이밍)이 들어갔는지 확인
        // `BaseEntity` 자체 테이블이 생기지 않았는지 확인
        Assertions.assertNotNull(found.getCreatedAt());
        Assertions.assertNotNull(found.getUpdatedAt());
    }

    /**
     * 3-3. 부모 타입(Item) 기준 다형성 조회 SQL 확인
     */
    @Test
    void test3() {
        // when
        List<Item> items = em.createQuery("select i from Ch7Item i order by i.id", Item.class)
                .getResultList();

        // then
        Assertions.assertEquals(2, items.size());
        Assertions.assertInstanceOf(NormalItem.class, items.get(0));
        Assertions.assertInstanceOf(SubscriptionItem.class, items.get(1));
    }

    /**
     * 3-4. 주문 1건 조회 시 OrderItem / Item 조회 방식 확인
     */
    @Test
    void test4() {
        // Order 단건 조회
        Order o = em.find(Order.class, order.getId());
        Assertions.assertNotNull(o);

        // o.getOrderItems() 접근 시점의 SQL 확인
        System.out.println("====== OrderItems 접근 ======");
        List<OrderItem> orderItems = o.getOrderItems();
        Assertions.assertEquals(2, orderItems.size());
    }
}
