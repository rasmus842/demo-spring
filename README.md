# Demo Spring project

---

# 1. Project structure

* spring-boot gradle project
  * java 21, gradle 8, spring-boot 3
* MySQL as default database
  * liquibase gradle plugin to manage migrations
* openapi specs for services documentation
  * openapi_spec.yaml
  * used to generate java model classes and interfaces for controllers

### Account CRUD app:
* package `com.example.demo.account`
* `Account` Entity and `AccountService` interface
* repo, impl, and controller subpackages should not be touched by other parts of the application, if those exist
  * If other app features exist, they should us AccountService to access Account entities
  * (an idea- maybe do not expose Account Entity at all? Another immutable analogue of it using record or @Value class)


---

# 2. Local Development Environment:

## 2.1 Prerequisites:

### 2.1.1. MySQL:

a) Install, configure, and run MySQL locally
  * https://dev.mysql.com/doc/

b) Run MySQL in a local dev container:
  1) install docker https://docs.docker.com/engine/install/
  2) Run mysql container locally: `./dev/start-mysql.sh`
  3) To stop the container: `./dev/stop-mysql.sh`

###  2.1.2. Install java 21 (Eclipse Temurin)

* via sdkman: https://sdkman.io/jdks#tem

### 2.1.3. Download dependencies and build app

* Run `./gradlew build`
- generates classes from openapi_spec.yaml (src/generated folder)
- compiles and runs tests against h2 in-memory database

### 2.1.4. Update MySQL

* Run `./gradlew update` to update MySQL with liquibase

## 2.2. Run `./gradlew bootRun` and start developing

* Automatically sets `spring.active.profile=dev` and uses local mysql as database

---

# 2. Tests

## 2.1. Running tests

* Run unit-tests and int-tests against h2 in-memory database: `./gradlew test`
* Run int-tests against MySql test containter: `./gradlew mySqlTest`
  * Test container setup is time consuming

## 2.2. Writing Tests

* See abstract class BaseTest and TestAppConfig
  * Loads AppConfig and overrides some beans for testing purposes
  * if tests configured to use MySQL then starts test-container and configures it as the datasource
  * Strongly recommended to extend this class when writing new tests
    * There should not arise a need to create a separate custom application context
    * In that case should rather create simple unit tests without spring

### 2.2.1 Unit tests

* Choose between:
  1) extending BaseTest class and using whole AppConfig
  2) Using custom mocks

### 2.2.2 Integration tests

* Extend abstract class BaseTest 
* Annotate with @SpringBootTest
  * choose whether or not to make real http requests or MockMvc

---

# 3. Building For Production

1. Build jar: `./gradlew bootJar`
2. Deploy jar: `java -jar demo-<version>.jar --spring.profiles.active=prod -DMYSQL_URL=<url> -DMYSQL_USER=<user> -DMYSQL_PASSWORD=<pw>`
