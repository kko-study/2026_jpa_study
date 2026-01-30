package homework.chapter_6.homework2;

import jakarta.persistence.*;

@Entity(name = "Hw2Locker")
@Table(name = "locker_hw2")
public class Locker {

    @Id
    @GeneratedValue
    @Column(name = "locker_id")
    private Long id;

    private String name;

    protected Locker() {
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
