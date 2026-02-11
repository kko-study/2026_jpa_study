========= 3-4 시작 =========
========= Order 조회 =========
Hibernate:
select
    o1_0.id,
    o1_0.createdAt,
    o1_0.orderNo,
    o1_0.updatedAt
from
    orders o1_0
where
    o1_0.id=?
    ========= getOrderItems 접근 =========
Hibernate:
select
    oi1_0.order_id,
    oi1_0.order_item_id,
    oi1_0.createdAt,
    oi1_0.item_id,
    oi1_0.itemNameSnapshot,
    oi1_0.quantity,
    oi1_0.unitPriceSnapshot,
    oi1_0.updatedAt
from
    OrderItem oi1_0
where
    oi1_0.order_id=?
    ========= getItem 접근 =========
========= getItem 접근 =========
========= 3-4 종료 =========