# 🏗️ **Lab-1: Security & Compliance in GCP**

## 🎯 **Objective**
In this lab, you will learn how to apply **least privilege access control**, **encrypt data**, **ensure data privacy**, and **comply with regional regulations** in **Google Cloud Platform (GCP)**. 

By the end of this lab, you will have:
- Configured **IAM roles** for least privilege access.
- Encrypted data **at rest** with **Cloud KMS** and **in transit** with **TLS encryption**.
- Used **Cloud Data Loss Prevention (DLP)** to detect and redact sensitive data.
- Set up **VPC Service Controls** to restrict access across **regions** and comply with **GDPR, HIPAA, and PCI-DSS**.

---

## 🛠️ **Lab Prerequisites**
Before starting, ensure you have:
1. **Google Cloud SDK** installed → [Install Guide](https://cloud.google.com/sdk/docs/install)
2. **Terraform** installed → [Download Here](https://developer.hashicorp.com/terraform/downloads)
3. A **GCP Project** with billing enabled
4. A user account with:
   - `roles/iam.admin`
   - `roles/cloudkms.admin`
   - `roles/dlp.admin`
   - `roles/orgpolicy.policyAdmin`
   - `roles/accesscontextmanager.policyAdmin`

---

## 🏗️ **Step 1: Configure Least Privilege Access Control**

### 🔹 **Create a Custom IAM Role**
Instead of giving broad **Owner** or **Editor** roles, create a **least privilege** custom role.

#### 📝 **Steps:**
1. Open Cloud Shell or run locally:
   ```sh
   gcloud iam roles create least_privilege_role \
       --project=$(gcloud config get-value project) \
       --title="Least Privilege Role" \
       --description="Role with only necessary permissions" \
       --permissions="storage.objects.list,storage.objects.get"
   ```
2. Assign this role to a user:
   ```sh
   gcloud projects add-iam-policy-binding $(gcloud config get-value project) \
       --member=user:developer@example.com \
       --role=projects/$(gcloud config get-value project)/roles/least_privilege_role
   ```
   
✅ **Result:** The user **developer@example.com** can only list and get objects from Cloud Storage.

---

## 🔐 **Step 2: Encrypt Data at Rest & In Transit**

### 🔹 **Create a Cloud KMS Key for Encryption**
1. Enable the **Cloud KMS API**:
   ```sh
   gcloud services enable cloudkms.googleapis.com
   ```
2. Create a **key ring**:
   ```sh
   gcloud kms keyrings create my-keyring --location=global
   ```
3. Create a **crypto key**:
   ```sh
   gcloud kms keys create my-encryption-key \
       --location=global \
       --keyring=my-keyring \
       --purpose=encryption
   ```

### 🔹 **Encrypt a File with Cloud KMS**
1. Create a sample file:
   ```sh
   echo "Sensitive Data Example" > sensitive.txt
   ```
2. Encrypt it using **Cloud KMS**:
   ```sh
   gcloud kms encrypt \
       --location=global \
       --keyring=my-keyring \
       --key=my-encryption-key \
       --plaintext-file=sensitive.txt \
       --ciphertext-file=sensitive.txt.encrypted
   ```

✅ **Result:** The file **sensitive.txt** is now encrypted.

---

## 🛡️ **Step 3: Detect & Redact Sensitive Data using Cloud DLP**

### 🔹 **Enable Cloud DLP API**
```sh
gcloud services enable dlp.googleapis.com
```

### 🔹 **Scan for Sensitive Data**
1. Create a JSON file `dlp-job.json`:
   ```json
   {
     "inspectConfig": {
       "infoTypes": [{"name": "EMAIL_ADDRESS"}, {"name": "CREDIT_CARD_NUMBER"}],
       "minLikelihood": "POSSIBLE"
     },
     "storageConfig": {
       "cloudStorageOptions": {
         "fileSet": {
           "url": "gs://your-bucket-name/*"
         }
       }
     }
   }
   ```
2. Run the DLP job:
   ```sh
   gcloud dlp jobs create --project=$(gcloud config get-value project) \
       --display-name="DLP Scan" \
       --source=cloud-storage \
       --storage-config-file=dlp-job.json
   ```

✅ **Result:** Cloud DLP scans the bucket and detects **email addresses** or **credit card numbers**.

---

## 🌍 **Step 4: Implement VPC Service Controls for Compliance**

### 🔹 **Create an Access Policy**
1. Get the organization ID:
   ```sh
   gcloud organizations list
   ```
2. Create the **Access Policy**:
   ```sh
   gcloud access-context-manager policies create \
       --organization=ORG_ID \
       --title="SecurePolicy"
   ```

### 🔹 **Restrict Cloud Storage API to Certain Regions**
1. Get the policy ID:
   ```sh
   gcloud access-context-manager policies list
   ```
2. Create a JSON file `storage-restrict.json`:
   ```json
   {
     "name": "restricted-storage",
     "title": "Restrict Storage to US",
     "conditions": [
       {
         "regions": ["US"]
       }
     ]
   }
   ```
3. Apply the policy:
   ```sh
   gcloud access-context-manager policies update $(POLICY_ID) \
       --set-restricted-services=storage.googleapis.com
   ```

✅ **Result:** Storage access is **restricted to the US region**, helping with **GDPR** and **HIPAA** compliance.

---

## 📌 **Lab Cleanup**
To avoid unnecessary charges, **delete the resources** after the lab:

```sh
gcloud iam roles delete least_privilege_role --project=$(gcloud config get-value project)
gcloud kms keys delete my-encryption-key --location=global --keyring=my-keyring
gcloud kms keyrings delete my-keyring --location=global
gcloud access-context-manager policies delete $(POLICY_ID)
gcloud services disable dlp.googleapis.com cloudkms.googleapis.com
```

---

## 🎯 **Key Takeaways**
✅ Applied **Least Privilege Access Control** with custom IAM roles  
✅ Encrypted data **at rest** with **Cloud KMS** and **in transit** with **TLS**  
✅ Used **Cloud DLP** to detect and redact sensitive data  
✅ Implemented **VPC Service Controls** for **compliance with GDPR, HIPAA, and PCI-DSS**  

🚀 **Next Lab:** [Lab-2: Data Processing & Reliability](./Lab_2/Lab-2-Reliability & Data Integrity in GCP.md)  
``` 
