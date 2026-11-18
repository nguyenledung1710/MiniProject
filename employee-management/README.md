# Employee Management System

Spring Boot 3.3 + Spring Data JPA + MySQL + Thymeleaf.

## Run

1. Start MySQL. The database `employee_db` is created automatically (dev profile: user `root`, password `12345`).
2. `mvn spring-boot:run` (profile `dev` is active by default, tables are created automatically).
3. Production profile: set `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET` and run with `-Dspring-boot.run.profiles=prod`.

## Security (Module 9)

Every API and web page requires login. A default admin account is created at startup:
username `admin`, password `admin123` (change it for any real deployment).

| Role | Permissions |
|---|---|
| `USER` | View only (GET employees, departments, report, web list/search) |
| `ADMIN` | Everything: create, update, delete employees and departments, add form, actuator |

Anyone can register (`POST /api/auth/register`), the new account always gets the role `USER`.

| Method | URL | Description |
|---|---|---|
| POST | `/api/auth/register` | Register `{ "username", "password" }` (3+ chars, 6+ chars), returns a JWT |
| POST | `/api/auth/login` | Login `{ "username", "password" }`, returns `{ "token", "username", "role" }` |

Two ways to authenticate:
- **JWT** for the API: send `Authorization: Bearer <token>` (token valid for 1 hour).
- **Login form** for the web pages (`/login`, session based, with `/register` and Logout).
- **Basic Authentication** is also accepted (for example `curl -u admin:admin123 ...`).

CSRF protection is on for the web forms (Thymeleaf adds the token automatically) and ignored for `/api/**`, which uses JWT.

Status codes: `401` not logged in or wrong password, `403` logged in but not allowed, `409` username already exists.

## API

| Method | URL | Description |
|---|---|---|
| GET | `/api/departments` | List departments |
| GET/PUT/DELETE | `/api/departments/{id}` | Get / update / delete a department |
| POST | `/api/departments` | Create `{ "name": "IT" }` |
| GET | `/api/employees?name=&department=&departmentId=` | List / search employees |
| GET/PUT/DELETE | `/api/employees/{id}` | Get / update / delete an employee |
| POST | `/api/employees` | Create `{ "name", "email", "password", "departmentId" }` |
| GET | `/api/reports/employee-count` | Total number of employees (cached for 1 minute) |

## Web pages

| Page | Who | Description |
|---|---|---|
| `/login`, `/register` | everyone | Log in and create a `USER` account |
| `/employees/list` | USER, ADMIN | List of employees |
| `/employees/search` | USER, ADMIN | Search by employee name or department name |
| `/employees/statistics` | USER, ADMIN | Statistics by department and by month |
| `/employees/add` | ADMIN | Add an employee |
| `/employees/edit/{id}` | ADMIN | Edit an employee (empty password keeps the old one) |
| `/employees/delete/{id}` | ADMIN | Delete an employee (button with confirmation) |

## Module 8: Actuator, Scheduling, Caching

### Actuator (health, metrics)

Exposed endpoints: `/actuator/health`, `/actuator/metrics`, `/actuator/info`.

`GET /actuator/health` (status UP, database MySQL UP):

![Actuator health](docs/images/actuator-health.png)

`GET /actuator/metrics/jvm.memory.used`:

![Actuator metrics](docs/images/actuator-metrics.png)

### Report API with caching (1 minute)

`GET /api/reports/employee-count` is annotated with `@Cacheable` (Caffeine, `expireAfterWrite=1m`).
Calling it twice within a minute returns the same `generatedAt`, so the second call did not hit the database.

![Report API](docs/images/report-api.png)

### Scheduled task

`SystemScheduler` logs `System running` every 30 seconds (`@Scheduled(fixedRate = 30000)`).
The `cache miss` line appears only for the first report request; the next calls within one minute are served from the cache.

![Scheduled log](docs/images/scheduled-log.png)

## Module 9 evidence: Spring Security

### Real application log

Register, wrong password, `401` without token, `403` when a USER tries to create or delete, and the admin actions:

![Application log](docs/images/security-app-log.png)

### API tests (JWT)

![API test 1](docs/images/security-api-test-1.png)

![API test 2](docs/images/security-api-test-2.png)

### Web pages (Basic Authentication)

These three screenshots were taken before the login form was added; the current login form is shown in the last section.

USER `demo_user` sees the list, and the "Add employee" link is hidden:

![USER list](docs/images/security-user-list.png)

USER opening `/employees/add` gets `403`:

![USER forbidden](docs/images/security-user-forbidden.png)

ADMIN sees the "Add employee" link and the form:

![ADMIN add form](docs/images/security-admin-add.png)

Note: since Module 9 the actuator endpoints `/actuator/metrics` and `/actuator/info` require the ADMIN role (`/actuator/health` stays public). The Module 8 screenshots above were taken before security was added.

## Module 10: Reporting & Analytics

### Statistics API

`GET /api/statistics` (roles USER and ADMIN) returns the total number of employees, the number of employees without a department, and the number of employees in each department:

```json
{
  "totalEmployees": 14,
  "employeesWithoutDepartment": 0,
  "byDepartment": [
    { "departmentName": "IT", "employeeCount": 5 },
    { "departmentName": "Kinh doanh", "employeeCount": 4 },
    { "departmentName": "HR", "employeeCount": 2 },
    { "departmentName": "Ke toan", "employeeCount": 2 },
    { "departmentName": "Marketing", "employeeCount": 1 }
  ],
  "newEmployeesByMonth": [
    { "month": "2026-06", "employeeCount": 3 },
    { "month": "2026-07", "employeeCount": 3 },
    { "month": "2026-08", "employeeCount": 2 },
    { "month": "2026-09", "employeeCount": 2 },
    { "month": "2026-10", "employeeCount": 2 },
    { "month": "2026-11", "employeeCount": 2 }
  ]
}
```

`newEmployeesByMonth` is the trend: the number of employees created in each month (from the `created_at` column). The API screenshot below was taken before this field was added.

The numbers are calculated with `@Query` methods in the repositories (`COUNT` + `LEFT JOIN` + `GROUP BY`), so departments without employees are also listed with 0.

![Statistics API](docs/images/statistics-api.png)

### Statistics page

`/employees/statistics` shows the same data with Thymeleaf: summary cards and a table with a bar for each department.

![Statistics page](docs/images/statistics-web.png)

The screenshot above was taken as ADMIN. The page is also available to the USER role (the "Add employee" link is hidden):

![Statistics page as USER](docs/images/statistics-user.png)

### Log and generated SQL

![Statistics log](docs/images/statistics-log.png)

The screenshots were taken with the sample data in the database (14 employees in 5 departments), created through the API.

## Completing the roadmap: web UI and statistics trend

Added after Module 10 so the project covers all the main features of the roadmap.

### Login and register pages

Registration validates the input, a new account gets the role `USER`:

![Login](docs/images/web-login.png)

![Register with validation errors](docs/images/web-register-errors.png)

### Employee list for ADMIN (with Edit and Delete)

The name and role of the logged in user and the Logout button are in the menu:

![List as ADMIN](docs/images/web-list-admin.png)

### Add, edit and delete from the web (ADMIN)

Add, with the success message:

![Add employee](docs/images/web-add-success.png)

Edit form (filled with the current data, empty password keeps the old one):

![Edit employee](docs/images/web-edit.png)

![Update success](docs/images/web-update-success.png)

Delete (asks for confirmation first):

![Delete success](docs/images/web-delete-success.png)

### USER view

USER sees the list without the "Add employee" link and without the Actions column:

![List as USER](docs/images/web-list-user.png)

### Statistics with the trend

By department, total, and new employees per month:

![Statistics trend](docs/images/web-statistics-trend.png)

All screenshots in this section were taken from a real browser session (Chrome) against the running application. The employee added, edited and deleted in the screenshots was a temporary one, the database was left with the 14 sample employees. The creation dates of the sample employees were spread over the last 6 months so the trend has data.
