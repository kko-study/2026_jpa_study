# 10장 과제 – 객체지향 쿼리 언어

아래 질문들에 대해 각자의 의견을 정리해 주세요.

---

### Q1. 묵시적 조인의 위험성

아래 JPQL에서 실제로 나가는 SQL은 무엇인가요?

```
select m.team.name from Member m
```

- 왜 이런 묵시적 조인이 위험할까요?
- 본인이라면 실무에서 어떻게 작성하실 건가요?

### A1.
- 실제로 나가는 SQL
  - `select t.name from Member m join Team t on m.team_id = t.id`
  - Member → Team으로 묵시적 조인이 발생
- 위험한 이유
  - 조인 발생을 인지하기 어려움.
  - team 값이 null인 경우 응답 데이터에서 누락됨.
- 실무에서는?
  - 명시적으로 조인 작성하기

### Q2. fetch 조인과 값 타입

- 임베디드 타입(`@Embedded`)으로 설계된 필드는 fetch 조인이 필요한가요?
- 엔티티 연관관계와 비교하여, 조회 시점의 동작 차이를 설명해주세요.

### A2.
- 임베디드 타입은 fetch 조인 필요 없음. (같은 테이블에 저장되기 때문에.
- 엔티티 연관관계는 별도 테이블에 저장됨 => 지연 로딩 가능.

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

### A3.
- 문제점: 벌크 연산은 "영속성 컨텍스트를 무시"하고 DB에 직접 반영하기 때문에, member 객체의 age는 여전히 20으로 남아있음.
- 해결 방법: 벌크 연산 후 영속성 컨텍스트 초기화