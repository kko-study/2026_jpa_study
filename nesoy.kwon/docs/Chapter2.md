# Chapter 2
## Config
- persistence.xml
  - persistence-unit : 데이터베이스당 하나의 영속성 유닛을 등록

## JPA Annotation
- @Entity
  - JPA에게 엔티티로 인식하도록 알려주는 어노테이션
- @Table
  - 엔티티 클래스에 매핑할 테이블을 지정하는 어노테이션
- @Id
  - Primary Key를 지정하는 어노테이션
- @Column
  - 컬럼 매핑을 위한 어노테이션
  - 어노테이션이 없는 필드는 필드명을 사용해서 컬럼명으로 매핑한다.

## 구성 요소
- EntityManagerFactory
  - EntityManager를 생성하는 팩토리 객체
  - Persistence.createEntityManagerFactory("persistence-unit-name") 메서드를 사용하여 생성
  - persistence.xml 파일에 정의된 persistence-unit-name을 사용하여 생성
- EntityManager
  - JPA의 핵심 인터페이스로, 데이터베이스와의 상호작용을 담당
  - 트랜잭션 관리, 쿼리 실행, 엔티티 관리 등을 제공
  - 스레드간에 공유하거나 재사용하면 안된다.

## Transaction
- JPA를 사용하면 항상 트랜잭션 안에서 데이터 변경

## CRUD
- Create : persist
- Read : find
- Update : 별도의 메서드가 없음
- Delete : remove
