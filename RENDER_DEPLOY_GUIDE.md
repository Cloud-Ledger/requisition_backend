# 🚀 Render Deployment Guide (100% FREE)

> Deploy this Spring Boot backend to **Render** with a free PostgreSQL database and automatic GitHub deploys.

---

## Why Render? (vs Azure)

| Feature            | Azure (previous)         | Render (new)       |
|--------------------|--------------------------|---------------------|
| Container hosting  | ~$5+/month               | **FREE**            |
| Database           | ~$5+/month (Azure SQL)   | **FREE** (PostgreSQL, 1 GB) |
| Container registry | ~$5/month (ACR)          | **FREE** (built-in or GHCR) |
| SSL/HTTPS          | Included                 | **Included**        |
| Auto-deploy on push| GitHub Actions            | **Built-in** + GitHub Actions |

---

## Prerequisites

- A free [Render account](https://render.com/) (sign up with GitHub)
- Your code pushed to GitHub

---

## Step 1: Create a Free PostgreSQL Database on Render

1. Go to [Render Dashboard](https://dashboard.render.com/)
2. Click **New** → **PostgreSQL**
3. Configure:
   - **Name**: `requisition-db`
   - **Database**: `requisition_db`
   - **User**: `requisition_user`
   - **Region**: Oregon (US West) — or closest to your users
   - **Plan**: **Free**
4. Click **Create Database**
5. Once created, note down:
   - **Internal Database URL** (for connecting from Render services)
   - **External Database URL** (for connecting from your local machine)

---

## Step 2: Create a Web Service on Render

1. Click **New** → **Web Service**
2. Connect your GitHub repo: `Cloud-Ledger/requisition_backend`
3. Configure:
   - **Name**: `requisition-backend`
   - **Region**: Same as your database
   - **Runtime**: **Docker**
   - **Plan**: **Free**
4. Add **Environment Variables**:

   | Key                        | Value                                                              |
   |----------------------------|--------------------------------------------------------------------|
   | `SPRING_PROFILES_ACTIVE`   | `prod`                                                             |
   | `SERVER_PORT`              | `8080`                                                             |
   | `SPRING_DATASOURCE_URL`    | `jdbc:postgresql://<INTERNAL_HOST>:5432/requisition_db`            |
   | `SPRING_DATASOURCE_USERNAME` | `requisition_user`                                               |
   | `SPRING_DATASOURCE_PASSWORD` | *(copy from Render DB dashboard)*                                |
   | `JWT_SECRET_KEY`           | *(generate a random 64-char hex string)*                           |

   > ⚠️ **Important**: The `SPRING_DATASOURCE_URL` must start with `jdbc:postgresql://` — take the Internal Database URL from Step 1 and replace `postgres://` → `jdbc:postgresql://`, and append the database name.

5. Click **Create Web Service**

---

## Step 3: Set Up Auto-Deploy via GitHub Actions (Optional)

Render auto-deploys on push by default. But if you want explicit control via GitHub Actions:

1. In Render Dashboard → your Web Service → **Settings** → **Deploy Hook**
2. Copy the Deploy Hook URL
3. In GitHub → your repo → **Settings** → **Secrets and variables** → **Actions**
4. Add a secret:
   - **Name**: `RENDER_DEPLOY_HOOK_URL`
   - **Value**: *(paste the deploy hook URL)*

Now every push to `main` triggers a deploy via the workflow in `.github/workflows/deploy-render.yml`.

---

## Step 4: Verify Deployment

After deploy completes (first build takes ~5-10 minutes):

```bash
# Your app URL will be:
# https://requisition-backend.onrender.com

# Test the health endpoint
curl https://requisition-backend.onrender.com/actuator/health

# Swagger UI
open https://requisition-backend.onrender.com/swagger-ui/index.html
```

---

## One-Click Deploy (Alternative)

You can also use the `render.yaml` Blueprint:

1. Go to [Render Dashboard](https://dashboard.render.com/)
2. Click **New** → **Blueprint**
3. Connect your GitHub repo
4. Render will read `render.yaml` and create all services automatically

---

## Notes

- **Free tier cold starts**: The free tier spins down after 15 minutes of inactivity. First request after sleep takes ~30-60 seconds.
- **Database expiry**: Render free PostgreSQL databases expire after 90 days. You can recreate them (data is lost) or upgrade to paid ($7/month for persistent).
- **Custom domain**: Render supports free custom domains with automatic HTTPS.

---

## Clean Up Azure Resources (Stop Charges!)

Since you've migrated away from Azure, delete everything to stop billing:

```bash
# Delete the entire resource group (removes ALL Azure resources)
az group delete --name requisition-rg --yes --no-wait

# Or delete individual resources:
az acr delete --name requisitionbackendacr --resource-group requisition-rg --yes
```

Also remove Azure-related GitHub secrets that are no longer needed:
- `AZURE_CLIENT_ID`
- `AZURE_TENANT_ID`
- `AZURE_SUBSCRIPTION_ID`
- `GHCR_PAT`

