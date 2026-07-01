# PopJav

Plateforme d'apprentissage du langage **Java** organisee en **microservices Spring Boot**.
L'utilisateur consulte des chapitres et des lecons, passe des quiz et suit sa progression
depuis une interface web (Thymeleaf).

Projet realise dans le cadre du titre professionnel **DWWM** (Developpeur Web et Web Mobile).

---

## Architecture

```
                      Navigateur
                          |
                    +-----------+
                    | ui-service|  (Thymeleaf, sessions)
                    +-----------+
                          |  (Feign + JWT)
                    +-------------+
                    | api-gateway |  (Spring Cloud Gateway, filtre JWT)
                    +-------------+
             ______________|________________________
            |         |          |          |       |
        auth-    content-    quiz-      comment-   (users)
        service  service     service    service
            |         |          |          |       |
            +---------+----------+----------+-------+
                          |
                  persistence-service  ----> PostgreSQL
                                              (users, chapters,
                                               lessons, quizzes,
                                               questions, answers,
                                               results)
        comment-service ---------------------> MongoDB (commentaires)

        Consul : service discovery + load balancing (lb://)
```

| Service               | Role                                                              |
|-----------------------|-------------------------------------------------------------------|
| `api-gateway`         | Point d'entree unique, validation JWT, controle d'acces par role. |
| `auth-service`        | Inscription / connexion, hachage BCrypt, emission des JWT.        |
| `content-service`     | Facade des chapitres et lecons.                                   |
| `quiz-service`        | Quiz, questions, reponses, correction et calcul des resultats.    |
| `comment-service`     | Commentaires (stockes dans MongoDB).                              |
| `persistence-service` | Acces aux donnees relationnelles (source de verite PostgreSQL).   |
| `ui-service`          | Interface web (Thymeleaf) consommant l'API via la gateway.        |

### Stack technique
- Java 17+, Spring Boot, Spring Cloud Gateway, OpenFeign
- HashiCorp Consul (service discovery)
- PostgreSQL, MongoDB
- JWT (HS256), BCrypt
- Thymeleaf, HTML/CSS/JS
- Maven, Docker Compose

---

## Prerequis
- JDK 17 ou superieur
- Maven 3.9+ (ou le wrapper `./mvnw` fourni dans chaque service)
- Docker + Docker Compose (pour PostgreSQL, MongoDB et Consul)

---

## Installation et lancement

### 1. Configuration
```bash
cp .env.example .env
```
Editez `.env` et renseignez au minimum :
- `JWT_SECRET` (voir la commande `openssl` dans `.env.example`),
- les mots de passe `PERSISTENCE_DB_PASSWORD` et `COMMENT_DB_PASSWORD`.

### 2. Infrastructure (bases de donnees + Consul)
```bash
docker compose up -d
```
Consul UI : http://localhost:8500

### 3. Lancement des services
Dans un terminal par service (ou via votre IDE) :
```bash
cd persistence-service && ./mvnw spring-boot:run
cd auth-service        && ./mvnw spring-boot:run
cd content-service     && ./mvnw spring-boot:run
cd quiz-service        && ./mvnw spring-boot:run
cd comment-service     && ./mvnw spring-boot:run
cd api-gateway         && ./mvnw spring-boot:run
cd ui-service          && ./mvnw spring-boot:run
```
Demarrez `persistence-service` en premier ; les autres s'enregistrent ensuite
aupres de Consul.

### 4. Acces
Interface web : http://localhost:${UI_SERVICE_PORT} (par defaut http://localhost:8085)

---

## Routes principales (via l'API Gateway)

| Prefixe            | Service cible        | Authentification |
|--------------------|----------------------|------------------|
| `/auth/**`         | auth-service         | Public           |
| `/api/chapters/**` | content-service      | JWT              |
| `/api/lessons/**`  | content-service      | JWT              |
| `/api/quizzes/**`  | quiz-service         | JWT              |
| `/api/questions/**`| quiz-service         | JWT              |
| `/api/answers/**`  | quiz-service         | JWT              |
| `/api/results/**`  | quiz-service         | JWT              |
| `/api/comments/**` | comment-service      | JWT              |
| `/api/users/**`    | persistence-service  | JWT (voir ci-dessous) |

Regles specifiques `/api/users` appliquees a la gateway :
- `GET /api/users` (liste complete) : **ADMIN uniquement**
- `DELETE /api/users/{id}` : **ADMIN uniquement**
- `GET /api/users/credentials` : endpoint interne (auth-service), **bloque cote gateway**

---

## Securite
- Mots de passe haches avec **BCrypt**, jamais stockes ni renvoyes en clair.
- Aucun endpoint public ne renvoie le hash du mot de passe (DTO `UserResponseDTO`).
- Authentification par **JWT** (HS256) ; validation centralisee a l'API Gateway.
- Controle d'acces par role pour les operations sensibles sur les comptes.
- Message de connexion generique (« Email ou mot de passe incorrect ») pour eviter
  l'enumeration des comptes.
- Toute la configuration sensible (secrets, identifiants de bases) est externalisee
  dans `.env`, non versionne.
