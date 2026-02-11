package chapter8

import common.JpaUtil
import jakarta.persistence.EntityManager

fun main() {
    JpaUtil.executeInTransaction { em -> scenario(em) }
}

private fun scenario(em: EntityManager) {
    // ## 2) 데이터 준비
    val teamA = Team().apply {
        id = 10L
        name = "team-A"
    }

    val member1 = Member().apply {
        id = 1L
        username = "member-1"
        team = teamA
    }

    em.persist(teamA)
    em.persist(member1)
    em.flush()
    em.clear()

    // ## 3) 실험 코드 + 추가 케이스
    // 3-1) 기본 실험 코드
    // - `teamProxy`가 프록시인지 여부: class chapter8.Team$HibernateProxy => 프록시 맞음
    // - SQL이 실행되는 시점 : 1번 출력문 이전에 실행됨
    // - 영속성 컨텍스트 관점에서 어떤 상태인지 : detached 상태 (name 출력 안되고 LazyInitializationException 에러남)
    val findMember = em.find(Member::class.java, 1L)

    val teamProxy = findMember.team
    println("1) class = " + teamProxy?.javaClass)

    em.flush()
    em.clear()

    println("2) class = " + teamProxy?.javaClass)
//    println("3) name = " + teamProxy?.name) // 에러 나기 때문에 필요할 때 주석 제거


    // ### 3-2) 추가 케이스 A: clear 이후 "새로" getReference 비교
    // ㄴ getReference의 의미 : 새 프록시 생성 => 영속성 컨텍스트로 관리됨.
    val findMember2 = em.find(Member::class.java, 1L)
    val teamId = findMember2.team?.id

    em.flush()
    em.clear()

    val teamRef = em.getReference(Team::class.java, teamId)
    println("A) class = " + teamRef.javaClass)
    println("B) name = " + teamRef.name)

    em.clear()

    // ### 3-3) 추가 케이스 B: 트랜잭션 경계 밖에서 접근
    // Spring이 아니라 순수 JPA 환경이라 PASS

    // ### 3-4) 아래 코드에서 위험한 줄은? (실행해보고 확인해봅시다!)
    val m = em.find(Member::class.java, 1L)
    val t = m.team
    em.clear()
//    t!!.name // 여기도 에러 터지므로 필요할 때 주석 제거
}