package chapter7

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("subscription")
class SubscriptionItem() : Item() {
    var billingCycleDays: Int = 0
    var trialDays: Int = 0
}