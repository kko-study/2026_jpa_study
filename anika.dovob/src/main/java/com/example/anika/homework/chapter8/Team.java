package com.example.anika.homework.chapter8;

import jakarta.persistence.*;

@Entity(name = "Ch8Team")
@Table(name = "team_ch8")
public class Team {

    @Id
    @Column(name = "team_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    protected Team() {
    }

    public Team(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
