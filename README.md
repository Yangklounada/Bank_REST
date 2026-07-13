# 💳 Bank REST API

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-success)
![Spring Security](https://img.shields.io/badge/Spring_Security-6-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![JWT](https://img.shields.io/badge/Auth-JWT-red)
![Liquibase](https://img.shields.io/badge/Database-Liquibase-yellow)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED)
![Lombok](https://img.shields.io/badge/Lombok-1.18-important)
![MapStruct](https://img.shields.io/badge/MapStruct-1.6-yellowgreen)

---

# 📖 О проекте

**Bank REST API** — серверное приложение для управления банковскими картами и денежными переводами между собственными картами.

Проект реализован на **Spring Boot** с использованием современной архитектуры REST API, JWT-аутентификации, разграничения ролей пользователей, шифрования конфиденциальных данных (AES/GCM) и миграций базы данных через Liquibase.

Основной целью проекта является демонстрация построения безопасного банковского backend-приложения с использованием современных технологий Java.

---

# 🚀 Возможности

## Пользователь

- регистрация
- авторизация по JWT
- создание банковских карт
- просмотр своих карт (постранично)
- просмотр информации о карте
- блокировка своей карты
- удаление своей карты
- выполнение переводов между своими картами
- валидация всех входящих запросов

---

## Администратор

Помимо возможностей пользователя:

- просмотр списка всех пользователей
- просмотр пользователя по ID
- удаление пользователя
- назначение роли ADMIN пользователю

---

# 🔒 Безопасность

### JWT Authentication

После успешной авторизации сервер выдает JWT-токен (HMAC-SHA256, Base64-ключ).

Каждый защищенный запрос проходит через `JwtAuthenticationFilter`.

---

### Spring Security

Используется:

- `SecurityFilterChain`
- `AuthenticationManager`
- `PasswordEncoder` (BCrypt)
- Role-based authorization (`ROLE_USER`, `ROLE_ADMIN`)
- Stateless Session Policy
- CORS (разрешены все источники для разработки)
- Публичный доступ к Swagger UI и OpenAPI spec

---

### Шифрование номеров карт

Номера банковских карт **не хранятся в открытом виде**.

Используется:

- **AES/GCM/NoPadding** — шифрование с аутентификацией
- Случайный IV (12 байт) для каждого шифрования
- Все данные хранятся в БД в зашифрованном виде
- При возврате в API номер карты маскируется (**** **** **** 1234)

---

# 🏛 Архитектура

Проект построен по классической многослойной архитектуре.

```
         REST

           │
    Controller Layer
           │
     Service Layer
           │
    Repository Layer
           │
      PostgreSQL
```

Каждый слой отвечает только за свою область ответственности. Дополнительно выделены слои мапперов (Entity → DTO) и утилит (шифрование, маскирование).

---

# 📂 Структура проекта

```
src/main/java/com/example/bankcards/
│
├── config          # CORS, Swagger/OpenAPI, Security
├── controller      # REST-контроллеры
├── dto             # DTO (запросы/ответы)
├── entity          # JPA-сущности
├── exception       # Исключения и глобальный обработчик
├── mapper          # Преобразование Entity → DTO
├── repository      # Spring Data JPA репозитории
├── security        # JWT-фильтр, провайдер, утилиты
├── service         # Бизнес-логика
└── util            # Шифрование, маскирование карт
```

---

# ⚙️ Используемые технологии

## Backend

- Java 17
- Spring Boot 3.2
- Spring MVC
- Spring Security 6
- Spring Data JPA / Hibernate
- Lombok
- MapStruct

---

## База данных

- PostgreSQL 16
- Liquibase (с `preConditions`)

---

## Документация

- Swagger UI / OpenAPI 3

---

## Безопасность

- JWT (HMAC-SHA256)
- BCrypt
- AES/GCM/NoPadding

---

## Сборка

- Maven

---

## Контейнеризация

- Docker Compose (PostgreSQL)

---

# 🗄 База данных

Для управления схемой используется **Liquibase** с `preConditions`, гарантирующими безопасный повторный запуск.

Миграции автоматически выполняются при старте приложения:

- создание таблиц `users`, `user_roles`, `cards`
- создание внешних ключей и индексов
- проверка условий перед применением (`dbms`, `runningAs`, `not tableExists`)

---

# 📡 REST API

Все endpoints, кроме аутентификации, требуют JWT-токен в заголовке `Authorization: Bearer <token>`.

## Authentication (публичные)

```
POST /api/auth/register
POST /api/auth/login
```

---

## Cards (требуют аутентификации)

```
GET    /api/cards              — список карт (постранично)
POST   /api/cards              — создать карту
GET    /api/cards/{id}         — информация о карте
PATCH  /api/cards/{id}/block   — заблокировать карту
DELETE /api/cards/{id}         — удалить карту
```

---

## Transfers (требуют аутентификации)

```
POST /api/transfers — перевод между своими картами
```

---

## Admin (требуют роли ADMIN)

```
GET    /api/admin/users          — список пользователей
GET    /api/admin/users/{id}     — пользователь по ID
DELETE /api/admin/users/{id}     — удалить пользователя
PATCH  /api/admin/users/{id}/role — назначить ADMIN
```

---

# 📑 Swagger

После запуска проекта документация доступна по адресу:

```
http://localhost:8080/swagger-ui/index.html
```

---

# 🐳 Запуск через Docker Compose

```bash
docker compose up -d
```

Запускает PostgreSQL 16 на порту **5432** с БД `bank_cards`.

---

# ▶️ Локальный запуск

### 1. Клонировать проект

```bash
git clone <repository>
cd Bank_REST
```

### 2. Запустить PostgreSQL

```bash
docker compose up -d
```

### 3. Настроить переменные окружения (опционально)

Создать `.env` из примера:

```bash
cp .env.example .env
```

Приложение также работает с fallback-значениями по умолчанию.

### 4. Запустить приложение

```bash
./mvnw spring-boot:run
```

Или:

```bash
mvn spring-boot:run
```

Приложение будет доступно на `http://localhost:8080`.

---

# 🔄 Логика перевода средств

При выполнении перевода приложение:

- проверяет существование обеих карт
- проверяет, что карты принадлежат текущему пользователю
- проверяет статус карт (обе должны быть `ACTIVE`)
- проверяет, что карты разные (нельзя перевести самому себе)
- проверяет достаточность средств на карте отправителя
- выполняет перевод в рамках одной транзакции (`@Transactional`)

---

# ❗ Обработка ошибок

Используется централизованный обработчик исключений (`@RestControllerAdvice`).

Иерархия исключений:

- `ApiException` (абстрактный базовый класс с HTTP-статусом)
  - `ResourceNotFoundException` → **404**
  - `DuplicateResourceException` → **409**
  - `TransferException` → **400**

Дополнительно обрабатываются:

- `MethodArgumentNotValidException` → **400** (детали по каждому полю)
- `Exception` → **500** (непредвиденные ошибки)

---

# 🎯 Особенности проекта

✔ JWT Authentication  
✔ Role Based Security  
✔ REST API  
✔ PostgreSQL  
✔ Liquibase (preConditions)  
✔ Docker Compose  
✔ Swagger / OpenAPI  
✔ AES/GCM Encryption  
✔ Pagination  
✔ DTO Pattern (Lombok)  
✔ Mapper Layer (MapStruct)  
✔ Layered Architecture  
✔ Validation  
✔ Centralized Exception Handling  
✔ SecurityUtils (текущий пользователь)  
✔ Secrets через env-переменные  
✔ Маскирование номеров карт  
