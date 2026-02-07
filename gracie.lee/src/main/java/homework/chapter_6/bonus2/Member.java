package homework.chapter_6.bonus2;

import jakarta.persistence.*;

@Entity(name = "Bonus2Member")
@Table(name = "member_bonus2")
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    protected Member() {
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
