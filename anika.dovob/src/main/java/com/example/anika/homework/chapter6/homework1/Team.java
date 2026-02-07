package com.example.anika.homework.chapter6.homework1;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "Hw1Team")
@Table(name = "team_hw1")
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    private String name;

    @OneToMany
//    @JoinColumn(name = "team_id")
    private List<Member> members = new ArrayList<>();

    public Team() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setName(String name) {
        this.name = name;
    }
}
