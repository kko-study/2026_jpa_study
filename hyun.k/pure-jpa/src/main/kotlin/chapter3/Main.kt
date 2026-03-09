package study.chapter3

import chapter3.Member
import jakarta.persistence.Persistence

fun main() {
    q1()
    q2()
}

private fun q1() {
    // 1. 회원 등록, 조회, 수정을 수행하는 전체 코드를 작성하세요.

    // EntityManagerFactory와 EntityManager 생성
    val emf = Persistence.createEntityManagerFactory("hyun-persistence")
    val em = emf.createEntityManager()
    val tx = em.transaction

    runCatching {

        tx.begin()

        // 회원(id=1L, name="홍길동") 저장
        val (id, name) = 1L to "홍길동"
        val member = Member(id = id, name = name)
        em.persist(member) // 영속 상태로 변경

        // 저장한 회원 조회
        val findMember = em.find(Member::class.java, id)

        // 회원 이름을 "김철수"로 변경 (변경 감지 활용)
        val changeName = "김철수"
        findMember.name = changeName
        val updatedMember = em.find(Member::class.java, id)

        require(updatedMember.name == changeName) { "회원 이름 변경 실패" }

    }.onFailure {
        tx.rollback()
    }.also {
        // 트랜잭션 커밋 및 자원 정리
        tx.commit()
        em.close()
        emf.close()
    }
}

private fun q2() {
    // 2. 1차 캐시와 동일성 보장을 테스트하는 코드를 작성하세요.

    val emf = Persistence.createEntityManagerFactory("hyun-persistence")
    val em = emf.createEntityManager()
    val tx = em.transaction

    runCatching {
        tx.begin()

        // 새로운 회원을 생성하여 영속 상태로 만들기
        val member = Member(id = 2L, name = "김태현")
        em.persist(member)

        // 같은 id로 두 번 조회하여 동일성(==) 비교
        val findMember1 = em.find(Member::class.java, 2L)
        val findMember2 = em.find(Member::class.java, 2L)
        require(findMember1 == findMember2)

        // 영속성 컨텍스트를 초기화(clear) 후 다시 조회
        em.clear()
        val findMember3 = em.find(Member::class.java, 2L)

        println("영속성 컨텍스트 초기화 후 객체 비교 : (findMember1 == findMember3) = ${findMember1 == findMember3}")

        // 각 단계마다 주석으로 예상되는 SQL 쿼리 여부 표시
        // ㄴ 1. persist 시점 : 영속성 컨텍스트에 MEMBER 저장, INSERT 쿼리 저장되어있음 (아직 DB로 날라가지 않음)
        // ㄴ 2. findMember1 : 1차 캐시에서 조회, SQL 쿼리 발생 X
        // ㄴ 3. findMember2 : 1차 캐시에서 조회, SQL 쿼리 발생 X
        // ㄴ 4. clear() : 영속성 컨텍스트 초기화, 1차 캐시 비워짐 (INSERT 쿼리도 같이 삭제됨)
        // ㄴ 5. findMember3 : 1차 캐시에서 조회 불가 -> DB에서 조회, SELECT 쿼리 발생
        // ㄴ 6. 따라서 findMember1 과 findMember3 는 다른 객체가 됨

    }.onFailure {
        tx.rollback()
    }.also {
        tx.commit()
        em.close()
        emf.close()
    }
}