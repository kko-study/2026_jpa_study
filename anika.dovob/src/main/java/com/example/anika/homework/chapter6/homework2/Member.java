package com.example.anika.homework.chapter6.homework2;

import jakarta.persistence.*;

@Entity(name = "Hw2Member")
@Table(name = "member_hw2")
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @OneToOne  // ← fetch = FetchType.LAZY 추가해서 지연 로딩 확인!
//    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locker_id")
    private Locker locker;

    public Member() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Locker getLocker() {
        return locker;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocker(Locker locker) {
        this.locker = locker;
    }
}