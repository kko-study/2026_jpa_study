package com.example.joonbug.chapter6.homework1

import jakarta.persistence.*

/**
 * 숙제 1: 일대다 단방향 - Team 엔티티
 *
 * @JoinColumn 유무에 따른 동작 차이 확인:
 * - @JoinColumn 없음: 조인 테이블 생성 (team_members)
 * - @JoinColumn 있음: Member 테이블에 FK 컬럼 추가 + UPDATE 쿼리
 */
@Entity(name = "Hw1Team")
@Table(name = "hw1_team")
class Team(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var name: String = "",

    /**
     * 일대다 단방향: Team이 연관관계 주인
     *
     * 실습: 아래 @JoinColumn 주석을 토글하며 SQL 로그 비교
     * - 주석 상태: 조인 테이블(hw1_team_members) 생성
     * - 주석 해제: Member 테이블에 team_id FK + UPDATE 쿼리 발생
     */
    @OneToMany
    // @JoinColumn(name = "team_id")  // <- 주석 해제하여 비교
    var members: MutableList<Member> = mutableListOf()
)
