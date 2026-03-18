package org.example.lu_homework.chapter13

import org.example.lu_homework.chapter12.Member
import org.example.lu_homework.chapter12.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {

    // ===== Scenario A: 변경 감지 (Dirty Checking) =====

    /**
     * A-1) @Transactional이 있는 경우.
     * save()를 호출하지 않아도 변경 감지에 의해 UPDATE가 반영됩니다.
     */
    @Transactional
    fun changeName(memberId: Long, newName: String) {
        val member = memberRepository.findById(memberId)
            .orElseThrow { IllegalArgumentException("Member not found") }

        member.changeName(newName)
        // save() 호출 없음 — Dirty Checking
    }

    /**
     * A-2) @Transactional이 없는 경우.
     * 동일한 로직이지만 @Transactional이 없습니다.
     */
    fun changeNameWithoutTransaction(memberId: Long, newName: String) {
        val member = memberRepository.findById(memberId)
            .orElseThrow { IllegalArgumentException("Member not found") }

        member.changeName(newName)
    }

    // ===== Scenario B: 트랜잭션 밖 지연 로딩 =====

    /**
     * B-1) Member만 조회 (Team은 LAZY — 프록시 상태).
     * 트랜잭션 밖에서 getTeam().getName() 호출 시 LazyInitializationException 발생.
     */
    @Transactional(readOnly = true)
    fun findMember(memberId: Long): Member {
        return memberRepository.findById(memberId)
            .orElseThrow { IllegalArgumentException("Member not found") }
    }

    /**
     * B-2) Member + Team을 fetch join으로 함께 조회.
     * 트랜잭션 밖에서도 getTeam().getName() 정상 동작.
     */
    @Transactional(readOnly = true)
    fun findMemberWithTeam(id: Long): Member {
        return memberRepository.findWithTeamById(id)
            ?: throw IllegalArgumentException("Member not found")
    }
}