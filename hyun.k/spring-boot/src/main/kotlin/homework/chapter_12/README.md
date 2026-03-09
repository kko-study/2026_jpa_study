## 공부한 내용

### 스프링 데이터 JPA
- interface 만으로 CRUD 공통 메서드 제공
    - 구현체는 스프링 데이터 jpa가 어플리케이션 실행 시점에 주입
    - extends JpaRepository
- save() = upsert
    - 엔티티의 식별자로 구분. 있으면 update 없으면 insert
- 쿼리 메서드 기능
    - 메서드 이름으로 쿼리 생성
        - 규칙 존재
    - 메서드 이름으로 JPA NamedQuery 호출
        - 미리 xml or 어노테이션으로 정의 ⇒ 정의된 함수 사용
    - @Query 어노테이션을 사용해서 레포지토리 인터페이스에 쿼리 직접 정의
- 기타 쿼리들
    - 벌크성 수정 쿼리 ⇒ @Modifying
        - // @Modifying 없으면 → getResultList() 호출 → 에러
          // @Modifying 있으면 → executeUpdate() 호출 → 정상 동작
        - 영속성 컨텍스트를 무시하고 DB에 직접 쿼리 ⇒ clearAutomatically 옵션으로 영속성 컨텍스트 초기화 필요한 이유
    - 페이징과 정렬
        - 페이징 : 반환 타입을 Page로 지정하면 됨. 단 이 경우 페이징 기능을 제공하기 위해 전체 데이터 건수 조회하는 count 쿼리 호출함. ⇒ 매 번 카운트 쿼리 날린다고 함 (Page 객체가 getTotalElements(), getTotalPages() 등의 정보를 제공해야 하기 때문에) ⇒ 대안으로 Slice (limit + 1) 존재.
        - 정렬 : 파라미터 Pageable 안에 Sort 존재.
- 명세
    - jpa Criteria
    - .where, .and (술어) 등으로 표현가능
- 기타
    - 웹 리졸버로도 사용 가능
    - crieteria vs QueryDsl
        - Criteria API: JPA 표준이라는 장점이 있지만, 가독성이 너무 떨어져 실무에서
          거의 사용하지 않음
        - QueryDSL: 설정이 필요하지만, 가독성·생산성·유지보수 면에서 실무 표준에
          가까움


## 5) 과제 하며 생각해볼 것들 -> 발표자가 이야기 해보아요!

#### Q1. 쿼리 메서드 vs @Query, 실무에서 어떤 기준으로 선택할까?
- 쿼리 메서드 이름이 길어지면 가독성이 어떻게 되는가?
- 예: `findByNameAndAgeGreaterThanAndTeamNameOrderByAgeDesc` — 이게 괜찮은가?

- 답변 : 개인적으론 이런 가독성이 좋지 않은 쿼리들을 좋아하지 않습니다.. (가독성 뿐만 아니라 오타 위험, 조건 변경시 메서드 명 변경 등)
- ㄴ 실무 기준은 팀 내에서 합의된 컨벤션이 중요하다고 생각. 조건 2개 이상은 @Query로 하는게 좋지 않을까라는 생각이긴 합니다..

#### Q2. Spring Data JPA가 인터페이스만으로 동작하는 원리는?
- 구현 클래스를 안 만들었는데 어떻게 실행되는가?
- 힌트: 프록시, `SimpleJpaRepository`

- 답변 : Spring Data JPA가 런타임에 인터페이스의 구현체를 동적으로 생성하는 프록시 패턴을 사용합니다. 
- `SimpleJpaRepository`는 Spring Data JPA에서 제공하는 기본 구현체로, 인터페이스를 구현하여 CRUD 메서드를 제공함.
- 개발자가 인터페이스만 정의하면, Spring Data JPA가 런타임에 해당 인터페이스의 구현체를 생성하여 주입해줍니다. (위에 정리된 내용)