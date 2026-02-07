package homework.chapter_4;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 4장 숙제 2번: UNIQUE 제약조건 테스트 (JUnit)
 *
 * ========================================
 * 🟡 핵심 학습 포인트: persist() vs flush()
 * ========================================
 *
 * 1. persist()란?
 *    - 엔티티를 영속성 컨텍스트에 저장 (메모리)
 *    - 아직 DB에 INSERT 하지 않음 (일반적인 경우)
 *    - "쓰기 지연 SQL 저장소"에 INSERT 쿼리를 모아둠
 *
 * 2. flush()란?
 *    - 쓰기 지연 SQL 저장소의 쿼리들을 DB에 실행
 *    - 영속성 컨텍스트의 변경 내용을 DB에 동기화
 *    - 트랜잭션 커밋 직전에 자동 호출됨
 *
 * 3. 왜 persist()에서는 에러가 안 나고 flush()에서 나는가?
 *    - persist() 시점: 아직 DB에 안 갔으므로 UNIQUE 체크 불가
 *    - flush() 시점: 실제 INSERT SQL 실행 → DB에서 UNIQUE 체크 → 예외 발생
 *
 * 4. 예외: IDENTITY 전략
 *    - IDENTITY 전략은 DB가 기본키를 생성 (AUTO_INCREMENT)
 *    - 기본키를 알아야 영속성 컨텍스트에서 관리 가능
 *    - 따라서 persist() 호출 시 즉시 INSERT 실행! (예외적 상황)
 *    - 이 경우 persist() 시점에도 UNIQUE 위반 예외 발생 가능
 *
 * ========================================
 */
class UniqueConstraintTest {

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

    private String uniqueId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    @Test
    @DisplayName("같은 loginId로 두 번 저장 시 PersistenceException 발생")
    void testDuplicateLoginIdThrowsException() {
        tx.begin();

        String duplicateLoginId = "dup_login_" + uniqueId();

        // ========================================
        // 📚 첫 번째 User 저장
        // ========================================
        User user1 = new User(
                duplicateLoginId,
                "test1_" + uniqueId() + "@example.com",
                "홍길동" + uniqueId(),
                25
        );

        // persist(): 영속성 컨텍스트에 저장
        // IDENTITY 전략이므로 이 시점에 INSERT 실행됨!
        em.persist(user1);

        // flush(): 명시적으로 DB 동기화 (이미 INSERT 됐지만 확실히 하기 위해)
        em.flush();

        // ========================================
        // 📚 두 번째 User 저장 시도 (중복 loginId)
        // ========================================
        User user2 = new User(
                duplicateLoginId,  // 💥 중복된 loginId!
                "test2_" + uniqueId() + "@example.com",
                "김철수" + uniqueId(),
                30
        );

        // ========================================
        // 📚 예외 발생 시점 설명
        // ========================================
        // assertThrows 안에서 persist() + flush() 실행
        //
        // [IDENTITY 전략인 경우] ← 현재 User 엔티티가 사용 중
        // - persist() 시점에 INSERT 즉시 실행 → 💥 여기서 예외 발생!
        //
        // [SEQUENCE/TABLE 전략인 경우]
        // - persist() 시점: 시퀀스로 ID만 조회, INSERT는 안 함
        // - flush() 시점: INSERT 실행 → 여기서 예외 발생!
        //
        // 🔴 Q: IDENTITY에서는 persist()만으로 예외 발생하는데, flush()는 왜 있는가?
        // 🔴 A: IDENTITY에서는 persist()에서 이미 예외 터져서 flush()는 실행 안 됨!
        // 🔴    flush()는 SEQUENCE/TABLE 전략을 위한 것.
        // 🔴    두 전략 모두 커버하는 범용 테스트 코드를 위해 함께 작성함.
        // ========================================
        PersistenceException exception = assertThrows(PersistenceException.class, () -> {
            em.persist(user2);  // ⚡ IDENTITY: 여기서 INSERT 즉시 실행 → 예외!
            em.flush();         // 🔴 IDENTITY에서는 도달 안 함 (SEQUENCE/TABLE용)
        }, "같은 loginId로 저장 시 PersistenceException이 발생해야 함");

        // 예외의 원인(Cause)이 있는지 확인
        // ConstraintViolationException이 원인으로 들어있음
        assertNotNull(exception.getCause(), "원인 예외가 있어야 함");
    }

    @Test
    @DisplayName("같은 email로 두 번 저장 시 PersistenceException 발생")
    void testDuplicateEmailThrowsException() {
        tx.begin();

        String duplicateEmail = "same_" + uniqueId() + "@example.com";

        User user1 = new User(
                "user1_" + uniqueId(),
                duplicateEmail,
                "테스트1_" + uniqueId(),
                20
        );
        em.persist(user1);
        em.flush();

        // email도 UNIQUE 제약조건이 걸려있으므로 중복 시 예외 발생
        User user2 = new User(
                "user2_" + uniqueId(),
                duplicateEmail,  // 💥 중복된 email!
                "테스트2_" + uniqueId(),
                21
        );

        // 🔴 IDENTITY: persist()에서 예외 발생, flush()는 도달 안 함
        PersistenceException exception = assertThrows(PersistenceException.class, () -> {
            em.persist(user2);
            em.flush();
        }, "같은 email로 저장 시 PersistenceException이 발생해야 함");

        assertNotNull(exception.getCause(), "원인 예외가 있어야 함");
    }

    @Test
    @DisplayName("같은 name+age 복합키로 두 번 저장 시 PersistenceException 발생")
    void testDuplicateNameAgeCompositeThrowsException() {
        tx.begin();

        String duplicateName = "복합테스트_" + uniqueId();
        int duplicateAge = 99;

        User user1 = new User(
                "composite1_" + uniqueId(),
                "comp1_" + uniqueId() + "@example.com",
                duplicateName,
                duplicateAge
        );
        em.persist(user1);
        em.flush();

        // ========================================
        // 📚 복합 UNIQUE 제약조건 테스트
        // ========================================
        // @Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "age"}))
        // name과 age의 "조합"이 유일해야 함
        // - ("홍길동", 25) + ("홍길동", 26) → OK (age가 다름)
        // - ("홍길동", 25) + ("김철수", 25) → OK (name이 다름)
        // - ("홍길동", 25) + ("홍길동", 25) → 💥 UNIQUE 위반!
        // ========================================
        User user2 = new User(
                "composite2_" + uniqueId(),
                "comp2_" + uniqueId() + "@example.com",
                duplicateName,   // 같은 name
                duplicateAge     // 같은 age → 복합 UNIQUE 위반!
        );

        // 🔴 IDENTITY: persist()에서 예외 발생, flush()는 도달 안 함
        PersistenceException exception = assertThrows(PersistenceException.class, () -> {
            em.persist(user2);
            em.flush();
        }, "같은 name+age 복합키로 저장 시 PersistenceException이 발생해야 함");

        assertNotNull(exception.getCause(), "원인 예외가 있어야 함");
    }

    @Test
    @DisplayName("다른 loginId로 저장 시 성공")
    void testDifferentLoginIdSuccess() {
        tx.begin();

        User user1 = new User(
                "success1_" + uniqueId(),
                "success1_" + uniqueId() + "@example.com",
                "성공1_" + uniqueId(),
                10
        );
        em.persist(user1);

        User user2 = new User(
                "success2_" + uniqueId(),  // 다른 loginId → OK!
                "success2_" + uniqueId() + "@example.com",
                "성공2_" + uniqueId(),
                11
        );
        em.persist(user2);

        // 예외 없이 flush 성공해야 함
        assertDoesNotThrow(() -> em.flush());

        tx.commit();
    }
}
