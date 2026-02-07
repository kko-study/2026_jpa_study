package com.example.anika.homework.chapter4.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 요구사항:
 * - User 엔티티 생성
 * - loginId: UNIQUE 제약조건
 * - email: UNIQUE 제약조건
 * - name + age 복합 UNIQUE 제약조건 (테이블 레벨)
 * - age: 0 이상 150 이하 (CHECK 제약조건은 주석으로 표시)
 * - persistence.xml의 hibernate.hbm2ddl.auto 설정 포함
 * - 테스트: 같은 loginId로 두 번 저장 시도하여 예외 확인
 */
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "UQ_name_age", columnNames = {"name", "age"})
        },
        check = @CheckConstraint(name = "CK_age", constraint = "age >= 0 AND age <= 150")
)
@Getter
@Setter
@NoArgsConstructor
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

    @Column(nullable = false)
    private Integer age;

    public User(String loginId, String email, String name, Integer age) {
        this.loginId = loginId;
        this.email = email;
        this.name = name;
        this.age = age;
    }
}
