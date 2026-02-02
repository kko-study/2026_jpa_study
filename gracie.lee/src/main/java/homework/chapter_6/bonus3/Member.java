package homework.chapter_6.bonus3;

import jakarta.persistence.*;

/**
 * 다대일 양방향: Member (연관관계 주인)
 *
 * FK(team_id)를 직접 관리 → INSERT 시 team_id 포함
 * → 별도 UPDATE 쿼리 불필요
 */
@Entity(name = "Bonus3Member")
@Table(name = "member_bonus3")
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "team_id")  // FK 직접 관리 (연관관계 주인)
    private Team team;

    protected Member() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Team getTeam() {
        return team;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
