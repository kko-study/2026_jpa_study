## 📘 8장 과제 – 프록시와 연관관계

### 과제명  
**프록시 객체와 영속성 컨텍스트 상태 변화 추적**

---

### 목표
- 프록시 객체의 정체와 위험해지는 시점을 명확히 이해한다.
- 영속성 컨텍스트 범위와 지연 로딩의 한계를 설명할 수 있어야 한다.

---
## 0) 과제 규칙
- `@Transactional` 범위를 바꿔가며 실험하는 케이스가 포함되어야 함(아래 3번 참조)

## 1) 엔티티 스켈레톤

### Team
- `id` (PK)
- `name` (String, not null)

### Member
- `id` (PK)
- `username` (String, not null)
- `team` (`@ManyToOne(fetch = LAZY)`)

> 주의: `toString()`, `equals()/hashCode()`에서 연관관계를 접근하지 않도록 주의

---

## 2) 데이터 준비
아래 데이터를 반드시 저장한다.
- Team(id=10, name="team-A")
- Member(id=1, username="member-1", team=team-A)

> ID는 생성전략을 자유로 하되, 제출 시 테스트 코드에서 재현 가능해야 함.

---

## 3) 실험 코드 + 추가 케이스

### 3-1) 기본 실험 코드
아래 코드를 실행하고, 각 단계에서
- `teamProxy`가 프록시인지 여부
- SQL이 실행되는 시점
- 영속성 컨텍스트 관점에서 어떤 상태인지
를 설명하시오.

```java
@Transactional
public void test_case_1() {
    Member m = em.find(Member.class, 1L);

    Team teamProxy = m.getTeam();
    System.out.println("1) class = " + teamProxy.getClass());

    em.flush();
    em.clear();

    System.out.println("2) class = " + teamProxy.getClass());
    System.out.println("3) name = " + teamProxy.getName());
}
```

### 3-2) 추가 케이스 A: clear 이후 "새로" getReference 비교
아래 코드를 추가로 실행해서 getReference()의 의미를 비교하시오.
```java
@Transactional
public void test_case_2() {
    Member m = em.find(Member.class, 1L);
    Long teamId = m.getTeam().getId();

    em.flush();
    em.clear();

    Team ref = em.getReference(Team.class, teamId);
    System.out.println("A) class = " + ref.getClass());
    System.out.println("B) name = " + ref.getName());
}
```

### 3-3) 추가 케이스 B: 트랜잭션 경계 밖에서 접근
아래 두 가지 중 하나로 “트랜잭션 밖 접근”을 재현하시오.
#### 옵션 1) 서비스에서 엔티티 반환 후 외부에서 접근
- spring.jpa.open-in-view=false 옵션으로 설정 후 진행
- 서비스 메서드: @Transactional에서 Member 반환
- 호출자(트랜잭션 밖)에서 member.getTeam().getName() 접근
- 서비스에서는 team을 절대 접근하지 말 것

```java

```java
@Service
@RequiredArgsConstructor
public class MemberService {

  @PersistenceContext
  private final EntityManager em;

  @Transactional
  public Member getMember(Long memberId) {
    return em.find(Member.class, memberId);
  }
}

@SpringBootTest(properties = "spring.jpa.open-in-view=false")
class LazyInitOption1Test {

  @Autowired MemberService memberService;

  @Test
  void lazyInitializationException_occurs_outside_tx() {
    Member member = memberService.getMember(1L); // tx 안에서 조회 후 반환, 여기서 tx 끝
    assertThrows(org.hibernate.LazyInitializationException.class, () -> {
      member.getTeam().getName(); // tx 밖 -> 프록시 초기화 시도 -> 예외
    });
  }
}
```


#### 옵션 2) 직접 clear + close 유사 상황 재현
- 트랜잭션을 종료시키거나 영속성 컨텍스트를 분리시킨 뒤 접근

**목표**: LazyInitializationException이 발생하는 조건을 정확히 적는다.

```java
@SpringBootTest
class LazyInitOption2ClearTest {

  @PersistenceContext EntityManager em;

  @Test
  @Transactional
  void lazyInitializationException_after_clear_on_proxy() {
    Member m = em.find(Member.class, 1L);
    Team teamProxy = m.getTeam(); // LAZY -> 프록시

    em.flush();
    em.clear();                   // 여기서 m, teamProxy 모두 detached 상태가 됨

    assertThrows(org.hibernate.LazyInitializationException.class, () -> {
      teamProxy.getName();        // detached 프록시 초기화 시도 -> 예외
    });
  }
}
```
---

### 3-4) 아래 코드에서 위험한 줄은? (실행해보고 확인해봅시다!)

```java
@Transactional
public void test() {
    Member m = em.find(Member.class, 1L);
    Team t = m.getTeam();
    em.clear();
    t.getName();
}
```

---

## 4) 제출물
- 엔티티 코드(Team/Member)
- 데이터 준비 코드
- 테스트 코드 (test_case_1, test_case_2, 트랜잭션 밖 접근 재현 케이스)
- SQL 로그
    - em.find(Member.class, 1L) 실행 시 SQL
    - m.getTeam() 호출 시 SQL이 나갔는지 여부
    - teamProxy.getName() 호출 시 SQL이 나갔는지 여부
    - em.clear() 이후 동일 프록시 참조를 사용할 때 어떤 SQL/예외가 나오는지


### 과제 하며 생각해볼만한 것들 -> 발표자가 이야기 해보아요!
#### Q1. teamProxy는 언제 프록시이며, 언제 위험한 객체가 되는가? (아래 조건을 포함해서 설명)
    - LAZY 연관관계에서 프록시가 반환되는 조건
    - 프록시 초기화(=실제 DB 조회)가 트리거되는 조건
    - em.clear() 이후 기존 프록시가 왜 “위험”해지는지 (관리/비관리 상태 관점)

#### Q2. em.clear() 이후에도 참조가 유지되는 이유는 무엇인가? (아래 조건을 포함하여 설명)
    - em.clear()가 지우는 대상은 무엇인가? (영속성 컨텍스트의 엔티티/프록시 관리 정보)
    - 자바 객체 참조 자체는 왜 살아있는가?
    - clear 이후 객체(프록시)가 ‘detached’가 되면 어떤 제약이 생기는가?

#### Q3. 위 코드가 운영 코드에 있다면, 가장 안전한 수정 방법은 무엇인가?
