# 🚀 Azure Container Apps – CI/CD Setup Guide

> A step-by-step guide to deploy any Spring Boot project to Azure Container Apps with GitHub Actions CI/CD.

---

## Prerequisites

- [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli) installed
- [GitHub CLI](https://cli.github.com/) installed and authenticated (`gh auth login`)
- An active Azure subscription (`az login`)

---

## 1. Set Your Variables

Edit these once per project:

```bash
# ── Project-specific values ──
PROJECT_NAME="my-app"                  # lowercase, no spaces
RESOURCE_GROUP="${PROJECT_NAME}-rg"
ACR_NAME="${PROJECT_NAME}acr"          # must be globally unique, alphanumeric only
CONTAINER_APP_NAME="${PROJECT_NAME}"
CONTAINER_APP_ENV="${PROJECT_NAME}-env"
LOCATION="eastus"
GITHUB_ORG="Cloud-Ledger"             # your GitHub org or username
GITHUB_REPO="${PROJECT_NAME}"
```

---

## 2. Create Azure Resources

```bash
# Login
az login

# Register providers (only needed once per subscription)
az provider register --namespace Microsoft.ContainerRegistry --wait
az provider register --namespace Microsoft.App --wait
az provider register --namespace Microsoft.OperationalInsights --wait

# Create resource group
az group create --name $RESOURCE_GROUP --location $LOCATION

# Create container registry
az acr create --resource-group $RESOURCE_GROUP --name $ACR_NAME --sku Basic --admin-enabled true

# Create container apps environment
az containerapp env create --name $CONTAINER_APP_ENV --resource-group $RESOURCE_GROUP --location $LOCATION
```

---

## 3. Create Service Principal + OIDC Federation

```bash
# Get subscription ID
SUB_ID=$(az account show --query id -o tsv)

# Create service principal
az ad sp create-for-rbac \
  --name "github-${PROJECT_NAME}" \
  --role contributor \
  --scopes /subscriptions/$SUB_ID/resourceGroups/$RESOURCE_GROUP \
  --sdk-auth > /tmp/sp-creds.json

# Extract client ID
CLIENT_ID=$(cat /tmp/sp-creds.json | grep clientId | cut -d '"' -f4)
TENANT_ID=$(cat /tmp/sp-creds.json | grep tenantId | head -1 | cut -d '"' -f4)

# Grant ACR push permission
ACR_ID=$(az acr show --name $ACR_NAME --query id -o tsv)
az role assignment create --assignee $CLIENT_ID --role AcrPush --scope $ACR_ID

# Create OIDC federation for GitHub Actions
az ad app federated-credential create --id $CLIENT_ID --parameters "{
  \"name\": \"github-actions-main\",
  \"issuer\": \"https://token.actions.githubusercontent.com\",
  \"subject\": \"repo:${GITHUB_ORG}/${GITHUB_REPO}:ref:refs/heads/main\",
  \"audiences\": [\"api://AzureADTokenExchange\"]
}"

# Print values you'll need
echo ""
echo "=== Save these values ==="
echo "CLIENT_ID:       $CLIENT_ID"
echo "TENANT_ID:       $TENANT_ID"
echo "SUBSCRIPTION_ID: $SUB_ID"

# Cleanup
rm /tmp/sp-creds.json
```

---

## 4. Create GitHub Repo & Push

```bash
cd /path/to/your/project

git init
git add .
git commit -m "Initial commit"

gh repo create ${GITHUB_ORG}/${GITHUB_REPO} --public --source=. --remote=origin --push
```

---

## 5. Add GitHub Secrets

```bash
REPO="${GITHUB_ORG}/${GITHUB_REPO}"

# Azure identity (from step 3)
gh secret set AZURE_CLIENT_ID       --repo $REPO --body "$CLIENT_ID"
gh secret set AZURE_TENANT_ID       --repo $REPO --body "$TENANT_ID"
gh secret set AZURE_SUBSCRIPTION_ID --repo $REPO --body "$SUB_ID"

# App secrets (edit these per project)
gh secret set DB_URL       --repo $REPO --body 'your-jdbc-connection-string'
gh secret set DB_USERNAME  --repo $REPO --body 'your-db-username'
gh secret set DB_PASSWORD  --repo $REPO --body 'your-db-password'
gh secret set JWT_SECRET_KEY --repo $REPO --body 'your-jwt-secret'
```

---

## 6. Required Project Files

### `.github/workflows/deploy-azure.yml`

```yaml
name: Build & Deploy to Azure Container Apps

on:
  push:
    branches: [main]
  workflow_dispatch:

env:
  ACR_NAME: myappacr                    # ← change per project
  IMAGE_NAME: my-app                    # ← change per project
  RESOURCE_GROUP: my-app-rg             # ← change per project
  CONTAINER_APP_NAME: my-app            # ← change per project
  CONTAINER_APP_ENV: my-app-env         # ← change per project

permissions:
  id-token: write
  contents: read

jobs:
  build:
    name: 🏗️ Build & Push
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Login to Azure
        uses: azure/login@v2
        with:
          client-id: ${{ secrets.AZURE_CLIENT_ID }}
          tenant-id: ${{ secrets.AZURE_TENANT_ID }}
          subscription-id: ${{ secrets.AZURE_SUBSCRIPTION_ID }}

      - name: Login to ACR
        run: az acr login --name ${{ env.ACR_NAME }}

      - name: Set tag
        id: tag
        run: echo "TAG=$(echo $GITHUB_SHA | head -c 7)" >> "$GITHUB_OUTPUT"

      - name: Build & push image
        run: |
          docker build \
            -t ${{ env.ACR_NAME }}.azurecr.io/${{ env.IMAGE_NAME }}:${{ steps.tag.outputs.TAG }} \
            -t ${{ env.ACR_NAME }}.azurecr.io/${{ env.IMAGE_NAME }}:latest .
          docker push ${{ env.ACR_NAME }}.azurecr.io/${{ env.IMAGE_NAME }}:${{ steps.tag.outputs.TAG }}
          docker push ${{ env.ACR_NAME }}.azurecr.io/${{ env.IMAGE_NAME }}:latest
    outputs:
      image_tag: ${{ steps.tag.outputs.TAG }}

  deploy:
    name: 🚀 Deploy
    needs: build
    runs-on: ubuntu-latest
    steps:
      - name: Login to Azure
        uses: azure/login@v2
        with:
          client-id: ${{ secrets.AZURE_CLIENT_ID }}
          tenant-id: ${{ secrets.AZURE_TENANT_ID }}
          subscription-id: ${{ secrets.AZURE_SUBSCRIPTION_ID }}

      - name: Deploy Container App
        run: |
          az containerapp create \
            --name ${{ env.CONTAINER_APP_NAME }} \
            --resource-group ${{ env.RESOURCE_GROUP }} \
            --environment ${{ env.CONTAINER_APP_ENV }} \
            --image ${{ env.ACR_NAME }}.azurecr.io/${{ env.IMAGE_NAME }}:${{ needs.build.outputs.image_tag }} \
            --registry-server ${{ env.ACR_NAME }}.azurecr.io \
            --target-port 8080 \
            --ingress external \
            --env-vars \
              SPRING_PROFILES_ACTIVE=prod \
              SPRING_DATASOURCE_URL="${{ secrets.DB_URL }}" \
              SPRING_DATASOURCE_USERNAME="${{ secrets.DB_USERNAME }}" \
              SPRING_DATASOURCE_PASSWORD="${{ secrets.DB_PASSWORD }}" \
              JWT_SECRET_KEY="${{ secrets.JWT_SECRET_KEY }}" \
              SERVER_PORT=8080 \
            --min-replicas 0 --max-replicas 1 || \
          az containerapp update \
            --name ${{ env.CONTAINER_APP_NAME }} \
            --resource-group ${{ env.RESOURCE_GROUP }} \
            --image ${{ env.ACR_NAME }}.azurecr.io/${{ env.IMAGE_NAME }}:${{ needs.build.outputs.image_tag }} \
            --set-env-vars \
              SPRING_PROFILES_ACTIVE=prod \
              SPRING_DATASOURCE_URL="${{ secrets.DB_URL }}" \
              SPRING_DATASOURCE_USERNAME="${{ secrets.DB_USERNAME }}" \
              SPRING_DATASOURCE_PASSWORD="${{ secrets.DB_PASSWORD }}" \
              JWT_SECRET_KEY="${{ secrets.JWT_SECRET_KEY }}" \
              SERVER_PORT=8080
```

### `Dockerfile` (multi-stage Spring Boot)

```dockerfile
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### `.dockerignore`

```
.git
.gitignore
target/
*.md
LICENSE
.idea/
*.iml
attachments/
```

### `application.properties`

```properties
spring.profiles.active=${SPRING_PROFILES_ACTIVE:local}
```

### `application-prod.yml` (template)

```yaml
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL}
    username: ${SPRING_DATASOURCE_USERNAME}
    password: ${SPRING_DATASOURCE_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false

server:
  port: ${SERVER_PORT:8080}

application:
  security:
    jwt:
      secret-key: ${JWT_SECRET_KEY}
```

### `.gitignore` (essentials)

```
target/
.idea/
*.iml
.DS_Store
.env
attachments/
*.class
*.jar
```

---

## 7. Verify Deployment

```bash
# Get app URL
az containerapp show \
  --name $CONTAINER_APP_NAME \
  --resource-group $RESOURCE_GROUP \
  --query "properties.configuration.ingress.fqdn" -o tsv

# Test
curl https://<your-app-url>/actuator/health
# Swagger: https://<your-app-url>/swagger-ui/index.html
```

---

## 8. Tear Down (when no longer needed)

```bash
# Delete everything in the resource group
az group delete --name $RESOURCE_GROUP --yes --no-wait

# Delete the service principal
az ad app delete --id $CLIENT_ID
```

---

## Quick Reference – What Changes Per Project

| Item | What to change |
|------|---------------|
| Variables in Step 1 | `PROJECT_NAME`, `GITHUB_ORG`, `GITHUB_REPO` |
| Workflow `env:` block | `ACR_NAME`, `IMAGE_NAME`, `RESOURCE_GROUP`, `CONTAINER_APP_NAME`, `CONTAINER_APP_ENV` |
| GitHub Secrets | `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET_KEY` |
| `Dockerfile` | Update base image if not Java 17 |
| `application-prod.yml` | Add/remove env vars for your app |

