package homework.chapter_6.bonus2;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 이벤트 주문 엔티티 (복합 키 사용)
 *
 * PK = (member_id, product_id)
 * → 같은 회원이 같은 상품을 2번 주문 불가 (1인 1회 제한)
 */
@Entity(name = "Bonus2EventOrder")
@Table(name = "event_order_bonus2")
@IdClass(EventOrderId.class)
public class EventOrder {

    @Id
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private LocalDateTime orderDate;

    protected EventOrder() {
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
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

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}
