package com.example.anika.homework.chapter6.homework3;

import jakarta.persistence.*;

@Entity(name = "Hw3LimitedOrder")
@Table(name = "limited_order_hw3")
@IdClass(LimitedOrderId.class)
public class LimitedOrder {

    @Id
    @Column(name = "member_id")
    private Long memberId;

    @Id
    @Column(name = "product_id")
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    private int count;

    public LimitedOrder() {
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
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

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setMember(Member member) {
        this.member = member;
        this.memberId = member.getId();
    }

    public void setProduct(Product product) {
        this.product = product;
        this.productId = product.getId();
    }

    public void setCount(int count) {
        this.count = count;
    }
}
