# Employee Management System

Spring Boot 3.3 + Spring Data JPA + MySQL.

## Run

1. Create the database: `employee_db` is created automatically (user `root`, password `12345` by default)
2. Set the connection in `src/main/resources/application.properties`
   (or env vars `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`).
3. `mvn spring-boot:run` (tables `department` and `employee` are created automatically).

## API

| Method | URL | Description |
|---|---|---|
| GET | `/api/departments` | List departments |
| GET/PUT/DELETE | `/api/departments/{id}` | Get / update / delete a department |
| POST | `/api/departments` | Create `{ "name": "IT" }` |
| GET | `/api/employees?name=&department=&departmentId=` | List / search employees |
| GET/PUT/DELETE | `/api/employees/{id}` | Get / update / delete an employee |
| POST | `/api/employees` | Create `{ "name", "email", "password", "departmentId" }` |
