# Chapter 5
## 객체 연관관계 vs 테이블 연관관계
- 객체는 참조로 연관관계를 맺는다.
- 테이블은 외래 키로 연관관계를 맺는다.
- 객체 연관관계와 테이블 연관관계를 이어주는 연관관계 맵핑이 필요하다.

## 연관관계 맵핑 
```
@ManyToOne
@JoinColumn(name = "TEAM_ID")
```

## 양방향 맵핑
### 연관관계의 주인이 필요한 배경
- 객체는 양쪽에서 각각 참조하지만, 테이블은 FK 하나로 양방향 조인이 가능하다.
- 그래서 JPA는 "둘 중 누가 이 FK를 관리할 것인가?"를 정해야 합니다. 이게 연관관계의 주인입니다.

### 더 자세한 이해
- "외래키를 관리한다" = DB에 실제로 INSERT/UPDATE 쿼리를 날린다
- 주인과 FK 때문에 헷갈림
  - 그냥 관계 정보를 들고 있는 테이블이 주인이 된다고 이해하면 편함.
  - Team 테이블에는 회원 정보가 없기 때문에 관계 정보가 없다.
  - 하지만 회원 테이블은 팀 관계 정보가 있기 때문에 연관관계의 주인으로 이해하는게 편하다.

```
# 주인인 경우
member.setTeam(teamA);  // 주인이 관계를 설정
em.persist(member);

INSERT INTO member (id, name, team_id) VALUES (1, '홍길동', 100) // FK Updated

# 주인이 아닌 경우 
teamA.getMembers().add(member);  // 주인이 아닌 쪽에서만 설정
em.persist(member);

INSERT INTO member (id, name, team_id) VALUES (1, '홍길동', NULL) // team_id null
```

### 그럼 mappedBy 사용안하면?
```
@Entity
public class Member {
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
}

@Entity
public class Team {
    @OneToMany  // mappedBy 없음!
    private List<Member> members;
}
```

- JPA는 이걸 "서로 모르는 두 개의 단방향 관계"로 보고, 테이블을 이렇게 만듭니다:
```
member 테이블           team 테이블        team_members 테이블 (자동 생성!)
+---------+-------+  +----+------+     +---------+-----------+
| id      |team_id|  | id | name |     | team_id | member_id |
+---------+-------+  +----+------+     +---------+-----------+
```


```
[객체의 세계]                    [테이블의 세계]
Member → Team (team 필드)        MEMBER 테이블
Team → Member (members 필드)         ↓
                                 team_id (FK) ← 이 하나로 양쪽 조인 가능
참조가 2개!                       외래 키는 1개!
```

## 양방향 주의 사항
- 주인이 아닌 객체에 대해서 변경을 가하면 DB에 반영이 되지 않는다.

