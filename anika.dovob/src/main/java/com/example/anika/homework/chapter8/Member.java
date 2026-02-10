package com.example.anika.homework.chapter8;

import jakarta.persistence.*;

@Entity(name = "Ch8Member")
@Table(name = "member_ch8")
public class Member {

    @Id
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    protected Member() {
    }

    public Member(Long id, String username, Team team) {
        this.id = id;
        this.username = username;
        this.team = team;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Team getTeam() {
        return team;
    }
}
