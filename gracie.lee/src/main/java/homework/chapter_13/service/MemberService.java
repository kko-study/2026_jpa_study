package homework.chapter_13.service;

import homework.chapter_12.entity.Member;
import homework.chapter_12.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // ===== Scenario A: 변경 감지 (Dirty Checking) =====

    /**
     * A-1) @Transactional이 있는 경우.
     * save()를 호출하지 않아도 변경 감지에 의해 UPDATE가 반영됩니다.
     */
    @Transactional
    public void changeName(Long memberId, String newName) {
        // TODO: memberId로 Member를 조회하고, 이름을 newName으로 변경하세요.
        // save()를 호출하지 마세요!
    }

    /**
     * A-2) @Transactional이 없는 경우.
     * 동일한 로직이지만 @Transactional이 없습니다.
     */
    public void changeNameWithoutTransaction(Long memberId, String newName) {
        // TODO: memberId로 Member를 조회하고, 이름을 newName으로 변경하세요.
        // 위와 동일한 로직이지만 @Transactional이 없습니다.
    }

    // ===== Scenario B: 트랜잭션 밖 지연 로딩 =====

    /**
     * B-1) Member만 조회 (Team은 LAZY — 프록시 상태).
     * 트랜잭션 밖에서 getTeam().getName() 호출 시 LazyInitializationException 발생.
     */
    @Transactional(readOnly = true)
    public Member findMember(Long memberId) {
        // TODO: memberId로 Member를 조회해서 반환하세요.
        // Team은 접근하지 마세요! (LAZY 상태 유지)
        return null;
    }

    /**
     * B-2) Member + Team을 fetch join으로 함께 조회.
     * 트랜잭션 밖에서도 getTeam().getName() 정상 동작.
     *
     * 힌트: MemberRepository에 fetch join 쿼리 메서드를 추가해야 합니다.
     * 예) @Query("select m from Member m join fetch m.team where m.id = :id")
     *     Optional<Member> findWithTeamById(@Param("id") Long id);
     */
    @Transactional(readOnly = true)
    public Member findMemberWithTeam(Long memberId) {
        // TODO: Member를 조회하되, Team을 fetch join으로 함께 가져오세요.
        // Repository에 fetch join 쿼리 메서드를 추가해야 합니다.
        return null;
    }
}
