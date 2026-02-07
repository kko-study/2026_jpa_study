package com.example.anika.homework.chapter6.homework2;

import jakarta.persistence.*;

@Entity(name = "Hw2Locker")
@Table(name = "locker_hw2")
public class Locker {

    @Id
    @GeneratedValue
    @Column(name = "locker_id")
    private Long id;

    private String name;

//    @OneToOne(mappedBy = "locker", fetch = FetchType.LAZY)
//    private Member member;

    public Locker() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

//    public Member getMember() {
//        return member;
//    }

    public void setName(String name) {
        this.name = name;
    }
}
