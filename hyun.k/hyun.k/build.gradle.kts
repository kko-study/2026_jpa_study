plugins {
    kotlin("jvm") version "2.2.20"
}

group = "study"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.hibernate:hibernate-core:7.2.1.Final")
    implementation("org.hibernate:hibernate-entitymanager:5.6.15.Final")
    implementation("org.hibernate.javax.persistence:hibernate-jpa-2.1-api:1.0.2.Final")
    implementation("com.h2database:h2:2.4.240")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}