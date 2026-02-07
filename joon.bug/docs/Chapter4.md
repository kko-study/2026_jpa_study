# Chapter 1

## 매핑
- 객체, 테이블
  - @entity, @table
  - 기본 생성자 필수
  - final, enum, interface, inner 클래스 불가
  - final 사용불가
- 기본 키
  - @id
  - 직접할당
    - 어플리케이션에서 직접 할당
  - 자동 생성 @generatedvalue
    - @identity : db에 위임
    - @sequence : db sequence
    - @table : 키 생성 테이블
- 필드, 컬럼
  - @column
  - enum type mapping : @enumarated
  - date, time, timestamp : @temporal
  - @transient : 매핑하지 않음
- 연관관계
  - @manytoone, @joincolumn