package com.example.anika.homework.chapter7;

import jakarta.persistence.*;

@Entity(name = "Ch7Item")
@Table(name = "item_ch7")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
public abstract class Item extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "base_price", nullable = false)
    private long basePrice;

    protected Item() {
    }

    public Item(String name, long basePrice) {
        this.name = name;
        this.basePrice = basePrice;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getBasePrice() {
        return basePrice;
    }
}
