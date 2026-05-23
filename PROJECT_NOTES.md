# Employee Management System Notes

## Current Goal

Build an Employee Management System with Vue.js and Spring Boot while learning:

- GitHub
- Docker
- Kubernetes
- AWS or Azure
- Datadog

## GitHub Repository

Repository:

```text
https://github.com/smalline/employee-management-system
```

Local folder:

```text
C:\Users\sahit\OneDrive\Desktop\ReactPractice\code\employee-management-system
```

## Completed Milestones

### 1. GitHub Setup

- Installed Git.
- Installed GitHub CLI.
- Logged in to GitHub as `smalline`.
- Created GitHub repo.
- Initialized local Git repo.
- Pushed project to GitHub.

Commits:

```text
af8835f chore: initialize employee management project
```

### 2. Spring Boot Backend Starter

Generated backend using Spring Initializr.

Backend stack:

- Spring Boot
- Java 21 target
- Maven Wrapper
- Spring Web MVC
- Spring Data JPA
- Spring Security
- Validation
- Actuator
- PostgreSQL driver
- H2 database for early local development/tests

Added:

- `GET /api/status`
- basic `SecurityConfig`
- H2 configuration in `application.properties`

Verified:

```powershell
cd backend
.\mvnw.cmd test
.\mvnw.cmd package
java -jar target\backend-0.0.1-SNAPSHOT.jar
Invoke-RestMethod http://localhost:8080/api/status
```

Commit:

```text
8de8fe8 feat: add Spring Boot backend starter
```

### 3. Employee And Department CRUD APIs

Added layered backend architecture:

- Controller layer for REST endpoints
- Service layer for business logic
- Repository layer for database persistence
- DTOs for request/response API contracts
- JPA entities for database tables
- Global exception handling

Added:

- Department CRUD API
- Employee CRUD API
- `Department` JPA entity
- `Employee` JPA entity
- Many employees belong to one department
- Duplicate checks for department name and employee email
- Validation errors return `400`
- Missing records return `404`
- Duplicate records return `409`

Important learning note:

- We hit an employee listing issue caused by lazy department loading.
- Fixed it with `@EntityGraph(attributePaths = "department")` in `EmployeeRepository`.
- Interview wording: "I used EntityGraph to fetch related department data for employee API responses and avoid lazy loading issues after the transaction boundary."

Verified:

```text
DepartmentId: 1
EmployeeId: 1
EmployeeCount: 1
EmployeeEmail: ava.patel@example.com
DepartmentName: Engineering
```

Commit:

```text
e45518d feat: add employee and department CRUD APIs
```

## Concepts Explained

### H2

H2 is a lightweight Java database.

In this project, H2 is used as an in-memory database for early development and tests.

Interview wording:

```text
I used H2 as an embedded in-memory database for early development and tests because it allowed the Spring Boot application context and JPA mappings to be verified without external infrastructure.
```

### Hibernate

Hibernate is an ORM, not a database.

It maps Java entities to database tables and generates SQL.

Relationship:

```text
Spring Boot app -> Hibernate/JPA -> H2 or PostgreSQL
```

### Docker

Docker packages an application and its runtime dependencies into an image.

- Image: blueprint/package
- Container: running instance of an image
- Dockerfile: recipe for building an image
- Docker Compose: runs multiple containers together

Interview wording:

```text
A Docker image is a packaged, versioned artifact containing the application and runtime dependencies. A container is a running instance of that image.
```

## Docker Status

Docker Desktop was installed with `winget`.

Observed issue:

```text
Docker returned 500 Internal Server Error from dockerDesktopLinuxEngine
```

After restart, Docker CLI worked but Docker daemon still could not start:

```text
Docker Desktop is unable to start
```

Root cause found:

```text
Windows Subsystem for Linux is not installed
```

Attempting to enable WSL with DISM failed because the terminal was not elevated:

```text
Error: 740
Elevated permissions are required to run DISM.
```

Required fix:

- Open PowerShell as Administrator.
- Enable WSL and Virtual Machine Platform.
- Restart Windows.
- Open Docker Desktop.
- Let Docker finish starting.

Docker was later fixed by installing/enabling WSL 2 and restarting Windows.

Verified Docker commands:

```powershell
docker info
docker compose config
docker compose up --build -d
```

Verification results:

```text
ems-postgres: running and healthy
ems-backend: running on localhost:8080
GET /api/status: UP
PostgreSQL departments count: 1
PostgreSQL employees count: 1
```

## Docker Files Added Locally But Not Committed Yet

These files exist locally but have not been committed/pushed yet because Docker could not be fully verified:

```text
backend/Dockerfile
backend/.dockerignore
backend/src/main/resources/application-docker.properties
docker-compose.yml
README.md Docker section
```

## After Restart: Continue Here

Open Docker Desktop first.

If Docker still cannot start because WSL is missing, open PowerShell as Administrator and run:

```powershell
dism.exe /online /enable-feature /featurename:Microsoft-Windows-Subsystem-Linux /all /norestart
dism.exe /online /enable-feature /featurename:VirtualMachinePlatform /all /norestart
wsl --set-default-version 2
```

Then restart Windows.

After restart, open Docker Desktop first.

Then open a new normal PowerShell:

```powershell
cd C:\Users\sahit\OneDrive\Desktop\ReactPractice\code\employee-management-system
docker --version
docker compose version
docker info
```

If `docker info` works, run:

```powershell
docker compose up --build
```

Then test:

```powershell
Invoke-RestMethod http://localhost:8080/api/status
```

Create a department:

```powershell
Invoke-RestMethod -Method Post `
  -Uri http://localhost:8080/api/departments `
  -ContentType "application/json" `
  -Body '{"name":"Engineering","description":"Builds and maintains software systems"}'
```

Create an employee:

```powershell
Invoke-RestMethod -Method Post `
  -Uri http://localhost:8080/api/employees `
  -ContentType "application/json" `
  -Body '{"firstName":"Ava","lastName":"Patel","email":"ava.patel@example.com","jobTitle":"Software Engineer","hireDate":"2026-05-23","departmentId":1}'
```

List employees:

```powershell
Invoke-RestMethod http://localhost:8080/api/employees
```

If Docker verification works, commit and push:

```powershell
git status
git add README.md backend/.dockerignore backend/Dockerfile backend/src/main/resources/application-docker.properties docker-compose.yml PROJECT_NOTES.md
git commit -m "feat: add Docker Compose backend environment"
git push
```

## Next Planned Milestones

1. Commit/push full-stack Docker frontend milestone.
2. Add GitHub Actions CI.
3. Add Kubernetes manifests.
4. Deploy to AWS or Azure.
5. Add Datadog monitoring.

## Vue Frontend Milestone

Added a Vue 3 frontend generated with Vite.

Frontend stack:

- Vue 3
- Vite
- Axios

Added:

- Dashboard summary cards
- Department creation form
- Employee creation form
- Employee table
- Recent hires list
- `src/api.js` API service layer
- `VITE_API_BASE_URL` environment variable support

Backend change:

- Added CORS configuration allowing `http://localhost:5173`.

Verified:

```powershell
cd frontend
npm run build
```

Also verified backend CORS:

```text
Access-Control-Allow-Origin: http://localhost:5173
```

Development URL:

```text
http://127.0.0.1:5173
```

## Full-Stack Docker Frontend Milestone

Added:

- `frontend/Dockerfile`
- `frontend/nginx.conf`
- `frontend/.dockerignore`
- `frontend` service in `docker-compose.yml`
- Vite dev proxy for `/api` and `/actuator`

Containerized frontend behavior:

- Node builds the Vue app.
- Nginx serves the production static assets.
- Nginx proxies `/api` requests to `http://backend:8080`.

Verified:

```powershell
docker compose up --build -d
docker compose ps
Invoke-WebRequest http://localhost:5173
Invoke-RestMethod http://localhost:5173/api/status
Invoke-RestMethod http://localhost:5173/api/employees
```

Result:

```text
ems-frontend: running on localhost:5173
ems-backend: running on localhost:8080
ems-postgres: healthy on localhost:5432
```
