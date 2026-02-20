# 객체지향 쿼리 언어
- JPQL, Criteria, QueryDSL

## 객체지향 쿼리
- 단순한 객체 조회
  - 식별자로 조회 EntityManager.find
  - 객체 그래프 탐색(예: a.getB0())
- 하지만 조건이 추가된 조회는 위 방법으로 충분하지 않다.
  - 그렇다고 모든 데이터를 다 조회해서 메모리에서 필터링하는 방법도 좋지 않다.
- JPQL은 객체지향 SQL이다.
  - Java Persistent Query Language
- 조금 더 쉽게 사용하기 위해 도와주는 도구
  - Criteria, QueryDSL
  - 하지만 JPQL을 이해해야 잘 사용할 수 있다.

## JPQL
- SQL을 추상화해서 특정 데이터베이스를 의존하지 않는다.

```
String jpql = "select m from Member as m where m. username = "kim'";
List<Member> resultlist = em.createQuery (jpql, Member.class).getResultlist();
```

## Criteria
- Criteria가 가진 장점이 많지만 모든 장점을 상쇄할 정도로 복잡하고 장황
- 찾아봤는데 딱히 표준 소스코드가 있어 보이지 않음.. 🫠

## QueryDSL
- https://github.com/querydsl/querydsl
- https://github.com/OpenFeign/querydsl

## JPQL JOIN
- fetch join
  - JPQL에서 성능 최적화를 위해 제공하는 기능
  - fetch join과 일반 조인의 차이
    - JPQL은 결과를 반환할때 연관관계까지 고려하지 않는다.
    - 단지 Select 절에 지정한 엔티티만 조회할 수 있을 뿐이다.
  - 글로벌 패치 전략보다 패치 조인이 더 우선순위가 높다.
  - SQL 한번으로 여러 엔티티를 조회할 수 있어서 성능 최적화 할때 좋다.

## 경로 표현식(Path Expression)
- .을 찍어 객체 그래프를 탐색하는 것이다.
- 구성 요소
  - 상태 필드
  - 연관 필드 (단일, 컬렉션)
- 여기서 연관 필드를 사용할때 묵시적 조인이 발생한다.
  - 그냥 사용하면 내부조인이 발생한다.
- 우리가 의도하지 않았지만 묵시적으로 내부 조인이 발생하고, 이는 곧 성능 저하로 이어지게 된다.
  - 차라리 그럴바에 명시적으로 조인을 해서 알 수 있도록 한다.

## JPQL 동적, 정적 쿼리
- 동적 쿼리 : 런타임에 특정 조건에 따라 JPQL을 동적으로 구성
- 정적 쿼리 : 미리 정의한 쿼리에 이름을 부여해서 필요할 때 사용할 수 있다.
  - 재사용하므로 성능상 이점도 있다.