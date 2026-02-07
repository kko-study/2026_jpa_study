package homework.chapter_6.bonus2;

import java.io.Serializable;
import java.util.Objects;

/**
 * 복합 키 클래스
 *
 * 조건:
 * 1. Serializable 구현
 * 2. 기본 생성자 필요
 * 3. equals(), hashCode() 구현
 * 4. 필드명이 엔티티의 @Id 필드명과 동일해야 함
 */
public class EventOrderId implements Serializable {

    private Long member;   // EventOrder.member의 ID
    private Long product;  // EventOrder.product의 ID

    public EventOrderId() {
    }

    public EventOrderId(Long member, Long product) {
        this.member = member;
        this.product = product;
    }

    public Long getMember() {
        return member;
    }

    public Long getProduct() {
        return product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventOrderId that = (EventOrderId) o;
        return Objects.equals(member, that.member) && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, product);
    }
}
