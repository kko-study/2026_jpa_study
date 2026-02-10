package com.example.anika.homework.chapter7;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "Ch7OrderItem")
@Table(name = "order_item_ch7")
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "item_name_snapshot", nullable = false)
    private String itemNameSnapshot;

    @Column(name = "unit_price_snapshot", nullable = false)
    private long unitPriceSnapshot;

    @Column(nullable = false)
    private int quantity;

    @ElementCollection
    @CollectionTable(
            name = "order_item_option_ch7",
            joinColumns = @JoinColumn(name = "order_item_id")
    )
    private Set<ItemOption> options = new HashSet<>();

    protected OrderItem() {
    }

    public OrderItem(Item item, int quantity) {
        this.item = item;
        this.itemNameSnapshot = item.getName();
        this.unitPriceSnapshot = item.getBasePrice();
        this.quantity = quantity;
    }

    public void addOption(ItemOption option) {
        options.add(option);
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    void setOrder(Order order) {
        this.order = order;
    }

    public Item getItem() {
        return item;
    }

    public String getItemNameSnapshot() {
        return itemNameSnapshot;
    }

    public long getUnitPriceSnapshot() {
        return unitPriceSnapshot;
    }

    public int getQuantity() {
        return quantity;
    }

    public Set<ItemOption> getOptions() {
        return options;
    }
}
