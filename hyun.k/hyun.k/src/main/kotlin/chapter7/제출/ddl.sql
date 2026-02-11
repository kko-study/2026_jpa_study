-- 테이블 생성

create table Item
(
    basePrice bigint      not null,
    createdAt timestamp(6),
    id        bigint      not null,
    updatedAt timestamp(6),
    DTYPE     varchar(31) not null check ((DTYPE in ('normal', 'subscription'))),
    name      varchar(255),
    primary key (id)
)

create table NormalItem
(
    stockQuantity integer not null,
    id            bigint  not null,
    sku           varchar(255),
    primary key (id)
)

create table SubscriptionItem
(
    billingCycleDays integer not null,
    trialDays        integer not null,
    id               bigint  not null,
    primary key (id)
)

create table orders
(
    createdAt timestamp(6),
    id        bigint       not null,
    updatedAt timestamp(6),
    orderNo   varchar(255) not null unique,
    primary key (id)
)

create table OrderItem
(
    quantity          integer not null,
    createdAt         timestamp(6),
    item_id           bigint,
    order_id          bigint,
    order_item_id     bigint  not null,
    unitPriceSnapshot bigint  not null,
    updatedAt         timestamp(6),
    itemNameSnapshot  varchar(255),
    primary key (order_item_id)
)

create table OrderItemOption
(
    createdAt     timestamp(6),
    id            bigint not null,
    order_item_id bigint,
    updatedAt     timestamp(6),
    option_key    varchar(255),
    option_value  varchar(255),
    primary key (id)
)

-- FK 제약조건

-- JOINED 상속: 자식 → 부모 FK
alter table NormalItem add constraint ... foreign key (id) references Item
alter table SubscriptionItem add constraint ... foreign key (id) references Item

-- OrderItem → Item, Order FK
alter table OrderItem add constraint ... foreign key (item_id) references Item
alter table OrderItem add constraint ... foreign key (order_id) references orders

-- OrderItemOption → OrderItem FK
alter table OrderItemOption add constraint ... foreign key (order_item_id) references OrderItem

-- 3-2 검증 포인트

--   - createdAt, updatedAt이 모든 테이블에 존재 → @MappedSuperclass 정상 동작
--   - BaseEntity 테이블은 생성되지 않음 → 올바르게 상속만 됨
--   - JOINED 전략이라 Item, NormalItem, SubscriptionItem이 각각 별도 테이블로 생성됨