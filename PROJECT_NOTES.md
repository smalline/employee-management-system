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

1. Commit/push GitHub Actions CI milestone.
2. Add Kubernetes manifests.
3. Deploy to AWS or Azure.
4. Add Datadog monitoring.

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

## GitHub Actions CI Milestone

Added:

```text
.github/workflows/ci.yml
```

The workflow runs on pushes and pull requests to `main`.

Jobs:

- Backend Tests: uses Java 21 and runs `./mvnw test`
- Frontend Build: uses Node 24, runs `npm ci`, then `npm run build`
- Docker Compose Config: runs `docker compose config --quiet`

Interview wording:

```text
I added a GitHub Actions CI pipeline that validates backend tests, frontend builds, and Docker Compose configuration on every push and pull request. This gives fast feedback before changes are merged and proves the project works in a clean environment, not just on my machine.
```

## Kubernetes Milestone

Added Kubernetes manifests under:

```text
k8s/
```

Resources added:

- `Namespace` named `employee-management`
- PostgreSQL `Secret`
- PostgreSQL `PersistentVolumeClaim`
- PostgreSQL `Deployment`
- PostgreSQL `Service`
- Backend `ConfigMap`
- Backend `Deployment`
- Backend `Service`
- Frontend `Deployment`
- Frontend `Service` exposed with NodePort `30080`

Important learning notes:

- A `Deployment` describes how Kubernetes should run and restart application containers.
- A `Service` gives pods a stable network name because pod IP addresses can change.
- A `Secret` stores sensitive config separately from application code.
- A `PersistentVolumeClaim` requests disk storage so database data can survive pod restarts.
- The backend uses an init container to wait until PostgreSQL is reachable before Spring Boot starts.
- The frontend can call `/api` because Nginx proxies API traffic to the backend service name inside Kubernetes.

Interview wording:

```text
I created Kubernetes manifests for the full stack: PostgreSQL, Spring Boot, and Vue/Nginx. I used Deployments for running containers, Services for stable networking, a Secret for database credentials, and a PersistentVolumeClaim for database storage. The frontend is exposed locally with a NodePort, while backend and database services remain internal to the cluster.
```

Local Kubernetes was not enabled yet when these files were created.

Enable it in Docker Desktop:

```text
Docker Desktop -> Settings -> Kubernetes -> Enable Kubernetes -> Apply & restart
```

Then verify:

```powershell
kubectl config get-contexts
kubectl config use-context docker-desktop
kubectl cluster-info
```

Deploy:

```powershell
docker compose build backend frontend
kubectl apply -f k8s
kubectl get pods -n employee-management
kubectl get services -n employee-management
```

Verified local deployment:

```text
backend: Running
frontend: Running
postgres: Running
frontend service: NodePort 30080
GET http://localhost:30080/api/status: UP
```

On this local Docker Desktop Kubernetes setup, direct NodePort access was not reachable from Windows, so `kubectl port-forward` was used:

```powershell
kubectl port-forward service/frontend 30080:80 -n employee-management
```

## Container Registry Milestone

Added:

```text
.github/workflows/docker-images.yml
```

Purpose:

- Build backend Docker image in GitHub Actions
- Build frontend Docker image in GitHub Actions
- Push both images to GitHub Container Registry
- Tag images with both `latest` and the Git commit SHA

Published image names:

```text
ghcr.io/smalline/employee-management-system-backend
ghcr.io/smalline/employee-management-system-frontend
```

Interview wording:

```text
I added a GitHub Actions workflow that builds and publishes Docker images to GitHub Container Registry. This is important because Kubernetes clusters in cloud environments cannot use images that only exist on my laptop. Publishing images to a registry gives the cluster a stable place to pull versioned application images from.
```

## Azure AKS Milestone

Decision:

```text
Use Azure AKS for the cloud Kubernetes milestone.
```

Current local status:

```text
Azure CLI is not installed yet. The `az` command was not recognized.
```

Added:

```text
k8s/aks/aks-deployment.yaml
```

Purpose:

- Provide an AKS-ready Kubernetes manifest.
- Use GHCR image names instead of local Docker image names.
- Set backend and frontend image pull policy to `Always`.
- Change the frontend Service from `NodePort` to `LoadBalancer` for Azure.

Interview wording:

```text
For cloud deployment, I prepared an AKS-specific Kubernetes manifest. The local manifests use locally built Docker images and NodePort, while the AKS manifest uses GitHub Container Registry images and exposes the frontend through an Azure LoadBalancer.
```
