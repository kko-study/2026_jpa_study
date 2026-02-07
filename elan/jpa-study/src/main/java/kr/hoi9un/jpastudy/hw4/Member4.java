package kr.hoi9un.jpastudy.hw4;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "members")
@Getter
public class Member4 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", length = 50, nullable = false)
    @Setter
    private String username;

    @Setter
    private Integer age;

    @Column(length = 100, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Setter
    private Role role;

    @Temporal(TemporalType.DATE)
    @Setter
    private LocalDate createdAt;

    @Lob
    @Setter
    private String description;

    @Transient
    @Setter
    private String tempData;

    public Member4(String username) {
        this.username = username;
    }
}
