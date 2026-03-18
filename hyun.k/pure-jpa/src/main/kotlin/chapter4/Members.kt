package chapter4

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Lob
import jakarta.persistence.Temporal
import java.util.Date

/**
 * ## 1. 다음 요구사항에 맞는 Member 엔티티 클래스를 작성하세요.
 * 요구사항:
 * - 테이블명: members
 * - id: 기본키, 자동 생성 (IDENTITY 전략)
 * - username: VARCHAR(50), NOT NULL, 컬럼명 "user_name"
 * - age: Integer, NULL 허용
 * - email: VARCHAR(100), UNIQUE 제약조건
 * - role: enum 타입 (USER, ADMIN), 문자열로 저장
 * - createdAt: 날짜 타입 (DATE만 저장)
 * - description: CLOB 타입
 * - tempData: DB에 매핑하지 않는 임시 필드
*/

@Entity(name = "members")
data class Members(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // IDENTITY = 기본 키 생성을 DB에 위임, 즉시 INSERT 쿼리 발생
    var id: Long? = null, // val로 만들면 위임이 불가능해짐.

    @Column(length = 50, nullable = false, name = "user_name")
    var username: String,

    @Column(nullable = true)
    val age: Int?,

    @Column(length = 100, unique = true)
    val email: String,

    @Enumerated(EnumType.STRING)
    val role: Role,

    // @Temporal => jarkarta에서 deprecated. java.time 모듈 네이티브 지원
    // https://jakarta.ee/specifications/persistence/3.2/apidocs/deprecated-list
    val createAt: Date,

    @Lob
    val description: String,

    @Transient
    val tempData: String

)