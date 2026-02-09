package com.example.joonbug.chpater8

import jakarta.persistence.EntityManagerFactory
import org.hibernate.LazyInitializationException
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(9)
class Chapter8Runner(
    private val entityManagerFactory: EntityManagerFactory
) : CommandLineRunner {

    override fun run(vararg args: String) {
        println("===== Chapter 8: 프록시와 연관관계 테스트 =====")

        prepareData()
        verify1_checkIsTeamProxy()
        verify2_meaningOfReference()
        verify3_lazyInit()
        verify4_dangerousPoint()

        /*
         * Q1. teamProxy는 언제 프록시이며, 언제 위험한 객체가 되는가?
         * - LAZY 연관관계에서 프록시가 반환되는 조건: 연관관계 객체 호출 시
         * - 프록시 초기화(=실제 DB 조회)가 트리거되는 조건: 실제 데이터 접근 id 제외
         * - em.clear() 이후 기존 프록시가 왜 "위험"해지는지: detached 상태로 영속성 컨텍스트에서 관리 안하는데 자바 참조로 접근하면 위험
         *
         * Q2. em.clear() 이후에도 참조가 유지되는 이유는 무엇인가?
         * - em.clear()가 지우는 대상은 무엇인가?: 영속성 컨텍스트 내부의 1차 캐시 삭제
         * - 자바 객체 참조 자체는 왜 살아있는가?: gc가 안돌았으니까
         * - clear 이후 객체(프록시)가 'detached'가 되면 어떤 제약이 생기는가?: 영속성 컨텍스트가 제공하는 기능을 제공받지 못함
         *
         * Q3. 위 코드가 운영 코드에 있다면, 가장 안전한 수정 방법은 무엇인가?
         * - clear 전에 초기화 시키기
         * - 일단 eager로 바꾸기
         */

        /*
         * 툼스톤 패턴에서 cascade.remove를 사용하는게 올바른 방식인가 + 다른 cascade type은 안전한건 어떤건가
         * all : X
         * persist : 가능
         * merge : 가능
         * remove : 안됨
         * refresh : 가능은 함. 필요시만 -> 불필요 쿼리 발생(N+1 쿼리 발생 가능), 변경사항 손실 가능
         * detach : 가능은 함. 필요시만 -> lazy 프록시 초기화 불가, 변경 감지 불가
         *
         * orphan removal 도 위 개념에서 어떻게 해야하지
         * 툼스톤 패턴에서는 사용 안해야 함
         *
         * 지연로딩과 캐싱전략
         * 프록시 객체 직렬화 불가
         * 1. dto 생성 후 캐싱
         * 2. 연관 데이터 분리 캐싱
         * 이렇게 하면 지연로딩의 장점을 잃는거 같다
         * 클로드 추천
         * 읽기 빈도 높음 -> 레디스 캐싱
         * 읽기 빈도 낮음 -> LAZY 유지
         * 애매함 -> hibernate 2차 캐시 or 분리 캐싱
         *
         * cascade, orphan removal 시 캐싱 전략
         * cascade.persist -> 자식 자동 저장 시 캐시 누락
         * cascade.remove -> 자식 자동 삭제 시 캐시 삭제 누락
         * orphanRemoval -> 컬렉션에서 제거 시 캐시 삭제 누락
         * 전략 1. 이벤트 리스터로 캐시 무효화 -> 이건 너무 오버헤드가 크다
         * 전략 2. 서비스 레이어에서 캐시 관리
         * 전략 3. Spring Cache + SpEL로 연관 캐시 무효화
         * 전략들이 있지만 잘 설계 해야할듯
         */
    }

    private fun prepareData() {
        val em = entityManagerFactory.createEntityManager()
        val tx = em.transaction

        try {
            tx.begin()

            val teamA = Ch8Team().apply {
                name = "team-A"
            }
            em.persist(teamA)

            val member1 = Ch8Member().apply {
                username = "member-1"
                team = teamA
            }
            em.persist(member1)

            em.flush()
            tx.commit()
        } catch (e: Exception) {
            if (tx.isActive) tx.rollback()
            throw e
        } finally {
            em.close()
        }
    }

    private fun verify1_checkIsTeamProxy() {
        println("----- 3-1. verify1_checkIsTeamProxy -----")
        val em = entityManagerFactory.createEntityManager()
        val tx = em.transaction

        try {
            tx.begin()

            val m = em.find(Ch8Member::class.java, 1L)

            val teamProxy = m.team

            println("1) class = ${teamProxy?.javaClass}")
            println("프록시 상태, 초기화 X, managed")

            em.flush()
            em.clear()
            println("프록시 상태, 초기화 X, detached")

            println("2) class = ${teamProxy?.javaClass}")
            println("프록시 상태, 초기화 X, detached")

            try {
                println("3) name = ${teamProxy?.name}")
            } catch (e: LazyInitializationException) {
                println(e)
                println("프록시 상태, 초기화 X, detached 상태에서 초기화 시도로 인한 예외 발생")
            }

            tx.commit()
        } catch (e: Exception) {
            if (tx.isActive) tx.rollback()
            throw e
        } finally {
            em.close()
        }
        println()
    }

    private fun verify2_meaningOfReference() {
        println("----- 3-2. verify2_meaningOfReference -----")
        val em = entityManagerFactory.createEntityManager()
        val tx = em.transaction

        try {
            tx.begin()

            val m = em.find(Ch8Member::class.java, 1L)
            val teamProxy = m.team

            val teamId = teamProxy?.id
            println("1) teamProxy class = ${teamProxy?.javaClass}")
            println("프록시 상태, 초기화 X, managed")

            em.flush()
            em.clear()
            println("프록시 상태, 초기화 X, detached")

            val ref = em.getReference(Ch8Team::class.java, teamId)

            println("A) ref class = ${ref.javaClass}")
            println("새로운 프록시 객체, 초기화 X, managed")

            println("B) ref.name = ${ref.name}")
            println("프록시 초기화 O, managed, db 질의")

            tx.commit()
        } catch (e: Exception) {
            if (tx.isActive) tx.rollback()
            throw e
        } finally {
            em.close()
        }
        println()
    }

    private fun getTeamProxyFromMember(): Ch8Team? {
        val em = entityManagerFactory.createEntityManager()
        val tx = em.transaction

        return try {
            tx.begin()

            val m = em.find(Ch8Member::class.java, 1L)
            val teamProxy = m.team
            println("프록시 상태, 초기화 X, managed")

            tx.commit()
            teamProxy
        } catch (e: Exception) {
            if (tx.isActive) tx.rollback()
            throw e
        } finally {
            em.close()
            println("detached 상태 전환")
        }
    }

    private fun verify3_lazyInit() {
        println("----- 3-3. verify3_lazyInit -----")

        val teamProxy = getTeamProxyFromMember()
        println("프록시 상태, 초기화 X, detached (트랜잭션 밖)")

        try {
            println("name = ${teamProxy?.name}")
        } catch (e: LazyInitializationException) {
            println(e)
            println("트랜잭션 종료 이후 반환된 detached 프록시에 초기화 시도하지만 영속성 컨텍스트가 없어서 예외 발생")
        }
    }

    /**
     * 3-4) 위험한 줄 찾기
     */
    private fun verify4_dangerousPoint() {
        println("----- 3-4. verify4_dangerousPoint -----")
        val em = entityManagerFactory.createEntityManager()
        val tx = em.transaction

        try {
            tx.begin()

            val m = em.find(Ch8Member::class.java, 1L)
            val t = m.team
            em.clear()
            println("clear 이후 t?.name 호출 부분이 위험")
            t?.name

            tx.commit()
        } catch (e: LazyInitializationException) {
            println(e)
            println("detached 프록시에 초기화 시도로 예외 발생")
            if (tx.isActive) tx.rollback()
        } finally {
            em.close()
        }
    }
}