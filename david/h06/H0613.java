package hw.h06;

import hw.h06.homework3.Member;
import hw.h06.homework3.Order;
import hw.h06.homework3.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.util.List;

/**
 * 숙제 3: 다대다 중간 엔티티
 * 목표: 다대다 관계를 중간 엔티티로 풀어서 추가 속성 관리
 *
 * 시나리오
 * 홍길동 ──┬── 주문1 (운동화 3개, 1/15)
 *          └── 주문2 (운동화 2개, 1/20)  ← 같은 상품 재주문!
 *
 * 실습
 * 1. Member "홍길동", Product "운동화" 저장
 * 2. Order 2개 생성 (주문1: 3개, 1/15 / 주문2: 2개, 1/20)
 * 3. flush/clear
 * 4. 확인: Order 테이블에 2개 row, 각 Order에 count, orderDate 확인
 */
public class H0613 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("h06");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 1. Member "홍길동", Product "운동화" 저장
        Member member = new Member();
        member.setName("홍길동");
        em.persist(member);

        Product product = new Product();
        product.setName("운동화");
        em.persist(product);

        // 2. Order 2개 생성 (주문1: 3개, 1/15 / 주문2: 2개, 1/20)
        Order order1 = new Order();
        order1.setMember(member);
        order1.setProduct(product);
        order1.setCount(3);
        order1.setDate(LocalDate.of(2025, 1, 15));
        em.persist(order1);

        Order order2 = new Order();
        order2.setMember(member);
        order2.setProduct(product);
        order2.setCount(2);
        order2.setDate(LocalDate.of(2025, 1, 20));
        em.persist(order2);

        // 3. flush/clear
        em.flush();
        em.clear();

        // 4. 확인: Order 테이블에 2개 row, 각 Order에 count, orderDate 확인
        List<Order> orders = em.createQuery(
                "select o from Order o where o.member.id = :memberId", Order.class)
                .setParameter("memberId", member.getId())
                .getResultList();

        System.out.println("주문 수: " + orders.size());
        for (Order order : orders) {
            System.out.println("주문 - 회원: " + order.getMember().getName()
                    + ", production: " + order.getProduct().getName()
                    + ", count: " + order.getCount()
                    + ", orderDate: " + order.getDate());
        }

        tx.commit();
        em.close();
        emf.close();
    }
}
