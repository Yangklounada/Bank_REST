# 💳 Bank REST API

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-success)
![Spring Security](https://img.shields.io/badge/Spring_Security-6-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![JWT](https://img.shields.io/badge/Auth-JWT-red)
![Liquibase](https://img.shields.io/badge/Database-Liquibase-yellow)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED)

---

# 📖 О проекте

**Bank REST API** — серверное приложение для управления банковскими картами и денежными переводами между ними.

Проект реализован на **Spring Boot** с использованием современной архитектуры REST API, JWT-аутентификации, разграничения ролей пользователей, шифрования конфиденциальных данных и миграций базы данных через Liquibase.

Основной целью проекта является демонстрация построения безопасного банковского backend-приложения с использованием современных технологий Java.

---

# 🚀 Возможности

## Пользователь

- регистрация
- авторизация по JWT
- просмотр своих карт
- просмотр информации о карте
- просмотр баланса
- выполнение переводов между картами
- просмотр истории операций
- постраничный вывод данных
- валидация всех входящих запросов

---

## Администратор

Помимо возможностей пользователя:

- создание банковских карт
- блокировка карт
- разблокировка карт
- удаление карт
- управление пользователями
- просмотр информации обо всех картах

---

# 🔒 Безопасность

В проекте реализованы современные механизмы защиты.

### JWT Authentication

После успешной авторизации сервер выдает JWT-токен.

Каждый защищенный запрос проходит через собственный JWT Filter.

---

### Spring Security

Используется:

- SecurityFilterChain
- AuthenticationManager
- PasswordEncoder (BCrypt)
- Role-based authorization
- Stateless Session Policy

---

### Шифрование банковских карт

Номера банковских карт **не хранятся в открытом виде**.

Используется

- AES/GCM

что обеспечивает:

- конфиденциальность данных;
- защиту от изменения;
- безопасное хранение номеров карт.

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

Каждый слой отвечает только за свою область ответственности.

---

# 📂 Структура проекта

```
src
│
├── config
│
├── controller
│
├── dto
│
├── entity
│
├── exception
│
├── mapper
│
├── repository
│
├── security
│
├── service
│
├── util
│
└── validation
```

---

# ⚙️ Используемые технологии

## Backend

- Java 17
- Spring Boot 3
- Spring MVC
- Spring Security
- Spring Data JPA
- Hibernate

---

## База данных

- PostgreSQL
- Liquibase

---

## Документация

- Swagger / OpenAPI

---

## Безопасность

- JWT
- BCrypt
- AES/GCM Encryption

---

## Сборка

- Maven

---

## Контейнеризация

- Docker
- Docker Compose

---

# 🗄 База данных

Для управления схемой используется **Liquibase**.

При запуске приложения автоматически выполняются миграции.

Это обеспечивает:

- создание таблиц;
- создание ограничений;
- создание индексов;
- актуальную структуру БД.

---

# 📡 REST API

Основные группы API.

## Authentication

```
POST /auth/register

POST /auth/login
```

---

## Cards

```
GET

POST

PUT

DELETE
```

Работа с банковскими картами.

---

## Transfers

```
POST /transfer
```

Выполнение денежных переводов между картами.

---

# 📑 Swagger

После запуска проекта документация доступна по адресу

```
http://localhost:8080/swagger-ui/index.html
```

---

# 🐳 Запуск через Docker

```bash
docker-compose up -d
```

После запуска автоматически поднимется PostgreSQL.

---

# ▶️ Локальный запуск

### 1.

Клонировать проект

```bash
git clone <repository>
```

### 2.

Запустить PostgreSQL

### 3.

Создать БД

```
bank_cards
```

### 4.

Настроить

```
application.yml
```

или

```
application.properties
```

### 5.

Запустить

```bash
mvn spring-boot:run
```

или

```bash
./mvnw spring-boot:run
```

---

# 🔄 Логика перевода средств

При выполнении перевода приложение:

- проверяет существование карт;
- проверяет владельца;
- проверяет статус карты;
- проверяет достаточность средств;
- выполняет перевод в рамках транзакции;
- сохраняет информацию об операции.

---

# 📄 Валидация

Используются возможности Spring Validation.

Проверяются:

- корректность входных данных;
- обязательные поля;
- размеры строк;
- номера карт;
- ограничения бизнес-логики.

---

# ❗ Обработка ошибок

Используется централизованная обработка исключений.

Возвращаются понятные HTTP-ответы:

- 400 Bad Request
- 401 Unauthorized
- 403 Forbidden
- 404 Not Found
- 409 Conflict
- 500 Internal Server Error

---

# 🎯 Особенности проекта

✔ JWT Authentication

✔ Role Based Security

✔ REST API

✔ PostgreSQL

✔ Liquibase

✔ Docker

✔ Swagger

✔ AES Encryption

✔ Pagination

✔ DTO Pattern

✔ Layered Architecture

✔ Validation

✔ Exception Handling
