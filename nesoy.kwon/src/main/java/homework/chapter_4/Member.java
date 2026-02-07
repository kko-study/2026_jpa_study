package homework.chapter_4;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

import java.time.LocalDateTime;
import java.util.Date;

/**
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
@Table(name="members")
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String id;

	@Column(name = "user_name", length = 50, nullable = false)
	private String username;

	@Column(nullable = true)
	private Integer age;

	@Column(length = 100, unique = true)
	private String email;

	@Enumerated(EnumType.STRING)
	private Role role;

	/**
	 * Deprecated 되었다.
	 * java8 이후는 LocalDateTime 사용하면 된다.
	 */
	@Temporal(TemporalType.DATE)
	private Date createdAt;

	@Column
	private LocalDateTime newCreatedAt;

	@Lob
	private String description;

	@Transient
	private String tempData;


	public String getId() {
		return id;
	}

	public void setId(String id) {
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
}

enum Role {
	USER, ADMIN
}