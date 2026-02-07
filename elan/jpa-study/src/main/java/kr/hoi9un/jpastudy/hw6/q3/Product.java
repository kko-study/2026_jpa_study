package kr.hoi9un.jpastudy.hw6.q3;

import jakarta.persistence.*;

@Entity(name = "Hw3Product")
@Table(name = "product_hw3")
public class Product {

    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private Long id;

    private String name;

    protected Product() {
    }

    public Product(String name) {
        this.name = name;
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
