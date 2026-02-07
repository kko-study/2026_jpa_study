# persist vs merge (JPA 스펙 기준 정리)

JPA에서 `persist`와 `merge`는 **엔티티를 영속성 컨텍스트(managed state)에 편입시키는 방법**이지만,  
**대상 객체의 상태 전이 방식과 동작 모델이 근본적으로 다릅니다.**

핵심 차이는 다음 한 줄로 요약할 수 있습니다.

- `persist` → **전달한 객체 자체를 관리**
- `merge` → **전달한 객체의 상태를 복사하고, 다른 관리 객체를 반환**

---

# persist

```java
em.persist(entity);
````

## 동작

* **새 엔티티(new / transient)** 를 영속성 컨텍스트에 등록
* **전달한 객체 인스턴스 자체가 영속 상태(managed)** 가 됨
* 트랜잭션 flush/commit 시점에 INSERT SQL 실행

## 특징

* 이미 **영속 상태**이면 일반적으로 영향 없음(no-op에 가까움)
* **준영속(detached)** 인 경우 예외 발생 가능 (`EntityExistsException` 등)
* 식별자(ID)는 **“없어야 한다”가 아니라 “아직 DB에 존재하지 않는 새 엔티티”여야 함**

    * assigned 전략이라면 ID가 있어도 `persist` 가능

## 예시

```java
Member member = new Member("kim");
em.persist(member);   // member 자체가 managed

member.setName("lee"); // 변경 감지 → UPDATE
```

---

# merge

```java
Member managed = em.merge(entity);
```

## 동작

* **준영속(detached) 또는 비영속(transient)** 엔티티의 상태를
* 영속성 컨텍스트 내부의 **관리 객체에 복사**
* **관리되는 새 인스턴스를 반환**

즉:

```
전달 객체 ≠ 반환 객체
```

## 특징

* 원본 객체는 계속 **detached 상태 유지**
* 반드시 **반환값을 사용해야 함**
* 식별자 기준으로 기존 엔티티 존재 여부를 판단

    * 필요 시 SELECT 발생 가능
    * 존재 → UPDATE
    * 미존재 → INSERT
* 필드 전체가 복사되므로 **null 값도 그대로 반영**

## 예시

```java
Member detached = new Member(1L, "kim");

Member managed = em.merge(detached);

detached.setName("lee"); // 반영 안 됨 (detached)
managed.setName("park"); // 반영 됨 (managed)
```

---

# 핵심 차이 비교

| 구분     | persist           | merge        |
| ------ | ----------------- | ------------ |
| 주 대상   | 새 엔티티 (transient) | 준영속/비영속      |
| 반환값    | void              | 영속 엔티티 반환    |
| 영속화 대상 | 전달 객체 자체          | 별도의 관리 객체    |
| 원본 객체  | managed로 전환       | 여전히 detached |
| 내부 동작  | 등록                | 상태 복사(copy)  |
| 주 용도   | 신규 INSERT         | 재부착/상태 동기화   |

---

# merge 사용 시 주의점 (실무 이슈)

## 1. 반환값을 사용하지 않으면 버그 발생

```java
em.merge(detached);
detached.setName("lee"); // 반영 안 됨
```

## 2. null 덮어쓰기 위험

`merge`는 **필드 전체 복사** 방식이므로 값이 없는 필드도 그대로 반영됩니다.

```java
Member detached = new Member();
detached.setId(1L);
detached.setName("kim");
// age 설정 안 함 → null

em.merge(detached); // age = null 로 UPDATE
```

의도치 않은 데이터 손실 가능성이 있습니다.

---

# 권장 패턴: 변경 감지(Dirty Checking)

대부분의 CRUD는 `merge` 없이 처리 가능합니다.

## 권장 방식

```java
@Transactional
public void updateMember(Long id, String newName) {
    Member member = em.find(Member.class, id); // managed
    member.setName(newName);                   // 변경 감지
}
```

### 장점

* 불필요한 SELECT/복사 없음
* null 덮어쓰기 위험 없음
* 코드 의도 명확
* 성능 및 안정성 우수

---

# merge가 필요한 경우

다음과 같이 **영속성 컨텍스트가 끊긴 객체를 재부착해야 하는 상황**에서 사용합니다.

* 계층 간 DTO 대신 엔티티 직접 전달
* 배치 처리
* 세션 분리 아키텍처
* 레거시 구조

```java
// 트랜잭션 A
Member member = em.find(Member.class, 1L);

// 컨텍스트 종료 → detached

// 트랜잭션 B
Member merged = em.merge(member);
```

가능하면 구조적으로 **detached 엔티티 자체를 전달하지 않는 설계**가 더 바람직합니다.

---

# Spring Data JPA save()

참고 사항:

* 새 엔티티 → `persist`
* 기존 엔티티 → `merge`

즉, 항상 `merge`가 호출되는 것은 아닙니다.

---

# 영속성 컨텍스트와 준영속 상태

## 상태 전이 규칙

```
영속성 컨텍스트 존재 → managed
영속성 컨텍스트 종료 → detached
```

핵심은 **트랜잭션이 아니라 영속성 컨텍스트 생명주기**입니다.

---

# Spring + OSIV(Open Session In View)

## OSIV = true (기본 설정인 경우가 많음)

```
HTTP 요청 시작 → 영속성 컨텍스트 생성
Controller/Service 전체에서 managed 유지
HTTP 응답 종료 → 컨텍스트 종료 → detached
```

* Controller에서도 Lazy Loading 가능

## OSIV = false

```
@Transactional 시작 → 컨텍스트 생성
@Transactional 종료 → 컨텍스트 종료
```

* 트랜잭션 밖 Lazy Loading 시 `LazyInitializationException`

---

# 실무 권장 가이드

| 상황               | 권장 방법                          |
| ---------------- | ------------------------------ |
| 신규 저장            | persist                        |
| 단순 수정            | find → setter (Dirty Checking) |
| 대량 수정            | JPQL/Query                     |
| detached 재부착 불가피 | merge (주의해서 사용)                |

---

# 결론

* `persist`는 **신규 등록 전용**
* `merge`는 **상태 복사 기반 재부착**
* 일반적인 서비스 로직에서는 **변경 감지 패턴이 기본 선택**
* `merge`는 예외적 상황에서만 제한적으로 사용하는 것이 안전
