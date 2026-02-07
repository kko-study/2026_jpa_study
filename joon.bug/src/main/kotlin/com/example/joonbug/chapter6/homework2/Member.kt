package com.example.joonbug.chapter6.homework2

import jakarta.persistence.*

/**
 * 숙제 2: 일대일 지연 로딩 - Member 엔티티
 *
 * Member가 FK(locker_id)를 보유하는 주 테이블
 *
 * 실습: fetch 속성을 토글하며 SQL 로그 비교
 * - EAGER(기본값): Member 조회 시 Locker도 즉시 조회
 * - LAZY: Locker 접근 시점에 조회
 */
@Entity(name = "Hw2Member")
@Table(name = "hw2_member")
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var name: String = "",

    /**
     * 일대일 관계 - Member가 FK 보유 (연관관계 주인)
     *
     * 실습: 아래 fetch를 토글하며 SQL 로그 비교
     * - 현재 상태 (EAGER): Member 조회 시 Locker JOIN 또는 즉시 SELECT
     * - LAZY로 변경: locker 접근 시점에 SELECT
     */
    @OneToOne
    // @OneToOne(fetch = FetchType.LAZY)  // <- 주석 해제하여 비교
    @JoinColumn(name = "locker_id")
    var locker: Locker? = null
)