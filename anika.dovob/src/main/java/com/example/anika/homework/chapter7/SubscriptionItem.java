package com.example.anika.homework.chapter7;

import jakarta.persistence.*;

@Entity(name = "Ch7SubscriptionItem")
@Table(name = "subscription_item_ch7")
@DiscriminatorValue("SUBSCRIPTION")
public class SubscriptionItem extends Item {

    @Column(name = "billing_cycle_days", nullable = false)
    private int billingCycleDays;

    @Column(name = "trial_days", nullable = false)
    private int trialDays;

    protected SubscriptionItem() {
    }

    public SubscriptionItem(String name, long basePrice, int billingCycleDays, int trialDays) {
        super(name, basePrice);
        this.billingCycleDays = billingCycleDays;
        this.trialDays = trialDays;
    }

    public int getBillingCycleDays() {
        return billingCycleDays;
    }

    public int getTrialDays() {
        return trialDays;
    }
}
