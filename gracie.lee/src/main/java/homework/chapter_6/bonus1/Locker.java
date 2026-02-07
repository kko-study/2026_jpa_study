package homework.chapter_6.bonus1;

import jakarta.persistence.*;

@Entity(name = "Bonus1Locker")
@Table(name = "locker_bonus1")
public class Locker {

    @Id
    @GeneratedValue
    @Column(name = "locker_id")
    private Long id;

    private String name;

    @OneToOne(mappedBy = "locker", fetch = FetchType.LAZY)  // FK 없는 쪽, LAZY 설정
    private Member member;

    protected Locker() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Member getMember() {
        return member;
    }

    public void setName(String name) {
        this.name = name;
    }
}
