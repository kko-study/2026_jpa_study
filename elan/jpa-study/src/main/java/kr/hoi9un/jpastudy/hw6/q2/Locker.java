package kr.hoi9un.jpastudy.hw6.q2;

import jakarta.persistence.*;

@Entity(name = "Hw2Locker")
@Table(name = "locker_hw2")
public class Locker {

    @Id
    @GeneratedValue
    @Column(name = "locker_id")
    private Long id;

    private String name;

    protected Locker() {
    }

    public Locker(String name) {
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
