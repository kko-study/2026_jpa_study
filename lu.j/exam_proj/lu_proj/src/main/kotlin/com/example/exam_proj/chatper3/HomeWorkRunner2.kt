package com.example.exam_proj.chatper3

import com.example.exam_proj.CommandRunner
import jakarta.persistence.EntityManagerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class HomeWorkRunner2(
    private val entityManagerFactory: EntityManagerFactory,
) : CommandLineRunner {
//    - 새로운 회원을 생성하여 영속 상태로 만들기
//    - 같은 id로 두 번 조회하여 동일성(==) 비교
//    - 영속성 컨텍스트를 초기화(clear) 후 다시 조회
//    - 초기화 전후의 엔티티가 같은 객체인지 비교하고 결과 출력
//    - 각 단계마다 주석으로 예상되는 SQL 쿼리 여부 표시
    override fun run(vararg args: String) {
        val em = entityManagerFactory.createEntityManager()
        val tx = em.transaction

        try {
            tx.begin()

            val member = HWMember()
            member.id = 1L
            member.name = "홍길동"
            // SQL 없음
            em.persist(member)
            // 1차 캐시에서 반환
            val searchMember = em.find(HWMember::class.java, 1L)
            // 1차 캐시에서 반환
            val searchMember2 = em.find(HWMember::class.java, 1L)
            println("동일성 체크 : ${searchMember == searchMember2}")

            em.clear()

            // 영속성 캐시가 없어서 null로 리턴되어 둘 다 select 를 하게 될 것이다.
            // select m from member where id = 1
            val searchMember3 = em.find(HWMember::class.java, 1L)
            // select m from member where id = 1
            val searchMember4 = em.find(HWMember::class.java, 1L)
            // 둘 다 null 일 것이다.
            println("동일성 체크 : ${searchMember3 == searchMember4}")

            tx.commit()
        } catch (ex: Exception) {
            if (tx.isActive) {
                tx.rollback()
            }
            throw ex
        } finally {
            em.close()
        }
    }
}