# Chapter 3
## EntityManager, EntityManagerFactory
- EntityManagerFactory
  - EntityManager를 생성하는 팩토리 객체
  - Persistence.createEntityManagerFactory("persistence-unit-name") 메서드를 사용하여 생성
  - persistence.xml 파일에 정의된 persistence-unit-name을 사용하여 생성
- EntityManager
  - JPA의 핵심 인터페이스로, 데이터베이스와의 상호작용을 담당
  - 트랜잭션 관리, 쿼리 실행, 엔티티 관리 등을 제공
  - 스레드간에 공유하거나 재사용하면 안된다.

## Entity Lifecycle
- new/transient
  - Persistence context에 추가되지 않은 상태
- managed/persisted
  - Persistence context에 관리되는 상태
- detached
  - Persistence context에서 저장되었다가 분리된 상태
- removed
  - Persistence context에서 제거된 상태

## Persistence Context 
- 준영속 상태
  - 객체가 변경된 이후에 merge되면 반영되는건가?