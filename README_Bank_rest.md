Система управления банковскими картами

REST API для управления банковскими картами, пользователями и переводами.

Технологии

Java 17, Spring Boot 3.2.0
Spring Security + JWT аутентификация
Spring Data JPA + PostgreSQL
Liquibase миграции
Swagger/OpenAPI документация
AES/GCM шифрование номеров карт
Быстрый старт

Запустить PostgreSQL: docker-compose up -d
Собрать и запустить приложение: ./mvnw spring-boot:run
Swagger UI: http://localhost:8080/swagger-ui.html
API Endpoints

Auth (доступны без токена)

POST /api/auth/register — регистрация нового пользователя POST /api/auth/login — вход, возвращает JWT токен

Cards (требуют JWT)

POST /api/cards — создать карту GET /api/cards?page=0&size=10 — список карт пользователя (с пагинацией) GET /api/cards/{id} — получить карту по ID PATCH /api/cards/{id}/block — заблокировать карту DELETE /api/cards/{id} — удалить карту

Transfers (требуют JWT)

POST /api/transfers — перевод между своими картами

Admin (требуют ROLE_ADMIN)

GET /api/admin/users — список всех пользователей GET /api/admin/users/{id} — пользователь по ID DELETE /api/admin/users/{id} — удалить пользователя PATCH /api/admin/users/{id}/role — назначить администратора

Безопасность

Для доступа к защищённым endpoint'ам требуется JWT токен. Передаётся в заголовке: Authorization: Bearer

Пример использования

Регистрация

curl -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d '{"username":"ivan","email":"ivan@mail.ru","password":"123456"}'

Создание карты

curl -X POST http://localhost:8080/api/cards -H "Content-Type: application/json" -H "Authorization: Bearer " -d '{"cardNumber":"1234567890123456","cardHolder":"Ivan Ivanov","expiryDate":"2028-12-31","initialBalance":1000}'

Перевод

curl -X POST http://localhost:8080/api/transfers -H "Content-Type: application/json" -H "Authorization: Bearer " -d '{"fromCardId":1,"toCardId":2,"amount":100}'
