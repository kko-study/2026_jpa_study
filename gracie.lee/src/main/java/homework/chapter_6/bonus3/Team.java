package homework.chapter_6.bonus3;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 다대일 양방향: Team (읽기 전용)
 *
 * 숙제1(일대다 단방향)과 비교:
 * - 일대다 단방향: Team이 연관관계 주인 → UPDATE 쿼리 발생
 * - 다대일 양방향: Member가 연관관계 주인 → UPDATE 쿼리 없음
 */
@Entity(name = "Bonus3Team")
@Table(name = "team_bonus3")
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team")  // 읽기 전용 (연관관계 주인 아님)
    private List<Member> members = new ArrayList<>();

    protected Team() {
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
