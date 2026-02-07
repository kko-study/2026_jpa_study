package homework.chapter_4;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * User 엔티티 생성
 * loginId: UNIQUE 제약조건
 * email: UNIQUE 제약조건
 * name + age 복합 UNIQUE 제약조건 (테이블 레벨)
 * age: 0 이상 150 이하 (CHECK 제약조건은 주석으로 표시)
 * persistence.xml의 hibernate.hbm2ddl.auto 설정 포함
 * 테스트: 같은 loginId로 두 번 저장 시도하여 예외 확인
 */
@Entity
@Table(
		name = "user",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "uk_name_age",
						columnNames = {"name", "age"}
				)
		}
)
public class User {
	@Id
	private Long id;
	@Column(unique = true)
	private String loginId;
	@Column(unique = true)
	private String email;
	@Column(unique = true)
	private String name;

	/**
	 * CHECK(age >= 0 AND age <= 150)
	 */
	@Column
	private Integer age;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}

