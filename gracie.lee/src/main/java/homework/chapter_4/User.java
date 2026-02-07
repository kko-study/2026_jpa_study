package homework.chapter_4;

import jakarta.persistence.*;

/**
 * 4장 숙제 2번: DDL 자동 생성 기능을 활용한 제약조건 설정
 *
 * ========================================
 * 🟡 핵심 학습 포인트: UNIQUE 제약조건 설정 방법
 * ========================================
 *
 * 1. 컬럼 레벨 UNIQUE (@Column)
 *    - 단일 컬럼에 UNIQUE 적용
 *    - @Column(unique = true)
 *    - 제약조건 이름 지정 불가 (자동 생성)
 *
 * 2. 테이블 레벨 UNIQUE (@Table)
 *    - 복합 컬럼에 UNIQUE 적용 가능
 *    - @UniqueConstraint(columnNames = {"col1", "col2"})
 *    - 제약조건 이름 지정 가능 (name 속성)
 *
 * 3. DDL 자동 생성 옵션 (persistence.xml)
 *    - create: DROP + CREATE (개발 초기)
 *    - create-drop: 종료 시 DROP (테스트)
 *    - update: 변경분만 반영 (개발)
 *    - validate: 검증만 (운영)
 *    - none: 사용 안 함 (운영)
 *    ⚠️ 운영에서는 절대 create, create-drop, update 사용 금지!
 *
 * ========================================
 */
@Entity
@Table(name = "users",
        // ========================================
        // 📚 테이블 레벨 UNIQUE: 복합 유니크 제약조건
        // ========================================
        // name + age 조합이 유일해야 함
        // DDL: CONSTRAINT uk_name_age UNIQUE (name, age)
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_name_age",      // 제약조건 이름 지정 (가독성, 에러 메시지)
                        columnNames = {"name", "age"}  // 복합 컬럼
                )
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ========================================
    // 📚 컬럼 레벨 UNIQUE + NOT NULL
    // ========================================
    // DDL: loginId VARCHAR(255) NOT NULL UNIQUE
    // unique=true: 중복 불가
    // nullable=false: NULL 불가
    @Column(unique = true, nullable = false)
    private String loginId;

    // 📚 컬럼 레벨 UNIQUE (NULL 허용)
    // DDL: email VARCHAR(255) UNIQUE
    @Column(unique = true)
    private String email;

    private String name;

    // ========================================
    // 📚 CHECK 제약조건 (주석으로 표시)
    // ========================================
    // DDL: CHECK (age >= 0 AND age <= 150)
    // Hibernate 6.x: @Check(constraints = "age >= 0 AND age <= 150")
    // ⚠️ CHECK 제약조건은 DB마다 지원 여부 다름
    private Integer age;

    // ========================================
    // 📚 protected 기본 생성자
    // ========================================
    protected User() {
    }

    // 📚 필수값(loginId)만 받는 생성자
    public User(String loginId) {
        this.loginId = loginId;
    }

    // 📚 전체 필드를 받는 생성자
    public User(String loginId, String email, String name, Integer age) {
        this.loginId = loginId;
        this.email = email;
        this.name = name;
        this.age = age;
    }

    // Getter
    public Long getId() {
        return id;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    // ========================================
    // 📚 Setter: 변경 가능한 필드만!
    // ========================================
    // ❌ setLoginId() 없음: loginId는 식별자 역할 (불변)
    // ✅ setEmail() 있음: email은 변경 가능 (비즈니스 요구사항에 따라)

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
