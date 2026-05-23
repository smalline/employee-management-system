# Employee Management System

Learning project for building and deploying a full-stack employee management system.

## Tech Stack

- Frontend: Vue.js
- Backend: Spring Boot
- Database: PostgreSQL
- Containers: Docker
- Orchestration: Kubernetes
- Cloud: AWS or Azure
- CI/CD: GitHub Actions
- Observability: Datadog

## Planned Structure

```text
employee-management-system/
  frontend/
  backend/
  k8s/
  docker-compose.yml
```

## Learning Milestones

1. Build Spring Boot employee CRUD API
2. Build Vue.js frontend
3. Connect frontend to backend
4. Add PostgreSQL
5. Dockerize frontend, backend, and database
6. Run locally with Docker Compose
7. Add GitHub Actions CI/CD
8. Deploy to local Kubernetes
9. Deploy to AWS or Azure
10. Add Datadog monitoring and dashboards

## Backend

The backend is a Spring Boot API generated with Spring Initializr.

Current dependencies:

- Spring Web MVC for REST APIs
- Spring Data JPA for database persistence
- Spring Security for authentication and authorization
- Validation for request validation
- Actuator for health and operational endpoints
- PostgreSQL driver for the planned production database
- H2 for early local development and tests

Run tests:

```powershell
cd backend
.\mvnw.cmd test
```

Build the executable JAR:

```powershell
.\mvnw.cmd package
```

Run the backend:

```powershell
java -jar target\backend-0.0.1-SNAPSHOT.jar
```

Verify the API:

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

## Docker

Docker is used to run the backend and PostgreSQL in containers.

In this setup:

- `postgres` runs a PostgreSQL database container
- `backend` builds and runs the Spring Boot API container
- `frontend` builds the Vue app and serves it with Nginx
- Docker Compose creates a shared network so the backend can reach PostgreSQL by service name: `postgres`
- Nginx proxies frontend `/api` requests to the backend service
- The backend uses the `docker` Spring profile when running in Docker Compose

Start the Docker environment:

```powershell
docker compose up --build
```

Run it in the background:

```powershell
docker compose up --build -d
```

Stop the containers:

```powershell
docker compose down
```

Stop containers and delete the PostgreSQL volume:

```powershell
docker compose down -v
```

Check running containers:

```powershell
docker ps
```

Open the Dockerized frontend:

```text
http://localhost:5173
```

## Frontend

The frontend is a Vue 3 app generated with Vite.

It calls the Spring Boot API through Axios. By default, it expects the backend at:

```text
/api
```

During local development, Vite proxies `/api` requests to `http://localhost:8080`.
In Docker, Nginx proxies `/api` requests to the backend container.

Install dependencies:

```powershell
cd frontend
npm install
```

Run the frontend dev server:

```powershell
npm run dev
```

Open:

```text
http://localhost:5173
```

Build the frontend:

```powershell
npm run build
```

## CI

GitHub Actions runs checks on every push and pull request to `main`.

The CI workflow verifies:

- backend tests with Maven
- frontend production build with Vite
- Docker Compose configuration syntax

Workflow file:

```text
.github/workflows/ci.yml
```
