# GitHub Proxy API

A RESTful application that acts as a proxy for the GitHub API. It retrieves a user's repositories (filtering out forks) and lists their branches. 

## Technologies

* **Java:** 25
* **Spring Boot:** 4.0.1 (Snapshot/Milestone)
* **Build Tool:** Gradle (Kotlin DSL)
* **Dependencies:** `spring-boot-starter-webmvc`, `spring-boot-starter-restclient`
* **Dependencies:** `spring-boot-starter-webmvc`, `spring-boot-starter-restclient`
* **Tests:** Testing a Spring Boot 4.0.1 Application Using WireMock

## Requirements

* JDK 25 installed
* Internet connection (to fetch data from the public GitHub API)

## How to Run

### Build and Run Application
To start the application on port `8080`:

```bash
./gradlew build 
./gradlew test
./gradlew bootRun
```

### To check for which user the repositories should be displayed, replace "username" in the command with the GitHub username.
```bash
curl http://localhost:8080/users/username/repositories
```


