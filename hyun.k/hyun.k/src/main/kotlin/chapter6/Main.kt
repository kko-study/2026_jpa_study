package chapter6

import chapter6.homework1.Member
import chapter6.homework1.Team
import chapter6.homework2.Locker2
import chapter6.homework2.Member2
import chapter6.homework3.Member3
import chapter6.homework3.Order
import chapter6.homework3.Product
import common.JpaUtil
import jakarta.persistence.EntityManager
import java.time.LocalDateTime

fun main() {
//    JpaUtil.executeInTransaction { em -> q1(em) }
//    JpaUtil.executeInTransaction { em -> q2(em) }
    JpaUtil.executeInTransaction { em -> q3(em) }
}

private fun q1(em: EntityManager) {
    // 1. `homework1/Team`의 `@JoinColumn`이 주석 처리된 상태 확인
    // ㄴ Member의 JoinColumn 주석 처리 완료.

    // 2. 테스트 작성:
    // - Team "개발팀" 저장
    val team = Team(name = "개발팀")
    em.persist(team)

    // - Member "홍길동", "김철수" 저장
    val member1 = Member(name = "홍길동")
    val member2 = Member(name = "김철수")
    em.persist(member1)
    em.persist(member2)

    // - `team.getMembers().add(홍길동)`, `team.getMembers().add(김철수)` 호출
    team.members.add(member1)
    team.members.add(member2)

    // - flush
    em.flush()

    // 3. Hibernate SQL 로그 확인 (어떤 테이블이 생기고, 어떤 쿼리가 나가는지)
    // ㄴ homework1/sql_log_before.sql 파일에 로그 복붙해놓음.

    // 4. `@JoinColumn(name = "team_id")` 주석 해제 후 다시 실행
    // ㄴ homework1/sql_log_after.sql 파일에 로그 복붙해놓음.

    // 차이점 : 조인 테이블 생성 유무, UPDATE 쿼리 발생 유무

    // 5. SQL 로그 비교:
    // - 조인 테이블이 생기는 경우는?
    // ㄴ @JoinColumn이 없어서 FK를 어디다 둬야 하는지 JPA는 모름.
    // ㄴ 그래서 조인 테이블을 하나 만들어서 Team과 Member를 연결함

    // - UPDATE 쿼리가 나가는 경우는?
    // ㄴ FK 컬럼은 Member 테이블에 존재하는데, Member 생성 시점에는 FK 값을 모르기 때문에 null로 insert
    // ㄴ 이후 Team에 Member를 추가하는 시점에 FK 값을 알게 되어 update 쿼리가 발생 (변경감지)

}

/**
 * OneToOne 기본 전락 = EAGER (즉시로딩)
 * LAZY로 바꾸면, 지연로딩 됨 => 성능 최적화 및 N+1 문제 해결
 */
private fun q2(em: EntityManager) {
    // 1. `homework2/Member`에서 `@OneToOne`만 있는 상태 확인
    // ㄴ 확인. Member class도 숙제에서 다 만들어주셨다는걸 이때 확인..

    // 2. 테스트 작성:
    // - Locker "101번" 저장
    val locker = Locker2(name = "101번")
    em.persist(locker)

    // - Member "홍길동" 저장, `member.setLocker(locker)` 호출
    val member = Member2(name = "홍길동")
    em.persist(member)
    member.locker2 = locker

    // - flush/clear
    em.flush()
    em.clear()

    // - Member 조회
    val findMember = em.find(Member2::class.java, member.id!!)

    // - `println("=== Member 조회 끝 ===")`
    println("=== Member 조회 끝 ===")

    // - `member.getLocker().getName()` 호출
    val lockerName = findMember.locker2?.name
    println("Locker name: $lockerName")

    // 3. Hibernate SQL 로그에서 Locker SELECT가 println 전/후 언제 나오는지 확인
    // ㄴ 전에 발생됨 (즉, EAGER이기 때문에 Member 조회 시점에 Locker도 같이 조회됨)

    // 4. `@OneToOne(fetch = FetchType.LAZY)`로 수정 후 다시 실행
    // ㄴ 수정 완료

    // 5. Locker SELECT 타이밍이 어떻게 바뀌는지 확인
    // ㄴ 처음에 테스트 했을 때 기존 EAGER과 동일하게 나왔음. 즉시 로딩 되었음.
    // ㄴ 확인해보니, Locker2 클래스를 data class로 생성했는데, data class는 프록시 생성이 안됨
    // ㄴ LAZY 로딩 동작 원리가 Hibernate가 프록시 객체를 생성해서 넣어두었다가 필요한 시점에 실제 엔티티를 DB에서 조회해오는 것인데,
    // ㄴ data class는 final class이기 때문에 프록시 생성이 불가능
    // ㄴ 그래서 data class를 일반 클래스로 바꾸고 타이밍 변경되는 부분 확인 완료. (allOpen 플러그인도 추가)
}

/**
 * 다대다를 중간 엔티티로 푸는 방법
 */
private fun q3(em: EntityManager) {
    // 1. `homework3/` 스켈레톤 코드 확인 (Member, Product, Order)

    // 2. 테스트 작성:
    // - Member "홍길동" 저장
    val member = Member3(name = "홍길동")
    em.persist(member)

    // - Product "운동화" 저장
    val product = Product(name = "운동화")
    em.persist(product)

    // - Order 2개 생성:
    // - 주문1: 홍길동, 운동화, 3개, 1/15
    // - 주문2: 홍길동, 운동화, 2개, 1/20 )
    val order1 = Order(
        member = member,
        product = product,
        count = 3,
        orderDate = LocalDateTime.now(),
    )
    em.persist(order1)

    val order2 = Order(
        member = member,
        product = product,
        count = 2,
        orderDate = LocalDateTime.now(),
    )
    em.persist(order2)

    // - flush/clear
    em.flush()
    em.clear()

    // 3. 확인:
    // - Order 테이블에 2개 row가 저장됐는가?
    val orders = em.createQuery("SELECT o FROM Hw3OrderKt o", Order::class.java)
        .getResultList()
    println("Order count: ${orders.size}")

    // - 각 Order에 count, orderDate가 제대로 들어있는가?
    orders.forEach { order ->
        println("Order ID: ${order.id}, Count: ${order.count}, Order Date: ${order.orderDate}")
    }
}

// 보너스 1. 역방향 지연 로딩
// ㄴ mappedBy가 들어갔다 = 연관관계에서 주인이 아니다. (즉 FK가 없다)
// ㄴ FK가 없는 쪽은 LAZY가 동작하지 않는다. 왜냐하면 누가 자신과 연관관계가 있는지 모른다 (즉, Locker 입장에서는 어떤 Member가 등록되어있는지 모른다)
// ㄴ 그래서 Hibernate는 원래라면 proxy 객체 만들어서 LAZY 동작해야 되는데, Member가 있는지 없는지 모름.
// ㄴ 그럼 프록시 객체를 만들어야하는지, null 값을 넣어야 하는지 hibernate는 결정할 수 없음 => 그래서 즉시 조회해서 확인함.

// 보너스 2. 복합 키 (@IdClass)

// 보너스 3. 일대다 단방향 단점
// (1) 일대다 단방향 (Team -> Members)
// ㄴ Member 테이블에 FK가 있어야 관계가 성립하는데, 단방향이라 FK 관리를 Team 컬렉션이 담당해야 함
// ㄴ `@JoinColumn`이 없으면: 중간 `조인 테이블`이 생김 => Insert 추가됨.
// ㄴ `@JoinColumn`이 있으면: 조인 테이블은 없지만 UPDATE가 생길 수 있음

// (2) 다대일 양방향 (Member -> Team이 주인)
// ㄴ FK는 Member가 들고 있고, 연관관계의 주인도 Member라서 “FK를 INSERT 때 바로 세팅” 가능
