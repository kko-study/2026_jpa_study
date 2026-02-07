package homework.chapter_6.homework2.homework2_lazy;

import jakarta.persistence.*;

@Entity(name = "Hw2LazyMember")
@Table(name = "member_hw2_lazy")
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @OneToOne(fetch = FetchType.LAZY)  // LAZY 로딩
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
