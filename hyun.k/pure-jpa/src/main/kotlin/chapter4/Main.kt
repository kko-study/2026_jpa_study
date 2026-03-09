package chapter4

import jakarta.persistence.Persistence
import java.util.Date

fun main() {
    q1()
    q2()
}

private fun q1() {
    // 1. 회원 등록, 조회, 수정을 수행하는 전체 코드를 작성하세요.

    val emf = Persistence.createEntityManagerFactory("hyun-persistence")
    val em = emf.createEntityManager()
    val tx = em.transaction

    runCatching {
        tx.begin()

        val members = Members(
            username = "hyun",
            age = 29,
            email = "hyun.k@test.email.com",
            role = Role.ADMIN,
            createAt = Date(),
            description = "테스트입니다...",
            tempData = "임시 데이터",
        )

        em.persist(members)

        println("members 객체의 id값 : ${members.id}") // 1 출력되는 것 확인 완료.

    }.onFailure {
        tx.rollback()
    }.also {
        tx.commit()
        em.close()
        emf.close()
    }

}

private fun q2() {

    val emf = Persistence.createEntityManagerFactory("hyun-persistence")
    val em = emf.createEntityManager()
    val tx = em.transaction

    // 2. DDL 자동 생성 기능을 활용한 제약조건 설정과 테스트 코드를 작성하세요.

    runCatching {
        tx.begin()

        val user1 = User(
            email = "hyun.k@test.email.com",
            name = "hyun.k",
            age = 29
        )

        em.persist(user1)

        val userId = user1.longId!! // null 값이라면 exception 발생
        println("userId : $userId")

        em.clear() // user1은 이미 DB에 저장되어있음. (IDENTITY 전략이라 lazy가 아니기 때문에)

        em.persist(user1) // exception 발생 (id UNIQUE 제약조건 위반)

    }.onFailure {
        println("error : ${it.message}")
        // 실제 출력값 = { error : Detached entity passed to persist: chapter4.User }
        tx.rollback()
    }.also {
        tx.commit()
        em.close()
        emf.close()
    }
}