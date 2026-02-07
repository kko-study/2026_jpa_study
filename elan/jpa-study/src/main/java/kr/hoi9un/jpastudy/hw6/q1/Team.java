package kr.hoi9un.jpastudy.hw6.q1;

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
//    @JoinColumn(name = "team_id")  // ← 주석 해제/처리해서 SQL 차이 확인!
    private List<Member> members = new ArrayList<>();

    protected Team() {
    }

    protected Team(String name) {
        this.name = name;
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
