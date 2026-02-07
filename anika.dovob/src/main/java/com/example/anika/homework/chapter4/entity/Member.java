package com.example.anika.homework.chapter4.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
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
@Entity
@Table(name = "members")
@Getter
@Setter
@ToString
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

    private LocalDate createdAt;

    @Lob
    private String description;

    @Transient
    private String tempData;
}
