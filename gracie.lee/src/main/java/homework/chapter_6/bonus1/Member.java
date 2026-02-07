package homework.chapter_6.bonus1;

import jakarta.persistence.*;

@Entity(name = "Bonus1Member")
@Table(name = "member_bonus1")
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locker_id")  // Member가 FK 보유
    private Locker locker;

    protected Member() {
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
