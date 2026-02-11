package com.example.anika.homework.chapter7;

import jakarta.persistence.*;

@Entity(name = "Ch7NormalItem")
@Table(name = "normal_item_ch7")
@DiscriminatorValue("NORMAL")
public class NormalItem extends Item {

    @Column(nullable = false)
    private String sku;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    protected NormalItem() {
    }

    public NormalItem(String name, long basePrice, String sku, int stockQuantity) {
        super(name, basePrice);
        this.sku = sku;
        this.stockQuantity = stockQuantity;
    }

    public String getSku() {
        return sku;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }
}
