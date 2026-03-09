package chapter7

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class OrderItemOption : BaseEntity() {
    @Id
    @GeneratedValue
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    var orderItem: OrderItem? = null

    @Column(name = "option_key")
    var key: String = ""

    @Column(name = "option_value")
    var value: String = ""
}
