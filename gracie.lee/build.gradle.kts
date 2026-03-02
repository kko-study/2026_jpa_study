plugins {
    java
    application
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.jpa") version "1.9.22"
    kotlin("plugin.allopen") version "1.9.22"
    id("io.spring.dependency-management") version "1.1.4"
}

application {
    mainClass.set("homework.chapter_3.CacheTest")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.2.3")
    }
}

dependencies {
    // 기존 순수 JPA (ch3-8)
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    runtimeOnly("com.h2database:h2:2.2.224")

    // Spring Boot (ch12-13)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

// Kotlin Entity 클래스를 open으로 만들어 Hibernate 프록시 생성 가능하게 함
allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
    }
}
