package com.example.joonbug.chapter5

import jakarta.persistence.EntityManagerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * 5장. 다대일 양방향 관계 테스트
 */
@Component
@Order(6)
class Chapter5Runner(
    private val entityManagerFactory: EntityManagerFactory
) : CommandLineRunner {

    override fun run(vararg args: String) {
        println("\n===== Chapter 5: 다대일 양방향 관계 테스트 =====")
        test1_1_FkSave()
        test1_2_FkUpdate()
        test1_3_OwnerImportance()
        test1_4_FirstLevelCacheProblem()
        test2_1_ToStringInfiniteLoop()
        test2_2_ToStringSolution()
    }

    /**
     * 문제 1-1: FK 저장 확인
     * 연관관계 주인 쪽에서 FK를 설정하면 DB에 저장되는지 확인
     */
    private fun test1_1_FkSave() {
        val em = entityManagerFactory.createEntityManager()
        val tx = em.transaction

        try {
            tx.begin()
            println("\n----- 문제 1-1: FK 저장 확인 -----")

            // 1. Team "개발팀" 저장
            val team = Team(name = "개발팀")
            em.persist(team)
            println("1. Team '개발팀' 저장 완료 (id=${team.id})")

            // 2. Member "홍길동" 저장
            val member = Member5(name = "홍길동")
            em.persist(member)
            println("2. Member '홍길동' 저장 완료 (id=${member.id})")

            // 3. member.setTeam(team) 호출
            member.team = team
            println("3. member.team = team 설정 완료")

            // 4. flush/clear 후 Member를 다시 조회
            em.flush()
            em.clear()
            println("4. flush/clear 완료")

            val foundMember = em.find(Member5::class.java, member.id)

            // 5. member.getTeam()이 "개발팀"인지 확인
            val foundTeamName = foundMember.team?.name
            println("5. 조회된 member.team.name = '$foundTeamName'")
            println("   결과: ${if (foundTeamName == "개발팀") "성공! FK가 DB에 저장됨" else "실패"}")

            tx.commit()
        } catch (e: Exception) {
            if (tx.isActive) tx.rollback()
            throw e
        } finally {
            em.close()
        }
    }

    /**
     * 문제 1-2: FK 업데이트 확인
     * 연관관계 변경이 DB에 반영되는지 확인
     */
    private fun test1_2_FkUpdate() {
        val em = entityManagerFactory.createEntityManager()
        val tx = em.transaction

        try {
            tx.begin()
            println("\n----- 문제 1-2: FK 업데이트 확인 -----")

            // 1. Team "개발팀", "기획팀" 저장
            val devTeam = Team(name = "개발팀")
            val planTeam = Team(name = "기획팀")
            em.persist(devTeam)
            em.persist(planTeam)
            println("1. Team '개발팀'(id=${devTeam.id}), '기획팀'(id=${planTeam.id}) 저장 완료")

            // 2. Member "홍길동"을 "개발팀"에 소속시킴
            val member = Member5(name = "홍길동", team = devTeam)
            em.persist(member)
            println("2. Member '홍길동'을 '개발팀'에 소속시켜 저장 완료")

            // 3. flush/clear
            em.flush()
            em.clear()
            println("3. flush/clear 완료")

            // 4. Member를 다시 조회해서 member.setTeam(기획팀) 호출
            val foundMember = em.find(Member5::class.java, member.id)
            val foundPlanTeam = em.find(Team::class.java, planTeam.id)
            println("4. Member 조회 완료 (현재 team: ${foundMember.team?.name})")

            foundMember.team = foundPlanTeam
            println("   member.team = 기획팀 설정 완료")

            // 5. flush/clear 후 Member를 다시 조회
            em.flush()
            em.clear()
            println("5. flush/clear 완료")

            val reFetchedMember = em.find(Member5::class.java, member.id)

            // 6. member.getTeam()이 "기획팀"으로 변경됐는지 확인
            val newTeamName = reFetchedMember.team?.name
            println("6. 조회된 member.team.name = '$newTeamName'")
            println("   결과: ${if (newTeamName == "기획팀") "성공! FK 업데이트가 DB에 반영됨" else "실패"}")

            tx.commit()
        } catch (e: Exception) {
            if (tx.isActive) tx.rollback()
            throw e
        } finally {
            em.close()
        }
    }

    /**
     * 문제 1-3: 연관관계 주인의 중요성
     * 주인이 아닌 쪽에서만 설정하면 DB에 반영 안 됨을 확인
     */
    private fun test1_3_OwnerImportance() {
        val em = entityManagerFactory.createEntityManager()
        val tx = em.transaction

        try {
            tx.begin()
            println("\n----- 문제 1-3: 연관관계 주인의 중요성 -----")

            // 1. Team, Member 저장
            val team = Team(name = "개발팀")
            val member = Member5(name = "홍길동")
            em.persist(team)
            em.persist(member)
            println("1. Team '개발팀', Member '홍길동' 저장 완료")

            // 2. team.getMembers().add(member)만 호출 (member.setTeam은 호출하지 않음)
            team.members.add(member)
            println("2. team.members.add(member)만 호출 (member.team 설정 안 함!)")

            // 3. flush/clear 후 Member를 다시 조회
            em.flush()
            em.clear()
            println("3. flush/clear 완료")

            val foundMember = em.find(Member5::class.java, member.id)

            // 4. member.getTeam()이 null인지 확인
            val foundTeam = foundMember.team
            println("4. 조회된 member.team = $foundTeam")
            println("   결과: ${if (foundTeam == null) "예상대로 null! 연관관계 주인이 아닌 쪽에서만 설정하면 DB에 반영 안 됨" else "예상과 다름"}")
            println()
            println("   [왜 null일까요?]")
            println("   - mappedBy가 설정된 쪽(Team.members)은 '읽기 전용'")
            println("   - DB의 FK(team_id)는 연관관계 주인인 Member.team이 관리")
            println("   - team.members.add()는 메모리상의 컬렉션만 변경, DB에는 영향 없음")

            tx.commit()
        } catch (e: Exception) {
            if (tx.isActive) tx.rollback()
            throw e
        } finally {
            em.close()
        }
    }

    /**
     * 문제 1-4: 1차 캐시 문제
     * 양쪽 다 설정해야 하는 이유 (1차 캐시 동기화)
     */
    private fun test1_4_FirstLevelCacheProblem() {
        val em = entityManagerFactory.createEntityManager()
        val tx = em.transaction

        try {
            tx.begin()
            println("\n----- 문제 1-4: 1차 캐시 문제 -----")

            // 1. Team, Member 저장
            val team = Team(name = "개발팀")
            val member = Member5(name = "홍길동")
            em.persist(team)
            em.persist(member)
            println("1. Team '개발팀', Member '홍길동' 저장 완료")

            // 2. member.setTeam(team)만 호출 (team.getMembers().add()는 호출하지 않음)
            member.team = team
            println("2. member.team = team만 호출 (team.members.add()는 호출 안 함!)")

            // 3. flush 전에 team.getMembers().size() 출력
            val sizeBeforeFlush = team.members.size
            println("3. [flush 전] team.members.size = $sizeBeforeFlush")

            // 4. flush/clear 후 Team을 다시 조회
            em.flush()
            em.clear()
            println("4. flush/clear 완료")

            val foundTeam = em.find(Team::class.java, team.id)

            // 5. team.getMembers().size() 출력
            val sizeAfterFlush = foundTeam.members.size
            println("5. [flush/clear 후] team.members.size = $sizeAfterFlush")

            println()
            println("   [3번과 5번의 결과가 다른 이유]")
            println("   - 3번(flush 전): team 객체는 1차 캐시에 있는 원본 객체")
            println("     -> team.members에 member를 추가하지 않았으므로 size = 0")
            println("   - 5번(flush/clear 후): DB에서 새로 조회한 Team 객체")
            println("     -> Lazy Loading으로 members를 DB에서 조회")
            println("     -> member.team_id가 team을 가리키므로 size = 1")
            println()
            println("   [결론: 양방향 연관관계는 양쪽 모두 설정해야 함]")
            println("   - member.team = team (DB 반영을 위해 필수)")
            println("   - team.members.add(member) (1차 캐시 동기화를 위해 권장)")

            tx.commit()
        } catch (e: Exception) {
            if (tx.isActive) tx.rollback()
            throw e
        } finally {
            em.close()
        }
    }

    /**
     * 문제 2-1: toString() 무한루프 확인
     * 양방향 toString()의 무한루프 문제 인식
     */
    private fun test2_1_ToStringInfiniteLoop() {
        val em = entityManagerFactory.createEntityManager()
        val tx = em.transaction

        try {
            tx.begin()
            println("\n----- 문제 2-1: toString() 무한루프 확인 -----")

            // 1. Team, Member 양방향 관계 설정
            val team = Team(name = "개발팀")
            val member = Member5(name = "홍길동", team = team)
            team.members.add(member)
            em.persist(team)
            em.persist(member)
            println("1. Team, Member 양방향 관계 설정 완료")

            // 2. flush/clear 후 Member 조회
            em.flush()
            em.clear()
            println("2. flush/clear 완료")

            val foundMember = em.find(Member5::class.java, member.id)
            println("3. Member 조회 완료")

            // 4. member.toStringWithLoop() 호출 -> StackOverflowError 발생
            println("4. member.toStringWithLoop() 호출 시도...")
            try {
                val result = foundMember.toStringWithLoop()
                println("   결과: $result")
                println("   예상과 다름 - StackOverflowError가 발생하지 않음")
            } catch (e: StackOverflowError) {
                println("   StackOverflowError 발생!")
                println()
                println("   [왜 무한루프가 발생할까요?]")
                println("   - Member.toStringWithLoop()이 team을 출력")
                println("   - Team.toStringWithLoop()이 members 리스트를 출력")
                println("   - members 리스트의 각 Member가 다시 toStringWithLoop() 호출")
                println("   - 무한 반복 -> StackOverflowError")
            }

            tx.commit()
        } catch (e: Exception) {
            if (tx.isActive) tx.rollback()
            if (e !is StackOverflowError) throw e
        } finally {
            em.close()
        }
    }

    /**
     * 문제 2-2: toString() 무한루프 해결
     * 연관 엔티티를 제외하거나 ID만 출력하도록 수정
     */
    private fun test2_2_ToStringSolution() {
        val em = entityManagerFactory.createEntityManager()
        val tx = em.transaction

        try {
            tx.begin()
            println("\n----- 문제 2-2: toString() 무한루프 해결 -----")

            // 1. Team, Member 양방향 관계 설정
            val team = Team(name = "개발팀")
            val member = Member5(name = "홍길동", team = team)
            team.members.add(member)
            em.persist(team)
            em.persist(member)
            println("1. Team, Member 양방향 관계 설정 완료")

            // 2. flush/clear 후 Member 조회
            em.flush()
            em.clear()
            println("2. flush/clear 완료")

            val foundMember = em.find(Member5::class.java, member.id)
            val foundTeam = em.find(Team::class.java, team.id)

            // 3. 해결된 toString() 호출 - ID만 출력
            println("3. 해결된 toString() 호출 (연관 엔티티는 ID만 출력)")
            println("   member.toString() = $foundMember")
            println("   team.toString() = $foundTeam")
            println()
            println("   [해결 방법]")
            println("   - 연관 엔티티 전체를 출력하지 말고 ID만 출력")
            println("   - 또는 연관 엔티티를 toString()에서 완전히 제외")
            println("   - Lombok 사용 시 @ToString.Exclude 활용")

            tx.commit()
        } catch (e: Exception) {
            if (tx.isActive) tx.rollback()
            throw e
        } finally {
            em.close()
        }
    }
}