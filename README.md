# Requisition Backend

Spring Boot backend for the requisition/claims management system.

## Tech Stack

- **Java 17** / **Spring Boot 3.0.5**
- **SQL Server** (Azure SQL)
- **Docker** (multi-stage build)
- **Azure Container Apps** (scale-to-zero, consumption plan)
- **Azure Container Registry** (Basic SKU)
- **Azure Key Vault** (secrets management)
- **GitHub Actions** (CI/CD)

## Deployment

This app is deployed to **Azure Container Apps** with automatic deployments via GitHub Actions.

See [AZURE_DEPLOY_GUIDE.md](AZURE_DEPLOY_GUIDE.md) for full setup instructions.
