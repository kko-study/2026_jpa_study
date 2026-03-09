package chapter7

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
@Table(name = "orders")
class Order() : BaseEntity() {
    @Id
    @GeneratedValue
    var id: Long? = null

    @Column(unique = true, nullable = false)
    var orderNo: String = ""

    // TODO: OrderItem 연관관계
    @OneToMany(mappedBy = "order")
    var orderItems = mutableListOf<OrderItem>()
}