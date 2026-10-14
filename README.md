# 🧩 RKD Domain Core

<p align="center">
  <img src="https://img.shields.io/badge/language-Java%2025-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 25">
  <img src="https://img.shields.io/badge/platform-Quarkus%203.39.2-4695EB?style=for-the-badge&logo=quarkus&logoColor=white" alt="Quarkus 3.39.2">
  <img src="https://img.shields.io/badge/library-Hibernate%20ORM%20Panache-59666C?style=for-the-badge&logo=hibernate&logoColor=white" alt="Hibernate ORM with Panache">
  <img src="https://img.shields.io/badge/library-Jackson-555555?style=for-the-badge" alt="Jackson">
  <img src="https://img.shields.io/badge/library-MapStruct%201.6.3-555555?style=for-the-badge" alt="MapStruct 1.6.3">
  <img src="https://img.shields.io/badge/database-PostgreSQL%2016-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL 16">
  <img src="https://img.shields.io/badge/tool-Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven">
  <img src="https://img.shields.io/badge/tool-Docker%20Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker Compose">
  <img src="https://img.shields.io/badge/test-JUnit%205-25A162?style=for-the-badge&logo=junit5&logoColor=white" alt="JUnit 5">
  <img src="https://img.shields.io/badge/test-REST%20Assured-555555?style=for-the-badge" alt="REST Assured">
  <img src="https://img.shields.io/badge/test-Mockito-78A641?style=for-the-badge" alt="Mockito">
</p>

## 📖 About the project

RKD Domain Core is a Quarkus REST API for managing **projects**, business **domains** (which can reference related domains and belong to a project), configurable **attributes** of a domain (typed as `NUMBER`, `DECIMAL`, `TEXT`, `DATE`, `DATE_TIME`, `MONEY`, `OPTION` or `JSON`) and reusable **option** lists stored in PostgreSQL as `jsonb`.

The code follows a layered structure: REST resources, services, Panache repositories, JPA entities, request/response DTOs and MapStruct mappers. The `load` folder contains helper scripts to generate and load seed data.

## 🛠️ Technologies used

| Technology | What it is used for |
| --- | --- |
| Java 25 | Language and runtime of the application (`maven.compiler.release` 25). |
| Quarkus 3.39.2 (`quarkus-rest`, `quarkus-arc`) | Application framework: REST endpoints (`/project`, `/domain`, `/attribute`, `/option`), CDI and CORS configuration. |
| Jackson (`quarkus-rest-jackson`) | JSON serialization of requests and responses, and of the `jsonb` option values. |
| Hibernate ORM with Panache | Persistence of the entities and repositories; the schema is created/updated automatically (`schema-management.strategy=update`). |
| PostgreSQL 16 (`quarkus-jdbc-postgresql`) | Database of the application, run through `docker-compose.yml`. |
| MapStruct 1.6.3 | Compile-time mapping between requests/responses and entities. |
| Maven (Maven Wrapper `mvnw`) | Build, dependency management and test execution. |
| Docker Compose | Starts the local PostgreSQL container and is used by `load/run-load.sh`. |
| JUnit 5, REST Assured, Mockito | Unit tests of the services and `@QuarkusTest` integration tests of the resources against PostgreSQL. |
| Node.js | Runs `load/generate-load.mjs`, which generates `load/domain_load.sql` from migrations in `../sinan-core`. |

## 🗺️ Mapped features

Status based on a real run of `./mvnw test` (31 tests: 30 passed, 1 failed).

| Feature | What it does | Status |
| --- | --- | --- |
| Project management (`/project`) | List (optionally filtered by `status`), find by id, create, update and delete projects. | PARTIAL |
| Domain management (`/domain`) | List, find by id, create, update and delete domains, linking related domains and an optional project. | PARTIAL |
| Attribute management (`/attribute`) | List, find by id, create, update and delete typed attributes of a domain. | COMPLETE |
| Option management (`/option`) | List, find by id, create, update and delete reusable option lists stored as `jsonb`. | COMPLETE |
| Error responses | Maps `DomainNotFoundException`, `InvalidDataException` and `InvalidActionException` to JSON error responses. | PARTIAL |
| Seed data load (`load/`) | `run-load.sh` recreates the database and loads `domain_load.sql`; `generate-load.mjs` regenerates that SQL. | PARTIAL |

## 🚀 How to run the project

### Requirements

- JDK 25 (`JAVA_HOME` pointing to it). Maven itself is not required: the project ships the Maven Wrapper (`mvnw`).
- Docker with Docker Compose, to run PostgreSQL 16 (or any reachable PostgreSQL instance).
- Node.js only if you want to regenerate `load/domain_load.sql`.

### 1. Start the database

```bash
docker compose up -d
```

This starts `postgres:16-alpine` on `localhost:5432` with database `domain_core`, user `postgres` and password `postgres`.

### 2. Configure the connection (optional)

The defaults in `src/main/resources/application.properties` match the container above. To use another database, set these environment variables instead of editing the file:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/domain_core
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
```

The schema is created/updated by Hibernate on startup. CORS only allows `http://localhost:4200`; edit `quarkus.http.cors.origins` if your frontend runs on another origin.

### 3. Run the application

```bash
./mvnw quarkus:dev
```

The API is available at `http://localhost:8080`.

### 4. Run the tests

The `@QuarkusTest` tests need the PostgreSQL database configured above to be running.

```bash
./mvnw test
```

### 5. Load seed data (optional, destructive)

> ⚠️ `run-load.sh` runs `docker compose down --volumes`, permanently deleting all data in the project database, then loads `load/domain_load.sql`.

```bash
bash load/run-load.sh
```

To regenerate the SQL (requires the sibling repository `../sinan-core`):

```bash
node load/generate-load.mjs [migrationsDir] [outputFile]
```

### Build

```bash
./mvnw package
```
