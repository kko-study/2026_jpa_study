# 10장 과제 – 객체지향 쿼리 언어

아래 질문들에 대해 각자의 의견을 정리해 주세요.

---

### Q1. 묵시적 조인의 위험성

아래 JPQL에서 실제로 나가는 SQL은 무엇인가요?

```
select m.team.name from Member m
```

```
SELECT t.name                                                                                                                                                                                                                         
FROM Member m                                                                                                                                                                                                                         
INNER JOIN Team t ON m.team_id = t.id   
```

- 왜 이런 묵시적 조인이 위험할까요?
  - 항상 inner join이 되고 조인 쿼리가 나감
- 본인이라면 실무에서 어떻게 작성하실 건가요?
  -
```
SELECT t.name                                                                                                                                                                                                                         
FROM Member m                                                                                                                                                                                                                         
JOIN m.team t
```

---

### Q2. fetch 조인과 값 타입

- 임베디드 타입(`@Embedded`)으로 설계된 필드는 fetch 조인이 필요한가요?
  - 필요없다.
- 엔티티 연관관계와 비교하여, 조회 시점의 동작 차이를 설명해주세요.
  - 임베디드 타입
    - 같은 테이블에 저장되고 즉시로딩되고 프록시 사용 안한다
  - 엔티티 연관관계
    - 별도 테이블에 저장되고 지연로딩을 선택했다면 프록시 사용한다

---

### Q3. 벌크 연산과 영속성 컨텍스트

- 아래 코드의 문제점은 무엇인가요?
  - 벌크 연산은 영속성 컨텍스트를 건너 뛰고 db에 직접실행해서 정합성이 깨진다
- 어떻게 해결하실 건가요?
  - 벌크 연산 후 영속성 컨텍스트 초기화
  - 벌크 연산 먼저 실행 이후 조회

```kotlin
val member = em.find(Member::class.java, 1L)
// member.age == 20

em.createQuery("update Member m set m.age = m.age + 1")
    .executeUpdate()

println(member.age)
```