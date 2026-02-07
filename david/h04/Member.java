package hw.h04;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 1. 다음 요구사항에 맞는 Member 엔티티 클래스를 작성하세요.
 * 요구사항:
 *
 * 테이블명: members
 * id: 기본키, 자동 생성 (IDENTITY 전략)
 * username: VARCHAR(50), NOT NULL, 컬럼명 "user_name"
 * age: Integer, NULL 허용
 * email: VARCHAR(100), UNIQUE 제약조건
 * role: enum 타입 (USER, ADMIN), 문자열로 저장
 * createdAt: 날짜 타입 (DATE만 저장)
 * description: CLOB 타입
 * tempData: DB에 매핑하지 않는 임시 필드
 */
@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", length = 50, nullable = false)
    private String username;

    private Integer age;

    @Column(length = 100, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @Lob
    private String description;

    @Transient
    private String tempData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTempData() {
        return tempData;
    }

    public void setTempData(String tempData) {
        this.tempData = tempData;
    }
}
