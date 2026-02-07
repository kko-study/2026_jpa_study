package com.example.joonbug.chapter6.homework2

import jakarta.persistence.*

/**
 * 숙제 2: 일대일 지연 로딩 - Locker 엔티티
 *
 * 보너스 1: 역방향 지연 로딩 테스트용
 * - FK 없는 쪽(Locker)에서 LAZY 로딩이 제대로 동작하는지 확인
 */
@Entity(name = "Hw2Locker")
@Table(name = "hw2_locker")
class Locker(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var name: String = "",

    /**
     * 보너스 1용: 역방향 일대일
     * FK가 없는 쪽에서는 LAZY가 동작하지 않을 수 있음
     */
    @OneToOne(mappedBy = "locker", fetch = FetchType.LAZY)
    var member: Member? = null
)