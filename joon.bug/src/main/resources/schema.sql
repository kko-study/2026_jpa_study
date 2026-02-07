CREATE TABLE MEMBER (
    ID VARCHAR(255) NOT NULL,
    NAME VARCHAR(255),
    AGE INTEGER,
    PRIMARY KEY (ID)
);

-- Chapter 4: Member4 엔티티
CREATE TABLE members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL,
    age INTEGER,
    email VARCHAR(100) UNIQUE,
    role VARCHAR(50),
    created_at DATE,
    description CLOB
);

-- Chapter 4: User 엔티티 (DDL 제약조건 테스트)
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    login_id VARCHAR(255) UNIQUE,
    email VARCHAR(255) UNIQUE,
    name VARCHAR(255),
    age INTEGER,
    CONSTRAINT uk_name_age UNIQUE (name, age),
    CONSTRAINT chk_age CHECK (age >= 0 AND age <= 150)
);