package homework.chapter_3;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 3장 숙제 2번: 1차 캐시와 동일성 보장 테스트 (JUnit)
 *
 * ========================================
 * 🟡 핵심 학습 포인트: 영속성 컨텍스트와 1차 캐시
 * ========================================
 *
 * 1. 영속성 컨텍스트란?
 *    - 엔티티를 영구 저장하는 환경
 *    - EntityManager를 통해 엔티티를 저장/조회하면 영속성 컨텍스트에 보관
 *    - 트랜잭션 범위 안에서 동작
 *
 * 2. 1차 캐시란?
 *    - 영속성 컨텍스트 내부의 캐시
 *    - Map<@Id, Entity> 형태로 엔티티 저장
 *    - em.find() 호출 시 1차 캐시를 먼저 확인 → 없으면 DB 조회
 *
 * 3. 동일성(Identity) 보장
 *    - 같은 트랜잭션 내에서 같은 엔티티를 조회하면 항상 같은 객체 반환
 *    - findMember1 == findMember2 가 true
 *    - Java 컬렉션에서 같은 객체를 꺼내는 것과 동일
 *
 * 4. clear()의 의미
 *    - 영속성 컨텍스트 초기화 (1차 캐시 비움)
 *    - 모든 엔티티가 준영속(detached) 상태로 전환
 *    - 이후 조회 시 DB에서 새로 가져옴 → 새로운 객체 생성
 *
 * 🔴 준영속 엔티티 위험성 → DetachedEntityTest.java 참고
 *
 * ========================================
 */
class CacheTest {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;

    @BeforeAll
    static void setUpFactory() {
        emf = Persistence.createEntityManagerFactory("jpabook");
    }

    @AfterAll
    static void closeFactory() {
        if (emf != null) {
            emf.close();
        }
    }

    @BeforeEach
    void setUp() {
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();
    }

    @AfterEach
    void tearDown() {
        if (tx.isActive()) {
            tx.rollback();
        }
        if (em != null) {
            em.close();
        }
    }

    @Test
    @DisplayName("같은 영속성 컨텍스트 내에서 같은 id로 조회하면 동일한 객체 반환 (동일성 보장)")
    void testIdentityInSamePersistenceContext() {
        // ========================================
        // 📚 엔티티를 영속 상태로 만들기
        // ========================================
        // persist() 호출 → 1차 캐시에 저장
        // 1차 캐시: { 1L: Member(1L, "홍길동") }
        Member member = new Member(1L, "홍길동");
        em.persist(member);

        // ========================================
        // 📚 1차 캐시에서 조회 (DB 접근 X)
        // ========================================
        // em.find() 호출 → 1차 캐시 확인 → 있음! → 바로 반환
        // ⚡ SELECT 쿼리 실행 안 됨! (성능 이점)
        Member findMember1 = em.find(Member.class, 1L);
        Member findMember2 = em.find(Member.class, 1L);

        // ========================================
        // ✅ 동일성 보장: 같은 객체임을 검증
        // ========================================
        // 1차 캐시에서 같은 엔티티를 반환하므로 == 비교 시 true
        assertSame(findMember1, findMember2, "같은 영속성 컨텍스트 내에서는 동일한 객체여야 함");
        assertEquals("홍길동", findMember1.getName());
    }

    @Test
    @DisplayName("clear() 후 조회하면 새로운 객체 반환")
    void testNewObjectAfterClear() {
        // 회원을 영속 상태로 만들고 DB에 반영
        Member member = new Member(2L, "김철수");
        em.persist(member);

        // ========================================
        // 📚 flush(): 쓰기 지연 SQL 저장소 → DB 동기화
        // ========================================
        // 이 시점에 INSERT SQL 실행
        // 하지만 1차 캐시는 그대로 유지됨
        em.flush();

        Member findMemberBeforeClear = em.find(Member.class, 2L);

        // ========================================
        // 📚 clear(): 영속성 컨텍스트 초기화
        // ========================================
        // 1차 캐시 비움! 모든 엔티티가 준영속 상태로 전환
        // 기존 엔티티(findMemberBeforeClear)는 더 이상 관리되지 않음
        em.clear();

        // ========================================
        // 📚 clear 후 조회 → DB에서 새로 가져옴
        // ========================================
        // 1차 캐시가 비었으므로 SELECT 쿼리 실행!
        // 새로운 객체가 생성됨
        Member findMemberAfterClear = em.find(Member.class, 2L);

        // ========================================
        // ❌ 다른 객체임을 검증
        // ========================================
        // clear() 전후의 객체는 다른 인스턴스
        assertNotSame(findMemberBeforeClear, findMemberAfterClear,
                "clear() 후에는 새로운 객체가 생성되어야 함");

        // 하지만 데이터(값)는 같아야 함
        assertEquals(findMemberBeforeClear.getId(), findMemberAfterClear.getId());
        assertEquals(findMemberBeforeClear.getName(), findMemberAfterClear.getName());
    }

    @Test
    @DisplayName("1차 캐시에서 조회 시 SELECT 쿼리 없음")
    void testNoDatabaseQueryFromFirstLevelCache() {
        // ========================================
        // 📚 persist 후 find → 1차 캐시 히트
        // ========================================
        // persist() → 1차 캐시에 저장
        // find() → 1차 캐시에서 바로 반환 (DB 안 감)
        Member member = new Member(3L, "테스트");
        em.persist(member);

        // persist한 객체와 find한 객체는 완전히 같은 객체!
        // (단순히 값이 같은 게 아니라 메모리 주소가 같음)
        Member cached = em.find(Member.class, 3L);
        assertSame(member, cached, "persist한 객체와 find한 객체는 같아야 함");
    }
}
