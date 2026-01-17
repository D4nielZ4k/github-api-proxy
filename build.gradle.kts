plugins {
	java
	id("org.springframework.boot") version "4.0.1"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"
description = "github-proxy"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-restclient")



    testImplementation ("org.springframework.boot:spring-boot-starter-test")



    // Source: https://mvnrepository.com/artifact/org.wiremock.integrations/wiremock-spring-boot
    testImplementation("org.wiremock.integrations:wiremock-spring-boot:4.0.8")

    // Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-webmvc-test
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test:4.0.1")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
