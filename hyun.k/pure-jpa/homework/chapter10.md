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

---

### Q2. fetch 조인과 값 타입

- 임베디드 타입(`@Embedded`)으로 설계된 필드는 fetch 조인이 필요한가요?
- 엔티티 연관관계와 비교하여, 조회 시점의 동작 차이를 설명해주세요.

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

---

## (Optional) 자유 토론 주제
9장을 읽으며 추가로 고민한 점이나 토론하고 싶은 주제가 있다면 자유롭게 작성해주세요.
31 changes: 31 additions & 0 deletions31  
homework/9.md
Original file line number	Diff line number	Diff line change
@@ -0,0 +1,31 @@
# 9장 과제 – 값 타입

아래 질문들에 대해 각자의 의견을 정리해 주세요.

---

### Q1. 값 타입 컬렉션 vs 일대다 엔티티

주소 이력(`AddressHistory`)을 관리해야 하는 상황입니다.

A안: `@ElementCollection`으로 값 타입 컬렉션 사용<br>
B안: AddressHistory를 별도 엔티티로 만들고 `@OneToMany` 사용

- 각 방식을 선택했을 때, **수정/삭제 시 발생하는 SQL**은 어떻게 달라지나요?
- 실무에서 어떤 기준으로 선택하실 건가요?

---

### Q2. 임베디드 타입과 불변 객체

임베디드 타입(`@Embeddable`)은 여러 엔티티에서 공유하면 사이드 이펙트가 발생할 수 있다고 합니다.
이를 방지하기 위해 "불변 객체로 만들어야 한다"고 하는데,

- 불변 객체로 만들면 값을 변경하고 싶을 때는 어떻게 해야 할까요?
- 본인이 실무에서 Address 같은 임베디드 타입을 설계한다면 어떻게 하실건가요?

---


## (Optional) 자유 토론 주제
9장을 읽으며 추가로 고민한 점이나 토론하고 싶은 주제가 있다면 자유롭게 작성해주세요.