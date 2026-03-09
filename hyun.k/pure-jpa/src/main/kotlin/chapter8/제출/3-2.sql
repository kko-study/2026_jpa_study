Hibernate:
select
    m1_0.id,
    m1_0.team_id,
    m1_0.username
from
    ch8_member m1_0
where
    m1_0.id=?

-- A) class = class chapter8.Team$HibernateProxy

Hibernate:
select
    t1_0.id,
    t1_0.name
from
    ch8_team t1_0
where
    t1_0.id=?


-- B) name = team-A