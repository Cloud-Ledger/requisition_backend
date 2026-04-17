# Azure Deployment Guide

This guide covers the full CI/CD pipeline for deploying the Requisition Backend to **Azure Container Apps** using **GitHub Actions** and **Azure Key Vault**.

---

## Architecture Overview

```
GitHub (release branch)
    │
    ▼
GitHub Actions Workflow
    │
    ├── Fetches secrets from Azure Key Vault
    ├── Builds Docker image
    ├── Pushes image to Azure Container Registry (ACR)
    └── Deploys to Azure Container Apps (scale-to-zero)
```

---

## Azure Resources

| Resource | Name | Details |
|---|---|---|
| Resource Group | `requisition-backend-rg` | South Africa North |
| Container Apps Environment | `requisition-env` | Consumption plan (scale-to-zero) |
| Container Registry (ACR) | `requisitionbackendc` | Basic SKU, admin enabled |
| Key Vault | `requisition-backend-kv` | Standard, stores all secrets |
| Service Principal | `requisition-backend-sp` | Contributor on resource group, Key Vault Secrets User |

---

## Azure Key Vault Secrets

All sensitive configuration is stored in Key Vault (`requisition-backend-kv`):

| Secret Name | Description |
|---|---|
| `datasource-url` | JDBC connection string for Azure SQL Server |
| `datasource-username` | Database username |
| `datasource-password` | Database password |
| `jwt-secret-key` | JWT signing key |
| `acr-username` | Azure Container Registry username |
| `acr-password` | Azure Container Registry password |

---

## GitHub Secrets

Only **one** GitHub secret is required:

| Secret | Description |
|---|---|
| `AZURE_CREDENTIALS` | Service principal JSON for Azure login |

All other secrets are fetched at deploy time from Azure Key Vault.

---

## CI/CD Pipeline

### Trigger

The workflow triggers on every push to the `release` branch. The intended flow is:

```
feature branch → main (PR/merge) → release (merge) → auto-deploy
```

### Workflow Steps

1. **Checkout** code
2. **Login to Azure** using the `AZURE_CREDENTIALS` service principal
3. **Fetch secrets** from Azure Key Vault using Azure CLI
4. **Login to ACR** using credentials from Key Vault
5. **Build & push** Docker image to ACR (tagged with commit SHA + `latest`)
6. **Deploy** to Azure Container Apps with environment variables injected from Key Vault
7. **Logout** from Azure

### Workflow File

Located at `.github/workflows/deploy-to-azure.yml`

---

## Setup From Scratch

### 1. Create Azure Resources

```bash
# Login
az login

# Create resource group
az group create --name requisition-backend-rg --location southafricanorth

# Register providers
az provider register --namespace Microsoft.App --wait
az provider register --namespace Microsoft.OperationalInsights --wait
az provider register --namespace Microsoft.ContainerRegistry --wait

# Create Container Apps environment (consumption plan, scale-to-zero)
az containerapp env create \
  --name requisition-env \
  --resource-group requisition-backend-rg \
  --location southafricanorth

# Create Container Registry (or use Azure Portal)
az acr create \
  --name requisitionbackendc \
  --resource-group requisition-backend-rg \
  --location southafricanorth \
  --sku Basic \
  --admin-enabled true

# Create Key Vault
az keyvault create \
  --name requisition-backend-kv \
  --resource-group requisition-backend-rg \
  --location southafricanorth \
  --sku standard
```

### 2. Create Service Principal

```bash
az ad sp create-for-rbac \
  --name "requisition-backend-sp" \
  --role contributor \
  --scopes /subscriptions/<SUBSCRIPTION_ID>/resourceGroups/requisition-backend-rg \
  --sdk-auth
```

Copy the JSON output — this becomes the `AZURE_CREDENTIALS` GitHub secret.

### 3. Assign Key Vault Access

In the Azure Portal:

1. Go to **Key Vault** → **Access control (IAM)**
2. Add role **Key Vault Secrets Officer** to your own account (to manage secrets)
3. Add role **Key Vault Secrets User** to the service principal `requisition-backend-sp` (for CI/CD reads)

### 4. Add Secrets to Key Vault

In the Azure Portal:

1. Go to **Key Vault** → **Objects** → **Secrets**
2. Click **+ Generate/Import** and add each secret listed in the [Key Vault Secrets](#azure-key-vault-secrets) table above

### 5. Add GitHub Secret

1. Go to your GitHub repo → **Settings** → **Secrets and variables** → **Actions**
2. Add `AZURE_CREDENTIALS` with the service principal JSON

### 6. Deploy

Merge `main` into `release` — the workflow will automatically build and deploy.

---

## Container Apps Configuration

- **Ingress**: External (publicly accessible)
- **Target port**: 8080
- **Scaling**: 0–1 replicas (scales to zero when idle = no cost)
- **Spring profile**: `prod` (set via `SPRING_PROFILES_ACTIVE` env var)

---

## Dockerfile

Multi-stage build:

1. **Build stage**: Uses `maven:3.9.6-eclipse-temurin-17-alpine` to compile the JAR
2. **Run stage**: Uses `eclipse-temurin:17-jre-alpine` for a minimal runtime image

---

## Profiles

| Profile | File | Usage |
|---|---|---|
| `local` | `application-local.yml` | Local development |
| `prod` | `application-prod.yml` | Azure deployment (SQL Server) |

The active profile is set via `SPRING_PROFILES_ACTIVE` environment variable (defaults to `local`).

---

## Costs

This setup is designed for **minimal cost**:

- **Container Apps** (Consumption): Free when scaled to zero, pay only for active usage
- **Container Registry** (Basic): ~$0.167/day (~$5/month)
- **Key Vault** (Standard): $0.03 per 10,000 operations (negligible)
- **Log Analytics Workspace**: Free tier up to 5GB/month

---

## Troubleshooting

### View container logs
```bash
az containerapp logs show \
  --name requisition-backend-app \
  --resource-group requisition-backend-rg \
  --follow
```

### Check container app status
```bash
az containerapp show \
  --name requisition-backend-app \
  --resource-group requisition-backend-rg \
  --query "properties.runningStatus"
```

### Restart container
```bash
az containerapp revision restart \
  --name requisition-backend-app \
  --resource-group requisition-backend-rg \
  --revision <revision-name>
```

### Check GitHub Actions workflow
Go to your repo → **Actions** tab → click the latest workflow run to see logs.

