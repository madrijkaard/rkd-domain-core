# RKD Domain Core

<p align="center">
  A REST API for managing business domains, hierarchical subdomains, configurable attributes, and reusable option lists.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 25">
  <img src="https://img.shields.io/badge/Quarkus-3.39.2-4695EB?style=for-the-badge&logo=quarkus&logoColor=white" alt="Quarkus 3.39.2">
  <img src="https://img.shields.io/badge/PostgreSQL-16-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL 16">
  <img src="https://img.shields.io/badge/Maven-3.9+-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/tests-27%20passing-2EA44F?style=for-the-badge" alt="27 tests passing">
  <img src="https://img.shields.io/badge/API-REST-6E56CF?style=for-the-badge" alt="REST API">
  <img src="https://img.shields.io/badge/database-JSONB-336791?style=for-the-badge" alt="JSONB database">
</p>

## ✨ About the project

RKD Domain Core is the central data-definition service for the RKD platform.

It provides a REST API for defining business domains and their configurable attributes. Domains can be organized in a parent–child hierarchy, while `OPTION` attributes can reference reusable option lists stored as PostgreSQL `JSONB` documents.

The service is built with Quarkus and follows a layered architecture with REST resources, application services, Panache repositories, JPA entities, DTOs, and MapStruct mappers.

## 📚 Table of contents

- [Technologies](#-technologies)
- [Features](#-features)
- [Project structure](#-project-structure)
- [Getting started](#-getting-started)
- [API overview](#-api-overview)
- [Error responses](#-error-responses)
- [License](#-license)

## 🛠 Technologies

| Technology | Purpose |
| --- | --- |
| [Java 25](https://openjdk.org/) | Application runtime and language |
| [Quarkus](https://quarkus.io/) | Supersonic Subatomic Java framework |
| [Jakarta REST](https://jakarta.ee/specifications/restful-ws/) | REST endpoint implementation |
| [Hibernate ORM with Panache](https://quarkus.io/guides/hibernate-orm-panache) | Persistence and repository abstraction |
| [PostgreSQL](https://www.postgresql.org/) | Relational database |
| [Jackson](https://github.com/FasterXML/jackson) | JSON serialization and deserialization |
| [MapStruct](https://mapstruct.org/) | Compile-time DTO and entity mapping |
| [Maven](https://maven.apache.org/) | Build and dependency management |
| [JUnit 5](https://junit.org/junit5/) | Unit testing framework |
| [Mockito](https://site.mockito.org/) | Service-level test isolation |
| [REST Assured](https://rest-assured.io/) | REST integration testing |
| [Docker Compose](https://docs.docker.com/compose/) | Local PostgreSQL environment |

## 🚀 Features

### Domain management

- Create root domains or child domains.
- Assign a parent domain using `parentDomainId`.
- Prevent missing parents, self-references, and cyclic hierarchies.
- List domains and filter them by active status.
- Update domain code, description, status, and parent.
- Prevent deleting domains that still have subdomains.
- Return subdomains as shallow summaries to avoid recursive JSON responses.

### Attribute management

- Create attributes linked to a domain.
- Support the following attribute types:

  `NUMBER`, `DECIMAL`, `TEXT`, `DATE`, `DATE_TIME`, `MONEY`, `OPTION`, and `JSON`.

- Mark attributes as mandatory or optional.
- Associate `OPTION` attributes with previously created option lists.
- Filter attributes by active status.
- Update and delete attributes.

### Option management

- Store reusable option lists in PostgreSQL `JSONB`.
- Require at least two option values.
- Filter options by active status.
- Update option values, descriptions, and status.
- Delete option lists when they are no longer referenced.

### Validation and error handling

- Consistent JSON error responses.
- Dedicated exception mappers for not-found, invalid-data, and invalid-action errors.
- HTTP status codes mapped to domain failures.

## 📁 Project structure

```text
src/
├── main/
│   ├── java/rkd/com/
│   │   ├── definition/   # HTTP exception mappers
│   │   ├── dto/          # Shared response DTOs
│   │   ├── exception/    # Domain exceptions
│   │   ├── mapper/       # MapStruct mappers
│   │   ├── message/      # Standardized messages
│   │   ├── model/        # JPA entities
│   │   ├── repository/   # Panache repositories
│   │   ├── request/      # Request payloads
│   │   ├── resource/     # REST resources
│   │   ├── response/     # Response payloads
│   │   ├── service/      # Business rules
│   │   └── type/         # Enumerations
│   └── resources/
│       └── application.properties
├── test/
│   ├── java/rkd/com/
│   │   ├── resource/     # REST Assured integration tests
│   │   └── service/      # Unit tests
│   └── resources/
│       └── application.properties
├── docker-compose.yml    # Local PostgreSQL service
├── pom.xml               # Maven configuration
└── README.md             # Project documentation
```

### Domain relationship model

```text
Domain
├── parent              # optional parent domain
├── subdomains[]        # child domains
├── attributes[]        # configurable attributes
└── status

Attribute
└── option              # used when type = OPTION
```

## 🧭 Getting started

### Prerequisites

- [Java 25](https://openjdk.org/) installed;
- [Maven 3.9+](https://maven.apache.org/) installed;
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) with Docker Compose;
- PostgreSQL, if you are not using the provided Compose setup.

### Start PostgreSQL

From the project root, start the database:

```bash
docker compose up -d
```

The Compose configuration creates:

| Setting | Value |
| --- | --- |
| Database | `domain_core` |
| Username | `postgres` |
| Password | `postgres` |
| Port | `5432` |

Check the container status:

```bash
docker compose ps
```

Stop the database when needed:

```bash
docker compose down
```

### Environment variables

The default values work with the provided `docker-compose.yml`. They can be overridden when starting the application:

| Variable | Default | Description |
| --- | --- | --- |
| `DB_USERNAME` | `postgres` | PostgreSQL username |
| `DB_PASSWORD` | `postgres` | PostgreSQL password |
| `DB_URL` | `jdbc:postgresql://localhost:5432/domain_core` | JDBC connection URL |
| `QUARKUS_HIBERNATE_ORM_LOG_SQL` | `false` | Enable Hibernate SQL logging |

### Run in development mode

```bash
mvn quarkus:dev
```

The API will be available at:

```text
http://localhost:8080
```

The Quarkus Dev UI will be available at:

```text
http://localhost:8080/q/dev/
```

### Run the tests

Make sure PostgreSQL is running, then execute:

```bash
mvn test
```

Run the complete Maven verification lifecycle:

```bash
mvn verify
```

The integration tests validate request headers, path parameters, query parameters, CRUD operations, and persistence against PostgreSQL.

### Build and run the application

Create the JVM package:

```bash
mvn package
```

Run the generated application:

```bash
java -jar target/quarkus-app/quarkus-run.jar
```

### Build a native executable

With GraalVM configured:

```bash
mvn package -Dnative
```

Without a local GraalVM installation:

```bash
mvn package -Dnative -Dquarkus.native.container-build=true
```

## 🔌 API overview

| Resource | Base path | Main operations |
| --- | --- | --- |
| Domains | `/domain` | Create, list, filter, update, delete |
| Attributes | `/attribute` | Create, list, filter, update, delete |
| Options | `/option` | Create, list, filter, update, delete |

### Example: create a root domain

```http
POST /domain
Content-Type: application/json
Accept: application/json
```

```json
{
  "code": "CUSTOMER",
  "description": "Customer domain"
}
```

### Example: create a subdomain

```json
{
  "code": "CUSTOMER_ADDRESS",
  "description": "Customer address",
  "parentDomainId": 1
}
```

### Example: filter active resources

```http
GET /domain?status=true
GET /attribute?status=true
GET /option?status=true
```

## ⚠️ Error responses

Errors follow a consistent structure:

```json
{
  "message": "Parent domain not found.",
  "type": "DOMAIN_NOT_FOUND",
  "timestamp": "2026-09-07T15:00:00"
}
```

Available error types include:

- `DOMAIN_NOT_FOUND`
- `INVALID_DATA`
- `INVALID_ACTION`

## 📄 License

This project is distributed under the terms described in [LICENSE](LICENSE).
