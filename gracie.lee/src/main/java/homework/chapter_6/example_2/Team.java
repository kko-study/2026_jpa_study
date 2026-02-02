package homework.chapter_6.example_2;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 일대다 단방향 예제 - Team (연관관계 주인)
 *
 * 특징: "1" 쪽에서 FK를 관리한다
 * 단점: Member 테이블에 UPDATE 쿼리가 추가로 발생
 */
@Entity(name = "Ch6Ex2Team")
@Table(name = "CH6_EX2_TEAM")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    @OneToMany
    @JoinColumn(name = "TEAM_ID")  // Member 테이블의 FK 컬럼
    private List<Member> members = new ArrayList<>();

    protected Team() {
    }

    public Team(String name) {
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

    public void addMember(Member member) {
        members.add(member);
    }
}
