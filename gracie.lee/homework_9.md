# 9장 과제 – 값 타입

---

### Q1. 값 타입 컬렉션 vs 일대다 엔티티

> 각 방식을 선택했을 때, 수정/삭제 시 발생하는 SQL은 어떻게 달라지나요?

**A안: `@ElementCollection` (값 타입 컬렉션)**

값 타입은 식별자(ID)가 없어서 개별 행을 특정할 수 없다.
컬렉션에서 하나만 수정/삭제해도, 해당 엔티티에 소속된 **모든 값 타입 데이터를 DELETE한 후 남은 데이터를 전부 INSERT**한다.

```sql
-- 주소 하나 삭제 시
DELETE FROM address_history WHERE member_id = ?
INSERT INTO address_history (member_id, city, street, zipcode) VALUES (?, ?, ?, ?)
INSERT INTO address_history (member_id, city, street, zipcode) VALUES (?, ?, ?, ?)
-- ... 남은 것들 전부 재삽입
```

**B안: `@OneToMany` (별도 엔티티)**

엔티티는 고유 ID가 있으므로 개별 행 단위로 정확하게 수정/삭제가 가능하다.

```sql
-- 주소 하나 삭제 시
DELETE FROM address_history WHERE id = ?다시 롤
```

> 실무에서 어떤 기준으로 선택하실 건가요?

- 값 타입 컬렉션은 정말 단순하고 변경이 거의 없는 경우에만 사용한다 (예: enum 목록, 태그 등)
- 이력 관리처럼 **데이터가 누적되고 수정/삭제가 발생하는 경우**에는 별도 엔티티 + `@OneToMany`를 사용한다
- 책에서도 "값 타입 컬렉션이 매핑된 테이블에 데이터가 많다면 일대다 관계를 고려하라"고 권장하고 있다

---

### Q2. 임베디드 타입과 불변 객체

> 불변 객체로 만들면 값을 변경하고 싶을 때는 어떻게 해야 할까요?

기존 객체를 수정하는 것이 아니라, **새로운 객체를 생성하여 통째로 교체**한다.

```java
// setter 없이, 새 객체로 교체
Address newAddress = new Address("새도시", "새거리", "12345");
member.setAddress(newAddress);
```

> 본인이 실무에서 Address 같은 임베디드 타입을 설계한다면 어떻게 하실건가요?

- `@Embeddable` 클래스는 **생성자로만 값을 설정하고 setter를 제공하지 않는다**
- 값 변경이 필요하면 `withCity("새도시")` 같은 **복사 팩토리 메서드**를 제공한다

```java
@Embeddable
public class Address {
    private final String city;
    private final String street;
    private final String zipcode;

    // 생성자만 제공, setter 없음

    public Address withCity(String city) {
        return new Address(city, this.street, this.zipcode);
    }
}
```

이렇게 하면 여러 엔티티가 같은 Address 인스턴스를 참조하더라도, 한쪽에서 변경해도 다른 쪽에 사이드 이펙트가 전파되지 않는다.
