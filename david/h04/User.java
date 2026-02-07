package hw.h04;

import jakarta.persistence.*;

@Entity
@Table(name = "user_tmp",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_NAME_AGE", columnNames = {"name", "age"})
    }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String loginId;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    // 제약 조건 : 0 이상 150 이하
    private Integer age;

    public User() {}

    public User(String loginId, String email, String name, Integer age) {
        this.loginId = loginId;
        this.email = email;
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", loginId='" + loginId + "', name='" + name + "'}";
    }
}
