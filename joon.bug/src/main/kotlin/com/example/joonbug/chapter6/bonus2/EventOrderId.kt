package com.example.joonbug.chapter6.bonus2

import java.io.Serializable

/**
 * 보너스 2: 복합 키 - EventOrder의 복합 키 클래스
 *
 * @IdClass에서 사용하는 복합 키는:
 * - Serializable 구현 필수
 * - equals(), hashCode() 구현 필수
 * - 기본 생성자 필수
 */
data class EventOrderId(
    val member: Long? = null,
    val product: Long? = null
) : Serializable