package kr.hoi9un.jpastudy.hw6.q3;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Hw3Member")
@Table(name = "member_hw3")
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "member")  // ← Order.member 필드를 가리킴 (읽기 전용)
    private List<Order> orders = new ArrayList<>();

    protected Member() {
    }

    public Member(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setName(String name) {
        this.name = name;
    }
}
