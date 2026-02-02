package homework.chapter_6.example_2;

import jakarta.persistence.*;

/**
 * 일대다 단방향 예제 - Member
 *
 * Member는 Team을 모른다! (team 필드 없음)
 */
@Entity(name = "Ch6Ex2Member")
@Table(name = "CH6_EX2_MEMBER")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    private String name;

    // team 필드가 없음! (단방향이라 Team → Member만 가능)

    protected Member() {
    }

    public Member(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
