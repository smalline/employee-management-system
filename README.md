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

## Container Registry

GitHub Actions also builds and publishes Docker images to GitHub Container Registry on every push to `main`.

Workflow file:

```text
.github/workflows/docker-images.yml
```

Published image names:

```text
ghcr.io/smalline/employee-management-system-backend
ghcr.io/smalline/employee-management-system-frontend
```

Each image gets two tags:

- `latest`
- the Git commit SHA

The commit SHA tag is useful for deployments because it points to one exact version of the application.

## Kubernetes

Kubernetes is used to run the same app as Docker Compose, but with deployment-style infrastructure.

In this setup:

- `Namespace` keeps this project's Kubernetes resources grouped together
- `Secret` stores PostgreSQL username, password, and database name
- `PersistentVolumeClaim` requests storage for PostgreSQL data
- `Deployment` runs containers and restarts them if they fail
- `Service` gives containers stable network names inside the cluster
- `NodePort` exposes the frontend at `http://localhost:30080`

Before running Kubernetes locally, enable it in Docker Desktop:

```text
Docker Desktop -> Settings -> Kubernetes -> Enable Kubernetes -> Apply & restart
```

Check that Kubernetes is available:

```powershell
kubectl config get-contexts
kubectl config use-context docker-desktop
kubectl cluster-info
```

Build the local Docker images:

```powershell
docker compose build backend frontend
```

Apply the Kubernetes manifests:

```powershell
kubectl apply -f k8s
```

Check the deployment:

```powershell
kubectl get pods -n employee-management
kubectl get services -n employee-management
```

Open the app:

```text
http://localhost:30080
```

If `localhost:30080` is not reachable directly, forward the Kubernetes frontend service to your machine:

```powershell
kubectl port-forward service/frontend 30080:80 -n employee-management
```

Test the API through the Kubernetes frontend service:

```powershell
Invoke-RestMethod http://localhost:30080/api/status
```

Delete the local Kubernetes environment:

```powershell
kubectl delete namespace employee-management
```

## Azure AKS

Azure Kubernetes Service, or AKS, is Azure's managed Kubernetes service.

For local Kubernetes, the cluster can use images built on your laptop. For AKS, the cluster must pull images from a container registry. This project uses GitHub Container Registry:

```text
ghcr.io/smalline/employee-management-system-backend
ghcr.io/smalline/employee-management-system-frontend
```

Install the Azure CLI, then sign in:

```powershell
az login
```

Create an Azure resource group:

```powershell
az group create --name ems-rg --location eastus
```

Create an AKS cluster:

```powershell
az aks create `
  --resource-group ems-rg `
  --name ems-aks `
  --node-count 1 `
  --node-vm-size Standard_DC2s_v3 `
  --generate-ssh-keys
```

If Azure says the subscription is not registered for AKS, register the provider:

```powershell
az provider register --namespace Microsoft.ContainerService --wait
```

Connect `kubectl` to AKS:

```powershell
az aks get-credentials --resource-group ems-rg --name ems-aks
kubectl config current-context
```

Deploy the AKS manifest:

```powershell
kubectl apply -f k8s/aks/aks-deployment.yaml
```

Check the app:

```powershell
kubectl get pods -n employee-management
kubectl get service frontend -n employee-management
```

The frontend service uses `LoadBalancer` in AKS, so Azure will create a public external IP address.

Test the app with the external IP:

```powershell
Invoke-RestMethod http://<external-ip>/api/status
```

Clean up Azure resources when finished to avoid cloud charges:

```powershell
az group delete --name ems-rg --yes
```
