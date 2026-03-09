-- 결과

Hibernate:
    /* select
        i
    from
        Item i
    order by
        i.id */ select
                             i1_0.id,
                             i1_0.DTYPE,
                             i1_0.basePrice,
                             i1_0.createdAt,
                             i1_0.name,
                             i1_0.updatedAt,
                             i1_1.sku,
                             i1_1.stockQuantity,
                             i1_2.billingCycleDays,
                             i1_2.trialDays
                from
                             Item i1_0
                                 left join
                             NormalItem i1_1
                             on i1_0.id=i1_1.id
                                 left join
                             SubscriptionItem i1_2
                             on i1_0.id=i1_2.id
                order by
                             i1_0.id

-- - 선택한 상속 전략에서 왜 이런 SQL이 나오는지 설명
-- ㄴ select i from Item i는 모든 Item을 다형성으로 조회하는 JPQL (다형성으로 조회한다 = 자식 클래스도 함께 조회한다)
-- ㄴ 따라서, 부모 테이블인 Item 테이블과 자식 테이블인 NormalItem, SubscriptionItem 테이블을 모두 조인하여 조회
-- ㄴ left join을 사용하는 이유는, 어떤 Item이 NormalItem일 수도 있고, SubscriptionItem일 수도 있기 때문
-- ㄴ 즉, 모든 Item을 빠짐없이 가져오려면 LEFT JOIN이어야 함.
-- ㄴ 전략별로 다름 :
--     - SINGLE_TABLE: JOIN 없이 select * from Item 한 방. 모든 컬럼이 한 테이블에 있으니까.
--     - TABLE_PER_CLASS: UNION ALL로 각 테이블을 합쳐서 조회.