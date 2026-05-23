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
