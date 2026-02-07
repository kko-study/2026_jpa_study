package homework.chapter_4;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * 4장 숙제 1번: 다양한 매핑 어노테이션을 사용한 Member 엔티티
 *
 * ========================================
 * 🟡 핵심 학습 포인트: JPA 엔티티 매핑 어노테이션
 * ========================================
 *
 * 1. @Entity: 이 클래스가 JPA 엔티티임을 선언
 *    - name 속성: 엔티티 이름 지정 (JPQL에서 사용)
 *    - 다른 패키지에 같은 클래스명 있으면 name으로 구분
 *
 * 2. @Table: 엔티티와 매핑할 테이블 지정
 *    - name 속성: 실제 DB 테이블명
 *
 * 3. @Id + @GeneratedValue: 기본키 매핑
 *    - IDENTITY: DB에 위임 (MySQL AUTO_INCREMENT)
 *    - SEQUENCE: 시퀀스 사용 (Oracle, PostgreSQL)
 *    - TABLE: 키 생성 테이블 사용
 *    - AUTO: DB 방언에 따라 자동 선택
 *
 * 4. @Column: 필드-컬럼 매핑
 *    - name: 컬럼명
 *    - length: 문자열 길이 (VARCHAR)
 *    - nullable: NULL 허용 여부 (DDL)
 *    - unique: UNIQUE 제약조건 (DDL)
 *
 * 5. @Enumerated: enum 타입 매핑
 *    - ⚠️ ORDINAL(기본값): 순서(0,1,2...) 저장 → 위험!
 *    - ✅ STRING: 이름 저장 → 권장!
 *
 * 6. @Lob: CLOB, BLOB 매핑
 *    - String, char[] → CLOB
 *    - byte[] → BLOB
 *
 * 7. @Transient: DB에 매핑하지 않음
 *    - 임시 데이터, 계산 필드 등에 사용
 *
 * ========================================
 */
@Entity(name = "Chapter4Member")  // 📚 엔티티 이름 (chapter_3.Member와 구분)
@Table(name = "members")          // 📚 테이블명 지정
public class Member {

    // ========================================
    // 📚 기본키 매핑: IDENTITY 전략
    // ========================================
    // IDENTITY: DB가 기본키 생성 (AUTO_INCREMENT)
    // ⚠️ 특징: persist() 시점에 즉시 INSERT 실행!
    //    (ID를 알아야 영속성 컨텍스트에서 관리 가능하므로)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ========================================
    // 📚 컬럼 매핑: 이름, 길이, NOT NULL
    // ========================================
    // DDL 생성: user_name VARCHAR(50) NOT NULL
    @Column(name = "user_name", length = 50, nullable = false)
    private String username;

    // 📚 기본 매핑: 필드명 = 컬럼명, NULL 허용
    private Integer age;

    // ========================================
    // 📚 UNIQUE 제약조건 (컬럼 레벨)
    // ========================================
    // DDL 생성: email VARCHAR(100) UNIQUE
    @Column(length = 100, unique = true)
    private String email;

    // ========================================
    // 📚 Enum 매핑: 반드시 STRING 사용!
    // ========================================
    // ORDINAL(기본값): USER=0, ADMIN=1 저장
    //   → enum 순서 바뀌면 데이터 꼬임! ❌
    // STRING: "USER", "ADMIN" 문자열 저장
    //   → 순서 바뀌어도 안전 ✅
    @Enumerated(EnumType.STRING)
    private Role role;

    // 📚 날짜 타입: LocalDate → DATE 타입으로 자동 매핑
    // (Java 8+의 java.time 패키지는 @Temporal 불필요)
    @Column(name = "created_at")
    private LocalDate createdAt;

    // ========================================
    // 📚 @Lob: 대용량 데이터 (CLOB)
    // ========================================
    // String에 @Lob → CLOB 타입
    // byte[]에 @Lob → BLOB 타입
    @Lob
    private String description;

    // ========================================
    // 📚 @Transient: DB 매핑 제외
    // ========================================
    // 이 필드는 DB에 저장되지 않음
    // 임시 데이터, 계산 결과 등에 사용
    @Transient
    private String tempData;

    // ========================================
    // 📚 생성자 설계: protected 기본 생성자
    // ========================================
    // JPA 스펙: 기본 생성자 필수 (public 또는 protected)
    // protected 권장: 외부에서 불완전한 객체 생성 방지
    protected Member() {
    }

    // 📚 필수값(username)을 받는 생성자
    // → new Member() 대신 new Member("홍길동") 사용 강제
    public Member(String username) {
        this.username = username;
    }

    // 📚 email은 불변 필드 → 생성자에서만 설정
    public Member(String username, String email) {
        this.username = username;
        this.email = email;
    }

    // Getter (모든 필드)
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Integer getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public String getDescription() {
        return description;
    }

    public String getTempData() {
        return tempData;
    }

    // ========================================
    // 📚 Setter 설계: 변경 가능한 필드만!
    // ========================================
    // setId() 없음: 기본키는 변경하면 안 됨
    // setEmail() 없음: email은 불변 (UNIQUE 필드)
    public void setUsername(String username) {
        this.username = username;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTempData(String tempData) {
        this.tempData = tempData;
    }
}
