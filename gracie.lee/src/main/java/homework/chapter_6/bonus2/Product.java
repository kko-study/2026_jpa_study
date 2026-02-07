package homework.chapter_6.bonus2;

import jakarta.persistence.*;

@Entity(name = "Bonus2Product")
@Table(name = "product_bonus2")
public class Product {

    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private Long id;

    private String name;

    protected Product() {
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
