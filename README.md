# PopJav

> A **Java** learning platform built as **Spring Boot microservices**.
> Chapters, lessons and quizzes turn theory into practice, with progress tracking.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-6DB33F)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2023.0.0-6DB33F)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1)
![MongoDB](https://img.shields.io/badge/MongoDB-7-47A248)
![Tests](https://img.shields.io/badge/tests-JUnit5%20%2B%20JaCoCo-blue)

Project built for the French **DWWM** professional certification (Web and Mobile Web Developer).

---

## Table of contents

- [Features](#features)
- [Architecture](#architecture)
- [Tech stack](#tech-stack)
- [Data model](#data-model)
- [Prerequisites](#prerequisites)
- [Getting started](#getting-started)
- [Configuration](#configuration)
- [API routes](#api-routes)
- [Security](#security)
- [Tests and coverage](#tests-and-coverage)
- [Project structure](#project-structure)
- [Roadmap](#roadmap)
- [Author](#author)

---

## Features

- 📚 **Learning content**: chapters → lessons, with a publicly browsable catalog.
- 📝 **Interactive quizzes**: lives system, scoring, passing threshold, server-side grading.
- 👤 **Accounts & roles**: sign-up / login, `USER` / `ADMIN` roles.
- 💬 **Comments** on lessons (stored in MongoDB).
- 📊 **Progress tracking** from the user profile.
- 🛠️ **Admin back-office**: manage chapters, lessons, quizzes, questions and answers.
- ♿ **Responsive and accessible UI** (labels, keyboard focus, ARIA, color contrast).

---

## Architecture

The application is split into **7 Spring Boot microservices**, with service discovery
(Consul), a single entry point (Spring Cloud Gateway) and internal calls over OpenFeign.

```mermaid
flowchart TD
    B[Browser] --> UI[ui-service<br/>Thymeleaf · sessions]
    UI --> GW[api-gateway<br/>JWT · RBAC · routing]
    GW --> AUTH[auth-service]
    GW --> CONTENT[content-service]
    GW --> QUIZ[quiz-service]
    GW --> COMMENT[comment-service]
    AUTH --> PERS[persistence-service]
    CONTENT --> PERS
    QUIZ --> PERS
    PERS --> PG[(PostgreSQL)]
    COMMENT --> MONGO[(MongoDB)]
    GW -. discovery .- CONSUL{{Consul}}
```

| Service | Responsibility |
|---|---|
| `api-gateway` | Single entry point, JWT validation, role-based access control (RBAC). |
| `auth-service` | Sign-up / login, BCrypt hashing, JWT issuance. |
| `content-service` | Facade for chapters and lessons. |
| `quiz-service` | Quizzes, questions, answers, grading and result computation. |
| `comment-service` | Comments (MongoDB). |
| `persistence-service` | Relational data access (PostgreSQL source of truth). |
| `ui-service` | Web UI (Thymeleaf) consuming the API through the gateway. |

---

## Tech stack

| Area | Technologies |
|---|---|
| **Language / build** | Java 17, Maven (wrapper), Lombok |
| **Framework** | Spring Boot 3.2.1, Spring Cloud 2023.0.0 |
| **Microservices** | Spring Cloud Gateway, Consul Discovery, OpenFeign, HashiCorp Consul 1.21.3, Actuator |
| **Security** | Spring Security, JWT (jjwt 0.11.5), BCrypt |
| **Persistence** | Spring Data JPA / Hibernate, PostgreSQL 16, Spring Data MongoDB, MongoDB 7, Bean Validation |
| **Front-end** | Thymeleaf, Tailwind CSS, custom CSS, vanilla JavaScript, Google Fonts |
| **Tests / quality** | JUnit 5, Mockito, Spring Security Test, JaCoCo 0.8.11 |
| **DevOps** | Docker, Docker Compose, Jib (image build), Docker Hub, Git / GitHub |

---

## Data model

**PostgreSQL** holds the learning domain; **MongoDB** holds the comments
(*polyglot persistence*). Cross-service references (`RESULT.user_id`,
`COMMENT.user_id/lesson_id`) are **logical** (no foreign key), to keep the bounded
contexts loosely coupled.

```mermaid
erDiagram
    CHAPTER  ||--o{ LESSON   : contains
    LESSON   ||--o| QUIZ     : has
    QUIZ     ||--|{ QUESTION : "is composed of"
    QUESTION ||--|{ ANSWER   : offers
    USERS    ||--o{ RESULT   : takes
    QUIZ     ||--o{ RESULT   : "is scored in"
    USERS    ||..o{ COMMENT  : writes
    LESSON   ||..o{ COMMENT  : "is commented in"

    USERS {
        bigint id PK
        string username UK
        string email UK
        string password
        enum role
        datetime created_at
    }
    CHAPTER {
        bigint id PK
        string title
        string description
        int order_index
    }
    LESSON {
        bigint id PK
        string title
        string content
        int order_index
        bigint chapter_id FK
    }
    QUIZ {
        bigint id PK
        string title
        int lives
        int passing_score
        bigint lesson_id FK
    }
    QUESTION {
        bigint id PK
        string text
        bigint quiz_id FK
    }
    ANSWER {
        bigint id PK
        string text
        boolean correct
        bigint question_id FK
    }
    RESULT {
        bigint id PK
        int score
        boolean completed
        int attempts
        bigint user_id "logical ref"
        bigint quiz_id FK
    }
    COMMENT {
        string id PK
        string text
        bigint user_id "logical ref"
        bigint lesson_id "logical ref"
        datetime created_at
    }
```

---

## Prerequisites

- **JDK 17**
- **Maven 3.9+** (or the bundled `./mvnw` wrapper)
- **Docker** + **Docker Compose**

---

## Getting started

### 1. Configuration

```bash
cp .env.example .env
```
Fill in at least `JWT_SECRET` (see the command in `.env.example`) and the database passwords.

### 2. Start the whole stack

The service images are published on Docker Hub. `docker compose` pulls them and starts
the infrastructure (PostgreSQL, MongoDB, Consul) **and** the 7 microservices:

```bash
docker compose pull
docker compose up -d
```
> Give the services ~30–60 s to register with Consul on first start.

### 3. Access

- Web UI: **http://localhost:8085**
- API gateway: http://localhost:8080
- Consul UI: http://localhost:8500

### Running from source (development)

Alternatively, start only the infrastructure and run each service from your IDE
(**`persistence-service` first**):

```bash
docker compose up -d postgres mongo consul
cd persistence-service && ./mvnw spring-boot:run
# ...then auth, content, quiz, comment, api-gateway, ui-service
```

---

## Configuration

All sensitive configuration is externalized in `.env` (not versioned). See
`.env.example` for the full list: service ports, PostgreSQL / MongoDB credentials,
Consul host, JWT secret and lifetime.

---

## API routes

| Prefix | Service | Authentication |
|---|---|---|
| `/auth/**` | auth-service | Public |
| `/api/chapters/summary` | content-service | **Public** (catalog) |
| `/api/chapters/**`, `/api/lessons/**` | content-service | JWT |
| `/api/quizzes/**`, `/api/questions/**`, `/api/answers/**`, `/api/results/**` | quiz-service | JWT |
| `/api/comments/**` | comment-service | JWT |
| `/api/users/**` | persistence-service | JWT |

RBAC rules (enforced at the gateway):
- **Content writes** (`POST`/`PUT`/`DELETE` on chapters, lessons, quizzes, questions, answers) → **ADMIN**.
- **User actions** (submitting a quiz, posting a comment, saving a result) → open.
- User listing, account deletion, internal `/api/users/credentials` endpoint → **ADMIN**.

---

## Security

- Passwords hashed with **BCrypt**, never returned to the client (`UserResponseDTO`).
- **JWT** authentication (HS256), validated centrally at the API gateway.
- **RBAC** by role for sensitive operations.
- **IDOR protection**: the gateway extracts the identity from the JWT and injects it as a
  trusted header (`X-User-Id`); client-supplied identifiers are ignored.
- **Generic login error** message (prevents account enumeration).
- Quiz answers **hidden** from the client (the `correct` flag is never exposed).
- Secrets externalized (`.env`, not versioned).

---

## Tests and coverage

Unit tests (JUnit 5 + Mockito) on the core business logic — `AuthService`
(sign-up / login) and `QuizService` (score computation). Coverage measured with **JaCoCo**.

```bash
cd auth-service && ./mvnw clean test
# report: target/site/jacoco/index.html
```

---

## Project structure

```
PopJav/
├── api-gateway/          # Gateway (routing, JWT, RBAC)
├── auth-service/         # Authentication, JWT
├── content-service/      # Chapters, lessons
├── quiz-service/         # Quizzes, questions, answers, results
├── comment-service/      # Comments (MongoDB)
├── persistence-service/  # Data access (PostgreSQL)
├── ui-service/           # Thymeleaf front-end
├── docker-compose.yml    # Full stack (infra + 7 services)
└── .env.example          # Configuration template
```

---

## Roadmap

- [x] **Full containerization**: images built with Jib and published to Docker Hub,
      full-stack orchestration (services + infra) via `docker-compose`.
- [ ] English code comments across the whole project.
- [ ] Extend test coverage to the remaining services.

---

## Author

**Enzo Gavini** — DWWM certification project.
