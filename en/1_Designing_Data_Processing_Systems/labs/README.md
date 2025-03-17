# 🏗️ GCP Sandbox Environment Setup with Terraform

This repository contains Terraform configurations to create **sandbox environments** for multiple developers on **Google Cloud Platform (GCP)**. Each developer will get:
- A **dedicated GCP project**
- A **Compute Engine VM** with SSH access
- Configured **IAM permissions**
- Secure password storage in **GCP Secret Manager**
- Automated **teardown script** for cleanup

---

## 🚀 Prerequisites

Before running the Terraform scripts, ensure you have:

1. **Google Cloud SDK** installed → [Install Guide](https://cloud.google.com/sdk/docs/install)
2. **Terraform** installed → [Download Here](https://developer.hashicorp.com/terraform/downloads)
3. **GCP Billing Account** → Required for project creation
4. A **GCP Service Account** with:
   - `roles/resourcemanager.projectCreator`
   - `roles/iam.admin`
   - `roles/compute.admin`
   - `roles/secretmanager.admin`
5. A `developers.json` file with the list of developer emails and passwords:

```json
[
    {
        "email": "developer1@example.com",
        "password": "SecurePassword123"
    },
    {
        "email": "developer2@example.com",
        "password": "AnotherSecurePass456"
    }
]
```

---

## ✅ Deploying the Sandbox Environments

### 1️⃣ Authenticate with Google Cloud

Run the following command to authenticate Terraform with GCP:

```sh
gcloud auth application-default login
```

### 2️⃣ Initialize Terraform

Navigate to the Terraform project directory and initialize Terraform:

```sh
terraform init
```

### 3️⃣ Apply Terraform Configuration (Deployment)

Run the following command to create sandbox environments:

```sh
terraform apply -auto-approve
```

### 4️⃣ View Sandbox Details

After Terraform completes execution, retrieve the list of created sandbox environments:

```sh
terraform output developer_sandboxes
```

### 5️⃣ SSH Access to Compute VMs

Each developer can SSH into their assigned sandbox using:

```sh
ssh developer1@example.com@EXTERNAL_IP
```

Replace `developer1@example.com` with the correct email and `EXTERNAL_IP` with the assigned IP address.

---

## 🛠️ Explanation of Terraform Steps (`main.tf`)

The Terraform script follows these steps:

### 1️⃣ **Read Developer List**
- Terraform reads `developers.json` to get the list of developers who need sandbox environments.

### 2️⃣ **Create a Project per Developer**
- Each developer gets a separate **GCP Project**.
- The project is assigned to a **billing account**.

### 3️⃣ **Enable Required APIs**
- Terraform enables APIs for:
  - `compute.googleapis.com` (Compute Engine)
  - `iam.googleapis.com` (Identity and Access Management)
  - `secretmanager.googleapis.com` (Secret Manager)

### 4️⃣ **Set Up IAM Roles for Developers**
- Each developer gets IAM permissions in their **sandbox project**.
- Assigned roles include:
  - `roles/editor` (General management)
  - `roles/compute.instanceAdmin` (VM management)
  - `roles/iam.serviceAccountUser` (For authentication)

### 5️⃣ **Store Developer Passwords in Secret Manager**
- The `password` field from `developers.json` is securely stored in **GCP Secret Manager**.

### 6️⃣ **Create Compute Engine VM per Developer**
- A **GCP VM instance** is created for each developer.
- The VM is configured with:
  - Ubuntu OS
  - SSH access using developer email
  - Firewall rules to allow SSH

---

## ❌ Destroying the Sandbox Environments

### 1️⃣ Initialize Terraform for Cleanup

```sh
terraform init
```

### 2️⃣ Destroy Terraform Resources

Run the following command to delete the created projects:

```sh
terraform apply -auto-approve -var="destroy=true"
```

This will:
- Delete **GCP projects**
- Remove **Compute Engine VMs**
- Revoke **IAM permissions**
- Clear **secrets from Secret Manager**

---

## 🛠️ Explanation of Terraform Steps (`destroy.tf`)

The **destroy script** follows these steps:

### 1️⃣ **Identify Active Developer Projects**
- Terraform checks for all projects created under the organization for **sandbox environments**.

### 2️⃣ **Revoke IAM Permissions**
- IAM roles assigned to developers are **revoked**.

### 3️⃣ **Delete Compute Engine VMs**
- All sandbox **VM instances** are deleted.

### 4️⃣ **Delete Secret Manager Entries**
- Any stored passwords in **Secret Manager** are erased.

### 5️⃣ **Remove the GCP Projects**
- Finally, the **GCP projects** created for each developer are deleted.

---

## 📘 References

- [Terraform Documentation](https://developer.hashicorp.com/terraform/docs)
- [GCP Compute Engine](https://cloud.google.com/compute)
- [GCP IAM Roles](https://cloud.google.com/iam/docs/roles-overview)
- [GCP Secret Manager](https://cloud.google.com/secret-manager)

---
