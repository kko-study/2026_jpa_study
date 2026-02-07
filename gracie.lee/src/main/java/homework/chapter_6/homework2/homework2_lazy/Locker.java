package homework.chapter_6.homework2.homework2_lazy;

import jakarta.persistence.*;

@Entity(name = "Hw2LazyLocker")
@Table(name = "locker_hw2_lazy")
public class Locker {

    @Id
    @GeneratedValue
    @Column(name = "locker_id")
    private Long id;

    private String name;

    protected Locker() {
    }

    public Locker(String name) {
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
