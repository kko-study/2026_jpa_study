package kr.hoi9un.jpastudy.hw6.q3;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class Hw6Q3 {
    public static Order createOrder(
            Member member,
            Product product,
            int count,
            String simpleDate
    ) {
        Order order = new Order();
        order.setMember(member);
        order.setProduct(product);
        order.setCount(count);

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("M/d")
                .parseDefaulting(ChronoField.YEAR, 2026)           // 연도 기본값
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)      // 시 기본값
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)   // 분 기본값
                .toFormatter();

        order.setOrderDate(LocalDateTime.parse(simpleDate, formatter));
        return order;
    }
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-study");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // 숙제 3: 다대다 중간 엔티티
            // 1) Member "홍길동" 저장
            Member 홍길동 = new Member("홍길동");
            em.persist(홍길동);
            // 2) Product "운동화" 저장
            Product 운동화 = new Product("운동화");
            em.persist(운동화);
            // 3) Order 2개 생성
            //    - 주문1: 홍길동, 운동화, 3개, 1/15
            //    - 주문2: 홍길동, 운동화, 2개, 1/20
            Order order1 = createOrder(홍길동, 운동화, 3, "1/15");
            em.persist(order1);
            Order order2 = createOrder(홍길동, 운동화, 2, "1/20");
            em.persist(order2);
            // 4) flush/clear
            em.flush();
            em.clear();
            // 5) Order 테이블에 2개 row 저장 확인
            // 6) count, orderDate 저장값 확인
            Order order11 = em.find(Order.class, order1.getId());
            System.out.println(order11);
            Order order22 = em.find(Order.class, order2.getId());
            System.out.println(order22);

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
