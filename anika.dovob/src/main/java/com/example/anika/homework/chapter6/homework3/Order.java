package com.example.anika.homework.chapter6.homework3;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "Hw3Order")
@Table(name = "orders_hw3")
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;  // 다대다를 중간 엔티티로 풀기: Member → Order

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;  // 다대다를 중간 엔티티로 풀기: Product → Order

    private int count;              // 주문 수량

    private LocalDateTime orderDate; // 주문 일자

    public Order() {
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }

    public int getCount() {
        return count;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}
