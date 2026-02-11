package chapter7

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany

// 과제 내용을 이해하기론, Order와 Item의 다대다 매핑을 중간 엔티티를 통해 풀어내는 부분을 의도하신게 아닌가..
@Entity
class OrderItem(
) : BaseEntity() {
    // TODO: 식별 or 비식별 선택
    // ㄴ 비식별로 선택 (자체 pk 가짐)

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    var order: Order? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    var item: Item? = null

    var itemNameSnapshot: String = ""
    var unitPriceSnapshot: Long = 0
    var quantity: Int = 0

    @OneToMany(mappedBy = "orderItem")
    var options = mutableListOf<OrderItemOption>()
}