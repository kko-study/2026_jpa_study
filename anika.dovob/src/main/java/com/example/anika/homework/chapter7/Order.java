package com.example.anika.homework.chapter7;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "Ch7Order")
@Table(name = "orders_ch7")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @Column(name = "order_no", unique = true, nullable = false)
    private String orderNo;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    protected Order() {
    }

    public Order(String orderNo) {
        this.orderNo = orderNo;
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public Long getId() {
        return id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
