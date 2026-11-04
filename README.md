# WagerPRO

> **Work in Progress (~85% completed)**  
> *Detailed documentation and README will be updated upon final completion.*

---

## WagerPRO - Microservices Platform

**WagerPRO** is a high-performance distributed sports betting platform built on a modern microservices architecture using **Java 21** and **Spring Boot 4**. The architecture follows Domain-Driven Design (DDD) principles with clear layer separation (`api`, `application`, `domain`, `infrastructure`).

---

## Architecture & Layering Pattern

Each microservice adheres to a strict layered structure:

* **`api`**: REST controllers, request/response DTOs, and exception handlers.
* **`application`**: Application services, use cases, and strategy implementations (e.g., `MarketEvaluatorStrategy`).
* **`domain`**: Core domain entities, value objects, domain logic, and repository interfaces.
* **`infrastructure`**: Database persistence (JPA/Hibernate), external client implementations, Kafka messaging, and configuration.

---

## Technology Stack

* **Core Language & Framework:** Java 21, Spring Boot 4
* **Inter-Service Communication:** Synchronous via Spring HTTP Interfaces (`@HttpExchange`), Asynchronous via **Apache Kafka**
* **Persistence & ORM:** PostgreSQL 17, **Hibernate** (JPA Provider), Flyway (Schema Migration)
* **Security & Auth:** Spring Security, JJWT (JSON Web Token authentication/authorization)
* **Performance & Caching:** Spring Cache (`@Cacheable`)
* **Aspect-Oriented Programming (AOP):** AspectJ for execution timing, method profiling, and centralized logging
* **Error Handling:** Centralized `GlobalExceptionHandler`
* **Design Patterns:** Strategy Pattern (`MarketEvaluatorStrategy` for dynamic bet evaluation)
* **Mapping & Boilerplate:** MapStruct, Lombok
* **Containerization:** Docker & Docker Compose

---

## Microservices Overview

1. **User Service (`8081`)**
   * Manages user profiles, wallets, authentication, and JWT issuing.
   * Database: `user_db` (Port `5433`)

2. **Catalog Service (`8082`)**
   * Manages sports events, matches, markets, and real-time odds.
   * Database: `catalog_db` (Port `5434`)

3. **Wager Service (`8083`)**
   * Processes betting tickets, evaluates market outcomes using `MarketEvaluatorStrategy`, and manages ticket states.
   * Inter-service communication with `catalog-service` via `@GetExchange` HTTP interfaces and Kafka events.
   * Database: `wager_db` (Port `5435`)

---

## Infrastructure Environment

* **Apache Kafka & Zookeeper:** Asynchronous event broker (`9092` / `2181`)
* **Kafka UI:** Web interface for monitoring topics and consumers (`http://localhost:8085`)
* **PgAdmin:** PostgreSQL database management (`http://localhost:5050`)

---

## Getting Started

### 1. Launching Infrastructure Services (Kafka)
For local development where microservices are run manually via IDE:

```bash
docker compose -f docker-compose-kafka.yml up -d
```

### 2. Launching Full System

```bash
docker compose up -d --build
```

## Future Improvements (Roadmap)

The following features and improvements are planned for upcoming development phases:
- **Test Classes**: Implementation of comprehensive unit and integration tests (JUnit 5, Mockito) to ensure code quality and reliability.
- **Virtual Threads**: Leveraging Java 21 Virtual Threads to efficiently handle high-concurrency background tasks and live odds simulation.
- **AWS S3 Integration**: Cloud storage integration for handling file uploads (e.g., media assets, user documents, or reports).
