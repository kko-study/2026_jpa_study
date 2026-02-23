# 10장 과제 – 객체지향 쿼리 언어

---

### Q1. 묵시적 조인의 위험성

> 실제로 나가는 SQL은 무엇인가요?

```sql
SELECT t.name
FROM member m
INNER JOIN team t ON m.team_id = t.id
```

`m.team.name`이라는 경로 표현식에 의해 **묵시적 내부 조인(INNER JOIN)** 이 발생한다.

> 왜 이런 묵시적 조인이 위험할까요?

1. **JPQL만 보면 조인이 발생하는지 알 수 없다** — 쿼리가 단순해 보이지만 실제로는 조인이 숨어 있다
2. **경로가 깊어지면 조인이 연쇄적으로 발생한다** — `m.team.organization.name` 같은 경우 2번의 조인이 숨겨진다
3. **INNER JOIN이 강제**되므로 team이 null인 Member는 결과에서 누락된다 (의도치 않은 데이터 손실)
4. **튜닝이 어렵다** — 조인이 명시적이지 않으니 성능 문제 발생 시 원인 파악이 힘들다

> 본인이라면 실무에서 어떻게 작성하실 건가요?

항상 **명시적 조인**을 사용하여 어떤 조인이 발생하는지 JPQL만 보고도 파악할 수 있게 한다.

```sql
select t.name
from Member m
join m.team t
```

---

### Q2. fetch 조인과 값 타입

> 임베디드 타입(`@Embedded`)으로 설계된 필드는 fetch 조인이 필요한가요?

**필요 없다.**

`@Embedded` 필드는 엔티티의 일부로, 같은 테이블에 컬럼으로 저장된다.
엔티티를 조회하면 임베디드 타입의 모든 필드가 **항상 함께 SELECT**에 포함되므로 추가 쿼리나 지연 로딩 자체가 발생하지 않는다.

> 엔티티 연관관계와 비교하여, 조회 시점의 동작 차이를 설명해주세요.

| 구분 | `@Embedded` (값 타입) | `@ManyToOne` / `@OneToMany` (엔티티) |
|------|----------------------|--------------------------------------|
| 저장 위치 | 같은 테이블 | 별도 테이블 |
| 조회 시점 | 항상 즉시 로딩 (별도 쿼리 없음) | 기본 전략에 따라 LAZY/EAGER |
| fetch 조인 필요 여부 | 불필요 | LAZY일 때 N+1 방지를 위해 필요 |
| 추가 SQL | 없음 | 지연 로딩 시 추가 SELECT 발생 |

---

### Q3. 벌크 연산과 영속성 컨텍스트

> 아래 코드의 문제점은 무엇인가요?

```kotlin
val member = em.find(Member::class.java, 1L)  // 영속성 컨텍스트에 age=20으로 캐싱
em.createQuery("update Member m set m.age = m.age + 1")
    .executeUpdate()  // DB에서는 age=21로 변경
println(member.age)  // 여전히 20 출력!
```

벌크 연산(`executeUpdate`)은 **영속성 컨텍스트를 무시하고 DB에 직접** 실행된다.
영속성 컨텍스트의 1차 캐시에는 여전히 변경 전 값(age=20)이 남아 있으므로, DB는 21인데 애플리케이션은 20으로 인식하는 **데이터 정합성 불일치**가 발생한다.

> 어떻게 해결하실 건가요?

벌크 연산 후 `em.clear()`로 영속성 컨텍스트를 초기화한다.

```kotlin
val member = em.find(Member::class.java, 1L)

em.createQuery("update Member m set m.age = m.age + 1")
    .executeUpdate()

em.clear()  // 영속성 컨텍스트 초기화

val refreshedMember = em.find(Member::class.java, 1L)  // DB에서 다시 조회
println(refreshedMember.age)  // 21 출력
```

또는 **벌크 연산을 가장 먼저 실행**하여 영속성 컨텍스트에 아직 아무것도 로드되지 않은 상태에서 처리하는 방법도 있다.
