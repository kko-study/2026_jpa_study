package com.example.anika.homework.chapter6.homework3;

import jakarta.persistence.*;

@Entity(name = "Hw3Product")
@Table(name = "product_hw3")
public class Product {

    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private Long id;

    private String name;

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
