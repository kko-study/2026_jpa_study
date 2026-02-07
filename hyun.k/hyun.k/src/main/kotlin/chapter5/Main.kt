package chapter5

import common.JpaUtil
import jakarta.persistence.EntityManager
import jakarta.persistence.Persistence

fun main() {

//    JpaUtil.executeInTransaction { em -> q1(em) }
//    JpaUtil.executeInTransaction { em -> q2(em) }
//    JpaUtil.executeInTransaction { em -> q3(em) }
//    JpaUtil.executeInTransaction { em -> q4(em) }
    JpaUtil.executeInTransaction { em -> q5(em) }

}

private fun applyFun(logic: (em: EntityManager) -> Unit) {
    val emf = Persistence.createEntityManagerFactory("hyun-persistence")
    val em = emf.createEntityManager()
    val tx = em.transaction

    runCatching {
        tx.begin()

        // 로직 수행
        logic(em)

    }.onFailure { e ->
        e.printStackTrace()
        tx.rollback()
    }.also {
        tx.commit()
        em.close()
        emf.close()
    }
}

// ### 문제 1-1: FK 저장 확인
// > 목표: 연관관계 주인 쪽에서 FK를 설정하면 DB에 저장되는지 확인
private fun q1(em: EntityManager) {
    // 연관관계의 주인 = FK를 가지고 있는 쪽 = Member (다대일 중 다쪽)

    // 1. Team "개발팀" 저장
    val team = Team(name = "개발팀")
    em.persist(team)

    val teamId = team.id

    // 2. Member "홍길동" 저장
    val member = Member(name = "홍길동") // 의도적으로 연관관계 세팅 안했음.
    em.persist(member)

    val memberId = member.id

    // 3. `member.setTeam(team)` 호출
    member.applyTeam(team)

    // 4. flush/clear 후 Member를 다시 조회
    em.flush()
    em.clear()
    val findMember = em.find(Member::class.java, memberId)

    // 5. `member.getTeam()`이 "개발팀"인지 확인
    require(findMember.team?.name == "개발팀")
}

// ### 문제 1-2: FK 업데이트 확인
// > 목표: 연관관계 변경이 DB에 반영되는지 확인
private fun q2(em: EntityManager) {
    // 1. Team "개발팀", "기획팀" 저장
    val devTeam = Team(name = "개발팀")
    val planTeam = Team(name = "기획팀")
    em.persist(devTeam)
    em.persist(planTeam)

    // 2. Member "홍길동"을 "개발팀"에 소속시킴
    val gildong = Member(name = "홍길동")
    gildong.applyTeam(devTeam)
    em.persist(gildong)

    val memberId = gildong.id!!

    // 3. flush/clear
    em.flush()
    em.clear()

    // 4. Member를 다시 조회해서 `member.setTeam(기획팀)` 호출
    val findMember1 = em.find(Member::class.java, memberId)
    findMember1.applyTeam(planTeam)
    em.persist(findMember1)

    // 5. flush/clear 후 Member를 다시 조회
    em.flush()
    em.clear()

    val findMember2 = em.find(Member::class.java, memberId)

    // 6. `member.getTeam()`이 "기획팀"으로 변경됐는지 확인
    require(findMember2.team?.name == "기획팀")
}

// ### 문제 1-3: 연관관계 주인의 중요성
// > 목표: 주인이 아닌 쪽에서만 설정하면 DB에 반영 안 됨을 확인
private fun q3(em: EntityManager) {
    // 1. Team, Member 저장
    val team = Team(name = "개발팀")
    em.persist(team)

    val member = Member(name = "홍길동")
    em.persist(member)

    // 2. `team.getMembers().add(member)`만 호출 (member.setTeam은 호출하지 않음)
    team.members.add(member)

    // 3. flush/clear 후 Member를 다시 조회
    em.flush()
    em.clear()

    val findMember = em.find(Member::class.java, member.id)

    // 4. `member.getTeam()`이 null인지 확인
    require(findMember.team == null)

    // 왜 null일까요?
    // ㄴ 연관관계의 주인은 다대일 관계에서 다쪽인 Member.
    // ㄴ 다쪽인 Member가 주인이 되고, FK를 관리하게 됨. 주인만 FK를 변경 (CRUD) 가능.
    // ㄴ 위 코드는 team 객체(주인이 아닌쪽)에 대해서만 FK 변경이 일어났기 때문에 DB에 반영되지 않음.
}

// ### 문제 1-4: 1차 캐시 문제
// > 목표: 양쪽 다 설정해야 하는 이유 (1차 캐시 동기화)
private fun q4(em: EntityManager) {
    // 1. Team, Member 저장
    val team = Team(name = "개발팀")
    em.persist(team)

    val member = Member(name = "홍길동")
    em.persist(member)

    // 2. `member.setTeam(team)`만 호출 (`team.getMembers().add()`는 호출하지 않음)
    member.team = team

    // 3. flush 전에 `team.getMembers().size()` 출력
    println("TEAM SIZE : ${team.members.size}")

    // 4. flush/clear 후 Team을 다시 조회
    em.flush()
    em.clear()

    val findTeam = em.find(Team::class.java, team.id)

    // 5. `team.getMembers().size()` 출력
    println("TEAM SIZE AFTER FLUSH : ${findTeam.members.size}")

    // 3번과 5번의 결과가 다른 이유는 무엇일까요?
    // 3: 0 출력, 5: 1 출력
    // ㄴ 1차 캐시에 member.setTeam(team)으로 team이 설정
    // ㄴ 하지만 team.getMembers()에는 member가 추가되지 않음.
    // ㄴ flush 이전에는 1차 캐시에서 조회하기 때문에 DB로 쿼리가 날라가지 않음. (확인 완료)
    // ㄴ 1차 캐시에는 team.members에 member가 없기 때문에 size()는 0을 반환.
    // ㄴ DB에 flush 될 때 member의 team_id가 설정되면서 DB에는 연관관계가 제대로 반영됨.

}


// ## 숙제 2: toString() 무한루프
//
// ### 요구사항
//
// `Team`과 `Member`에 서로를 출력하는 `toString()` 추가:
// - `Member.toString()`에서 `team` 출력
// - `Team.toString()`에서 `members` 출력
private fun q5(em: EntityManager) {
    // ### 문제 2-1: 무한루프 확인
    // > 목표: 양방향 toString()의 무한루프 문제 인식

    // 1. Team, Member 양방향 관계 설정
    val team = Team(name = "개발팀")
    em.persist(team)

    val member = Member(name = "홍길동")
    em.persist(member)

    team.members.add(member)
    member.team = team

    // 2. flush/clear 후 Member 조회
    em.flush()
    em.clear()

    val findMember = em.find(Member::class.java, member.id)

    // 3. `member.toString()` 호출

    runCatching { println(findMember.toString()) }
        .onFailure { ex ->
            // 4. `StackOverflowError` 발생하는지 확인
            check(ex is StackOverflowError)
        }

    // ### 문제 2-2: 해결 (선택)
    // toString()에서 연관 엔티티를 제외하거나 ID만 출력하도록 수정

    // 원인 : member.toString() -> team.toString() -> members.toString() -> member.toString() ... 무한루프
    // 해결 :
    // ㄴ toString 오버라이드 하면 됨.
    //   data class Member() {
    //      override fun toString(): String {
    //          return "Member(id=$id, name=$name, teamId=${team?.id})"  // team 객체 대신 ID만
    //      }
    //  }
}