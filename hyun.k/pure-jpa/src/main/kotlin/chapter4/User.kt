package chapter4

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

/**
 * ## 2. DDL 자동 생성 기능을 활용한 제약조건 설정과 테스트 코드를 작성하세요.
 *
 * 요구사항:
 * - User 엔티티 생성
 * - loginId: UNIQUE 제약조건
 * - email: UNIQUE 제약조건
 * - name + age 복합 UNIQUE 제약조건 (테이블 레벨)
 * - age: 0 이상 150 이하 (CHECK 제약조건은 주석으로 표시)
 */

@Entity(name = "users")
@Table(
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["name", "age"])
    ]
)
data class User(

    @Column(unique = true)
    val email: String,

    val name: String,

    // 어플리케이션 레벨 : @field:Range(min = 0, max = 150)
    // 어플리케이션 레벨 : @field:Min(0) , @field:Max(150)
    // DB 레벨 (테이블 자체 규약) : @Check(constraints = "age >= 0 AND age <= 150")
    @Column(columnDefinition = "INT CHECK(age >= 0 AND age <= 150)")
    val age: Int
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    var longId: Long? = null
        protected set

    // 궁금증이 생겼음.
    // ㄴ IDENTITY 전략을 사용하면 DB에게 위임을 맡기기 때문에 val 타입으로 선언이 불가능하다.
    // ㄴ 그럼 var로 선언 및 Nullable 하게 관리해야하는데, ID 값은 불변이 필수이다.
    // ㄴ 어떻게 안전하게 관리할 수 있을까? 라는 고민에 대한 해결책이 protected set.
    // ㄴ JPA 공식 문서에서 lazy 로딩을 위해서 (프록시 객체 생성하는 방식이라) final 필드를 권장하지 않음.
    // ㄴ 그래서 코틀린에서는 all-open 플러그인이 필요하게됨 (코틀린은 클래스가 default로 final 이기 때문)
    // ㄴ 잘 정리된 블로그 : https://jhzlo.tistory.com/73
}