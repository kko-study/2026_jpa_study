package kr.hoi9un.jpastudy.hw6.q1;

import jakarta.persistence.*;

@Entity(name = "Hw1Member")
@Table(name = "member_hw1")
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    // 일대다 단방향: Member는 Team을 참조하지 않음!
    // (Team → Member 방향만 존재)

    protected Member() {
    }

    protected Member(String name) {
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
