package homework.chapter_3;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "MEMBER")
public class Member {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    private Integer age;

    // 기본 생성자 (JPA용, 외부 생성 방지)
    protected Member() {
    }

    // 필수값(id, name)을 받는 생성자
    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getter
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    // Setter (변경 가능한 필드만)
    // setId() 없음 - id는 생성 시점에만 설정 (기본키는 불변)

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}