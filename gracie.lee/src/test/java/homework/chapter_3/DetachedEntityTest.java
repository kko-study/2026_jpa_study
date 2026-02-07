package homework.chapter_3;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 🔴 추가 학습: 준영속(Detached) 엔티티의 위험성
 *
 * ========================================
 * 🔴 핵심 학습 포인트: 준영속 엔티티 주의사항
 * ========================================
 *
 * Q: clear() 후 before와 after가 다른 객체일 때 주의할 점은?
 *
 * 1. ⚠️ 변경 감지 안 됨
 *    - 준영속 엔티티를 수정해도 DB에 반영되지 않음
 *    - before.setName("새이름") 해도 DB는 그대로!
 *
 * 2. ⚠️ LazyInitializationException
 *    - 지연 로딩 연관관계 접근 시 예외 발생
 *    - 영속성 컨텍스트가 없어서 프록시 초기화 불가
 *
 * 3. ⚠️ merge() 사용 시 주의
 *    - 준영속 → 영속 전환하려면 merge() 필요
 *    - merge()는 새로운 객체를 반환! (원본 객체 아님)
 *    - Member merged = em.merge(detached); // merged != detached
 *
 * 4. ⚠️ 컬렉션 동일성 문제
 *    - Set/Map에 넣어두면 같은 데이터인데 다른 객체로 인식
 *    - equals/hashCode를 ID 기반으로 구현해야 함
 *
 * ========================================
 */
class DetachedEntityTest {

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
    @DisplayName("준영속 엔티티 수정은 DB에 반영되지 않음")
    void testDetachedEntityChangeNotPersisted() {
        // 1. 회원 저장 및 DB 반영
        Member member = new Member(4L, "원본이름");
        em.persist(member);
        em.flush();

        // 2. clear 전에 조회 → 영속 상태
        Member beforeClear = em.find(Member.class, 4L);

        // 3. clear() → beforeClear는 준영속 상태로 전환
        em.clear();

        // ========================================
        // 🔴 핵심: 준영속 엔티티 수정
        // ========================================
        // beforeClear는 더 이상 영속성 컨텍스트가 관리하지 않음!
        // 변경 감지(Dirty Checking)가 작동하지 않음!
        beforeClear.setName("변경된이름");

        // 4. clear 후 다시 조회 → DB에서 새로 가져옴
        Member afterClear = em.find(Member.class, 4L);

        // ========================================
        // 🔴 검증: 준영속 엔티티 수정은 DB에 반영 안 됨
        // ========================================
        // beforeClear의 이름은 "변경된이름"으로 바뀌었지만
        // afterClear(DB에서 조회)는 여전히 "원본이름"!
        assertEquals("변경된이름", beforeClear.getName(),
                "준영속 엔티티의 메모리 값은 변경됨");
        assertEquals("원본이름", afterClear.getName(),
                "🔴 하지만 DB에는 반영되지 않음! (변경 감지 X)");

        // 다른 객체임을 확인
        assertNotSame(beforeClear, afterClear,
                "준영속 엔티티와 새로 조회한 엔티티는 다른 객체");

        // ========================================
        // ⚠️ 실무에서 이런 버그가 자주 발생!
        // ========================================
        // - 개발자가 beforeClear를 수정하면 DB에 반영될 것으로 기대
        // - 하지만 clear() 후라 변경 감지가 안 됨
        // - 해결책: merge()를 사용하거나, 다시 find()로 영속 상태 엔티티 사용
    }

    @Test
    @DisplayName("영속 상태 엔티티 수정은 DB에 자동 반영됨 (비교용)")
    void testManagedEntityChangeIsPersisted() {
        // 1. 회원 저장 및 DB 반영
        Member member = new Member(5L, "원본이름");
        em.persist(member);
        em.flush();

        // 2. 조회 → 영속 상태 (clear 안 함!)
        Member managed = em.find(Member.class, 5L);

        // ========================================
        // 📚 영속 상태에서 수정 → 변경 감지 작동!
        // ========================================
        managed.setName("변경된이름");

        // flush로 변경 내용 DB 반영
        em.flush();

        // clear 후 다시 조회하여 DB 값 확인
        em.clear();
        Member reloaded = em.find(Member.class, 5L);

        // ========================================
        // ✅ 영속 상태 수정은 DB에 반영됨
        // ========================================
        assertEquals("변경된이름", reloaded.getName(),
                "✅ 영속 상태 엔티티 수정은 DB에 반영됨 (변경 감지 O)");
    }
}
