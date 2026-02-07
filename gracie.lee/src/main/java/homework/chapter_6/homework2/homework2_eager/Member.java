package homework.chapter_6.homework2.homework2_eager;

import jakarta.persistence.*;

@Entity(name = "Hw2EagerMember")
@Table(name = "member_hw2_eager")
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @OneToOne  // 기본값 = EAGER
    @JoinColumn(name = "locker_id")
    private Locker locker;

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

    public Locker getLocker() {
        return locker;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocker(Locker locker) {
        this.locker = locker;
    }
}
