package chapter7

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("normal")
class NormalItem(): Item() {
    var sku: String = ""
    var stockQuantity: Int = 0
}