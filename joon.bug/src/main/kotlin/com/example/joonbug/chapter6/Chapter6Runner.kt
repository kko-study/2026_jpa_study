package com.example.joonbug.chapter6

import com.example.joonbug.chapter6.homework1.Member as Hw1Member
import com.example.joonbug.chapter6.homework1.Team as Hw1Team
import com.example.joonbug.chapter6.homework2.Locker as Hw2Locker
import com.example.joonbug.chapter6.homework2.Member as Hw2Member
import com.example.joonbug.chapter6.homework3.Member as Hw3Member
import com.example.joonbug.chapter6.homework3.Order as Hw3Order
import com.example.joonbug.chapter6.homework3.Product as Hw3Product
import com.example.joonbug.chapter6.bonus2.EventOrder
import jakarta.persistence.EntityManagerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.time.LocalDate

/**
 * 6장. 다양한 연관관계 매핑 테스트
 */
@Component
@Order(7)
class Chapter6Runner(
    private val entityManagerFactory: EntityManagerFactory
) : CommandLineRunner {

    override fun run(vararg args: String) {
        println("\n===== Chapter 6: 다양한 연관관계 매핑 테스트 =====")
        homework1_OneToManyUnidirectional()
        homework2_OneToOneLazyLoading()
        homework3_ManyToManyIntermediate()
        homework2_Bonus1_ReverseLazyLoading()
        bonus2_CompositeKey()
        bonus3_QueryEfficiency()
    }

    /**
     * 숙제 1: @JoinColumn 토글
     * 일대다 단방향에서 @JoinColumn 유무에 따른 동작 차이
     */
    private fun homework1_OneToManyUnidirectional() {
        val em = entityManagerFactory.createEntityManager()
        val tx = em.transaction

        try {
            tx.begin()
            println("\n----- 숙제 1: 일대다 단방향 (@JoinColumn 토글) -----")

            // 1. Team "개발팀" 저장
            val team = Hw1Team(name = "개발팀")
            em.persist(team)
            println("1. Team '개발팀' 저장 (id=${team.id})")

            // 2. Member "홍길동", "김철수" 저장
            val member1 = Hw1Member(name = "홍길동")
            val member2 = Hw1Member(name = "김철수")
            em.persist(member1)
            em.persist(member2)
            println("2. Member '홍길동', '김철수' 저장")

            // 3. team.members에 추가
            team.members.add(member1)
            team.members.add(member2)
            println("3. team.members.add() 호출")

            // 4. flush
            println("4. flush 실행 (SQL 로그 확인)")
            em.flush()

            println()
            println("   [@JoinColumn 없음 - 현재 상태]")
            println("   - 조인 테이블(hw1_team_hw1_member) 생성")
            println("   - INSERT: team 1개, member 2개, 조인테이블 2개")
            println()
            println("   [@JoinColumn 있음 - 주석 해제 시]")
            println("   - Member 테이블에 team_id FK 추가")
            println("   - INSERT: team 1개, member 2개 + UPDATE 2개")

            tx.commit()
        } catch (e: Exception) {
            if (tx.isActive) tx.rollback()
            throw e
        } finally {
            em.close()
        }
    }

    /**
     * 숙제 2: 일대일 지연 로딩
     * @OneToOne에서 EAGER vs LAZY 로딩 타이밍 확인
     */
    private fun homework2_OneToOneLazyLoading() {
        val em = entityManagerFactory.createEntityManager()
        val tx = em.transaction
        var memberId: Long? = null

        // 데이터 준비
        try {
            tx.begin()
            println("\n----- 숙제 2: 일대일 지연 로딩 -----")

            val locker = Hw2Locker(name = "101번")
            em.persist(locker)

            val member = Hw2Member(name = "홍길동", locker = locker)
            em.persist(member)
            memberId = member.id

            em.flush()
            em.clear()
            println("1. 데이터 준비 완료 (Member -> Locker)")

            tx.commit()
        } catch (e: Exception) {
            if (tx.isActive) tx.rollback()
            throw e
        } finally {
            em.close()
        }

        // 조회 테스트
        val em2 = entityManagerFactory.createEntityManager()
        try {
            println("2. Member 조회 시작")
            val foundMember = em2.find(Hw2Member::class.java, memberId)

            println("=== Member 조회 끝 ===")
            println("   (EAGER: Locker SELECT가 위에 나옴)")
            println("   (LAZY: Locker SELECT가 아래에 나옴)")

            println("3. locker.name 접근")
            val lockerName = foundMember.locker?.name
            println("   locker.name = $lockerName")
        } finally {
            em2.close()
        }
    }

    /**
     * 보너스 1: 역방향 일대일 LAZY 로딩
     * FK 없는 쪽에서 LAZY가 동작하지 않는 이유 확인
     */
    private fun homework2_Bonus1_ReverseLazyLoading() {
        val em = entityManagerFactory.createEntityManager()
        val tx = em.transaction
        var lockerId: Long? = null

        // 데이터 준비
        try {
            tx.begin()
            println("\n----- 보너스 1: 역방향 일대일 LAZY 로딩 -----")

            val locker = Hw2Locker(name = "102번")
            em.persist(locker)
            lockerId = locker.id

            val member = Hw2Member(name = "김철수", locker = locker)
            locker.member = member
            em.persist(member)

            em.flush()
            em.clear()
            println("1. 데이터 준비 완료 (Member가 FK 보유)")

            tx.commit()
        } catch (e: Exception) {
            if (tx.isActive) tx.rollback()
            throw e
        } finally {
            em.close()
        }

        // 조회 테스트
        val em2 = entityManagerFactory.createEntityManager()
        try {
            println("2. Locker 조회 시작 (FK 없는 쪽)")
            val foundLocker = em2.find(Hw2Locker::class.java, lockerId)

            println("=== Locker 조회 끝 ===")
            println("   (LAZY인데 Member SELECT가 위에 나왔다면 -> 프록시 생성 불가)")

            println("3. member.name 접근")
            val memberName = foundLocker.member?.name
            println("   member.name = $memberName")

            println()
            println("   [분석] FK 없는 쪽에서는 LAZY가 동작하지 않을 수 있음")
            println("   이유: JPA가 연관 엔티티 존재 여부를 알 수 없어 프록시 생성 불가")
        } finally {
            em2.close()
        }
    }

    /**
     * 숙제 3: 다대다 중간 엔티티
     * 같은 상품을 여러 번 주문 가능 (대리 키 사용)
     */
    private fun homework3_ManyToManyIntermediate() {
        val em = entityManagerFactory.createEntityManager()
        val tx = em.transaction

        try {
            tx.begin()
            println("\n----- 숙제 3: 다대다 중간 엔티티 -----")

            // 1. Member, Product 저장
            val member = Hw3Member(name = "Runner홍길동")
            val product = Hw3Product(name = "Runner운동화")
            em.persist(member)
            em.persist(product)
            println("1. Member 'Runner홍길동', Product 'Runner운동화' 저장")

            // 2. Order 2개 생성 (같은 회원, 같은 상품)
            val order1 = Hw3Order(
                member = member,
                product = product,
                count = 3,
                orderDate = LocalDate.of(2025, 1, 15)
            )
            val order2 = Hw3Order(
                member = member,
                product = product,
                count = 2,
                orderDate = LocalDate.of(2025, 1, 20)
            )
            em.persist(order1)
            em.persist(order2)
            println("2. Order 2개 저장 (같은 상품 재주문)")
            println("   - 주문1: 운동화 3개, 1/15")
            println("   - 주문2: 운동화 2개, 1/20")

            em.flush()
            em.clear()

            // 3. 검증
            val orders = em.createQuery(
                "SELECT o FROM Hw3Order o WHERE o.member.name = :name",
                Hw3Order::class.java
            ).setParameter("name", "Runner홍길동").resultList

            println("3. 검증: Order 테이블에 ${orders.size}개 row 저장됨")
            println()
            println("   [분석] 다대다를 중간 엔티티로 풀면:")
            println("   - 추가 속성(count, orderDate) 관리 가능")
            println("   - 같은 회원이 같은 상품을 여러 번 주문 가능")

            tx.commit()
        } catch (e: Exception) {
            if (tx.isActive) tx.rollback()
            throw e
        } finally {
            em.close()
        }
    }

    /**
     * 보너스 2: 복합 키
     * (회원, 상품)을 복합 키로 하여 1인 1회 구매 제약 구현
     */
    private fun bonus2_CompositeKey() {
        val em = entityManagerFactory.createEntityManager()
        val tx = em.transaction

        try {
            tx.begin()
            println("\n----- 보너스 2: 복합 키 (1인 1회 구매 제약) -----")

            // 데이터 준비
            val member = Hw3Member(name = "홍길동")
            val product = Hw3Product(name = "창립기념 한정판")
            em.persist(member)
            em.persist(product)
            println("1. Member '홍길동', Product '창립기념 한정판' 저장")

            // 첫 번째 구매
            val order1 = EventOrder(
                member = member,
                product = product,
                count = 1,
                orderDate = LocalDate.of(2025, 1, 15)
            )
            em.persist(order1)
            em.flush()
            println("2. 첫 번째 구매 성공!")

            // 두 번째 구매 시도
            println("3. 두 번째 구매 시도 (같은 회원, 같은 상품)...")
            val order2 = EventOrder(
                member = member,
                product = product,
                count = 1,
                orderDate = LocalDate.of(2025, 1, 20)
            )
            em.persist(order2)
            em.flush()

            tx.commit()
        } catch (e: Exception) {
            println("   -> 예외 발생! 복합 키 중복")
            println("   메시지: ${e.cause?.message?.take(80) ?: e.message?.take(80)}")
            println()
            println("   [분석] 복합 키 (member_id, product_id)가 중복되어 INSERT 실패")
            println("   이를 통해 '1인 1회 구매' 비즈니스 제약 구현")
            if (tx.isActive) tx.rollback()
        } finally {
            em.close()
        }
    }

    /**
     * 보너스 3: 일대다 단방향 단점
     * 일대다 단방향 vs 다대일 양방향 쿼리 효율 비교
     */
    private fun bonus3_QueryEfficiency() {
        val em = entityManagerFactory.createEntityManager()
        val tx = em.transaction

        try {
            tx.begin()
            println("\n----- 보너스 3: 일대다 단방향 쿼리 효율 -----")
            println("SQL 로그를 세어보세요:")

            val team = Hw1Team(name = "QA팀")
            em.persist(team)
            println("  1. Team INSERT")

            val member1 = Hw1Member(name = "테스터1")
            val member2 = Hw1Member(name = "테스터2")
            em.persist(member1)
            em.persist(member2)
            println("  2. Member INSERT x 2")

            team.members.add(member1)
            team.members.add(member2)
            println("  3. team.members.add() 호출")

            println("\n  [flush]")
            em.flush()

            println()
            println("   [@JoinColumn 없음 - 조인 테이블 방식]")
            println("   - INSERT hw1_team (1개)")
            println("   - INSERT hw1_member x 2 (2개)")
            println("   - INSERT 조인테이블 x 2 (2개)")
            println("   → 총 5개 쿼리")
            println()
            println("   [@JoinColumn 있음 - FK 방식]")
            println("   - INSERT hw1_team (1개)")
            println("   - INSERT hw1_member x 2 (2개)")
            println("   - UPDATE hw1_member x 2 (2개) ← team_id 설정")
            println("   → 총 5개 쿼리")
            println()
            println("   [다대일 양방향으로 변경 시]")
            println("   - INSERT team (1개)")
            println("   - INSERT member x 2 (2개) ← FK 포함")
            println("   → 총 3개 쿼리 (UPDATE 불필요!)")
            println()
            println("   [결론] 일대다 단방향은 추가 쿼리 발생 → 다대일 양방향 권장")

            tx.commit()
        } catch (e: Exception) {
            if (tx.isActive) tx.rollback()
            throw e
        } finally {
            em.close()
        }
    }
}