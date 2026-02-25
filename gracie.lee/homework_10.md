# 10장 과제 – 객체지향 쿼리 언어

---

### Q1. 묵시적 조인의 위험성

아래 JPQL에서 실제로 나가는 SQL은 무엇인가요?

```
select m.team.name from Member m
```

- 왜 이런 묵시적 조인이 위험할까요?
- 본인이라면 실무에서 어떻게 작성하실 건가요?

#### 답변

실제 SQL:

```sql
SELECT t.name
FROM member m
INNER JOIN team t ON m.team_id = t.id
```

`m.team.name` 경로 표현식 때문에 묵시적 INNER JOIN이 발생한다.

JPQL만 보면 조인이 있는지 모르고, 경로가 깊어지면 조인이 여러 개 숨겨진다. 
INNER JOIN이라 team이 null인 Member는 결과에서 빠질 수도 있다.

실무에서는 명시적 조인을 쓴다.

```sql
select t.name from Member m join m.team t
```

---

### Q2. fetch 조인과 값 타입

- 임베디드 타입(`@Embedded`)으로 설계된 필드는 fetch 조인이 필요한가요?
- 엔티티 연관관계와 비교하여, 조회 시점의 동작 차이를 설명해주세요.

#### 답변

`@Embedded` 필드는 같은 테이블 컬럼이라 엔티티 조회 시 항상 함께 가져온다. 별도 쿼리가 나가지 않으므로 fetch 조인이 필요 없다.

반면 `@ManyToOne`, `@OneToMany` 같은 엔티티 연관관계는 별도 테이블이고 LAZY 로딩 시 추가 SELECT가 발생하므로, N+1 방지를 위해 fetch 조인이 필요하다.

---

### Q3. 벌크 연산과 영속성 컨텍스트

- 아래 코드의 문제점은 무엇인가요?
- 어떻게 해결하실 건가요?

```kotlin
val member = em.find(Member::class.java, 1L)
// member.age == 20

em.createQuery("update Member m set m.age = m.age + 1")
    .executeUpdate()

println(member.age)
```

#### 답변

벌크 연산은 영속성 컨텍스트를 거치지 않고 DB에 직접 실행된다. 그래서 DB에선 age가 21이 되었지만 영속성 컨텍스트의 1차 캐시에는 여전히 20이 남아있어 `member.age`가 20으로 출력된다.

벌크 연산 후 `em.clear()`로 영속성 컨텍스트를 초기화하고 다시 조회한다.

```kotlin
em.createQuery("update Member m set m.age = m.age + 1")
    .executeUpdate()
em.clear()
val member = em.find(Member::class.java, 1L) // DB에서 다시 조회
```
