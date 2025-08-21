# Wayfair Catalog – DevOps Sample

A minimal Spring Boot REST service to demo a full DevOps flow.

**Stack**
- Build: Maven
- CI/CD: Jenkins
- Container: Docker
- Registry: Docker Hub (`gattisuresh/wayfair-catalog`) + AWS ECR
- Deploy: Kubernetes (manifests)
- IaC: Terraform (ECR)
- Config Mgmt: Ansible (install Docker on a node)

**Endpoints**
- `GET /health` -> `OK`
- `GET /products` -> sample JSON

## Local run
```bash
cd app
mvn clean package
java -jar target/catalog-0.0.1-SNAPSHOT.jar
# http://localhost:8080/health
