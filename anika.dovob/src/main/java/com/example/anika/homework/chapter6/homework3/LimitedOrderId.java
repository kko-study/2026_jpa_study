package com.example.anika.homework.chapter6.homework3;

import java.io.Serializable;
import java.util.Objects;

public class LimitedOrderId implements Serializable {

    private Long memberId;
    private Long productId;

    public LimitedOrderId() {
    }

    public LimitedOrderId(Long memberId, Long productId) {
        this.memberId = memberId;
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LimitedOrderId that = (LimitedOrderId) o;
        return Objects.equals(memberId, that.memberId) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, productId);
    }
}
