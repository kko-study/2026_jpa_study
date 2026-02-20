# JPA의 데이터 타입
- 엔티티 타입과 값 타입으로 나눌 수 있다.
- 엔티티 타입은 추적할 수 있지만, 값 타입은 추적할 수 없다.

## 임베디드 타입(복합 값 타입)
- 엔티티의 응집력을 높히기 위해서 별도의 값들을 분리한다.
- @Embeddable: 값 타입을 정의하는 곳에 표시
- @Embedded: 값 타입을 사용하는 곳에 표시

### 임베디드 타입과 맵핑
- 값이 속한 엔티티의 테이블에 매핑된다.

### @AttributeOverride
- 하나의 엔티티에서 여러 개의 임베디드 타입을 사용할 때, 각각의 필드명을 재정의할 수 있다.
- 너무 많이 사용하면 지저분해지니, 적당히 사용할것
```
@Embedded Address homeAddress;
@Embedded Address companyAddress;

@Embedded
@AttributeOverrides ({
    @AttributeOverride (name="city", column=@Column (name = "COMPANY_CITY")),
    @AttributeOverride (name="street", column=@Column (name = "COMPANY_STREET")),
    @AttributeOverride (name="zipcode", column=@Column (name = "COMPANY_ZIPCODE")),
})
Address companyAddress;
```


## 값 타입과 불변 객체
- 값 타입을 공유하면, side-effect가 발생하기 때문에 깊은 복사를 진행해야 한다.
- 왠만하면 복사해서 객체를 생성하는게, 편하다

## 값 타입과 비교
- hash - equals를 재정의

## 값 타입과 컬렉션
- @ElementCollection
- @CollectionTable

## 값 타입의 제약 사항
- 엔티티는 식별자가 있었기에, 쉽게 찾고 수정할 수 있었다.
- 값 타입은 식별자라는 개념이 없고, 단순한 값들의 모음이므로, 값을 변경해버리면 데이터베이스에 저장된 원본 데이터를 찾기 어렵다.
- 