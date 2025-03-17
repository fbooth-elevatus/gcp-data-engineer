# 🏗️ **Lab-2: Reliability & Data Integrity in GCP**

## 🎯 **Objective**
In this lab, you will learn how to ensure **data reliability and integrity** in Google Cloud by:
- Implementing **data validation** with **Cloud Dataprep** and **BigQuery Data Quality**.
- Ensuring **fault tolerance** using **Dataflow checkpointing** and **multi-region storage**.
- Enabling **monitoring and alerts** with **Cloud Logging, Cloud Monitoring, and Stackdriver**.

By the end of this lab, you will have a **highly reliable and monitored data pipeline** for robust cloud-based data processing.

---

## 🛠️ **Lab Prerequisites**
Before starting, ensure you have:
1. **Google Cloud SDK** installed → [Install Guide](https://cloud.google.com/sdk/docs/install)
2. **Terraform** installed → [Download Here](https://developer.hashicorp.com/terraform/downloads)
3. A **GCP Project** with billing enabled
4. A user account with:
   - `roles/bigquery.admin`
   - `roles/dataflow.admin`
   - `roles/monitoring.admin`
   - `roles/logging.admin`

---

## 🏗️ **Step 1: Implement Data Validation using Cloud Dataprep and BigQuery**

### 🔹 **Enable Required APIs**
```sh
gcloud services enable dataprep.googleapis.com bigquery.googleapis.com
```

### 🔹 **Create a Sample BigQuery Dataset**
1. Open Cloud Shell and create a dataset:
   ```sh
   bq mk --dataset $(gcloud config get-value project):data_validation_lab
   ```
2. Create a sample table:
   ```sh
   bq query --use_legacy_sql=false \
   'CREATE TABLE data_validation_lab.customer_data (
       id INT64,
       name STRING,
       email STRING,
       age INT64,
       signup_date TIMESTAMP
   );'
   ```
3. Insert sample dirty data:
   ```sh
   bq query --use_legacy_sql=false \
   'INSERT INTO data_validation_lab.customer_data (id, name, email, age, signup_date) VALUES
   (1, "Alice", "alice@example.com", 25, "2025-03-11 12:00:00"),
   (2, "Bob", "invalid_email", NULL, "2025-03-11 12:10:00"),
   (3, "Charlie", "charlie@example.com", 200, NULL);'
   ```

### 🔹 **Validate Data using BigQuery Data Quality**
1. Run a **data validation query**:
   ```sh
   bq query --use_legacy_sql=false \
   'SELECT id, name, email, age, signup_date,
   CASE 
     WHEN email NOT LIKE "%@%" THEN "Invalid Email"
     ELSE "Valid Email"
   END AS email_status,
   CASE 
     WHEN age IS NULL OR age < 0 OR age > 120 THEN "Invalid Age"
     ELSE "Valid Age"
   END AS age_status,
   CASE 
     WHEN signup_date IS NULL THEN "Missing Date"
     ELSE "Valid Date"
   END AS date_status
   FROM data_validation_lab.customer_data;'
   ```
✅ **Result:** Data validation detects **invalid emails, ages, and missing signup dates**.

---

## 🔄 **Step 2: Ensure Fault Tolerance with Dataflow Checkpointing & Multi-Region Storage**

### 🔹 **Enable Dataflow API**
```sh
gcloud services enable dataflow.googleapis.com
```

### 🔹 **Set Up a Multi-Region Cloud Storage Bucket**
1. Create a multi-region bucket:
   ```sh
   gsutil mb -c STANDARD -l US gs://dataflow-fault-tolerance-lab/
   ```
2. Upload a sample dataset:
   ```sh
   echo "1,Alice,alice@example.com,25" > sample.csv
   gsutil cp sample.csv gs://dataflow-fault-tolerance-lab/
   ```

### 🔹 **Run a Dataflow Pipeline with Checkpointing**
1. Deploy a simple Apache Beam pipeline:
   ```sh
   gcloud dataflow jobs run fault-tolerance-lab \
   --gcs-location gs://dataflow-templates/latest/Word_Count \
   --region us-central1 \
   --staging-location gs://dataflow-fault-tolerance-lab/tmp/ \
   --parameters inputFile=gs://dataflow-fault-tolerance-lab/sample.csv
   ```
2. **Enable Checkpointing** by modifying pipeline parameters:
   ```sh
   --parameters checkpointLocation=gs://dataflow-fault-tolerance-lab/checkpoints/
   ```

✅ **Result:** The Dataflow job will **auto-recover from failures using checkpoints**.

---

## 📊 **Step 3: Enable Monitoring & Alerts with Cloud Logging & Stackdriver**

### 🔹 **Enable Monitoring API**
```sh
gcloud services enable monitoring.googleapis.com logging.googleapis.com
```

### 🔹 **Create a Cloud Monitoring Alert**
1. Create an alerting policy for **Dataflow Job Failures**:
   ```sh
   gcloud alpha monitoring policies create \
       --display-name="Dataflow Job Failure Alert" \
       --conditions="metric.type=\"dataflow.googleapis.com/job/current_status\" AND resource.type=\"dataflow_job\"" \
       --notification-channels=email:alerts@example.com
   ```
2. **View logs in Cloud Logging**:
   ```sh
   gcloud logging read "resource.type=dataflow_job severity>=ERROR" --limit 5
   ```

✅ **Result:** Cloud Logging **captures errors**, and **alerts notify teams** of failures.

---

## 📌 **Lab Cleanup**
To avoid unnecessary charges, **delete the resources** after the lab:

```sh
bq rm -r -f data_validation_lab
gsutil rm -r gs://dataflow-fault-tolerance-lab/
gcloud dataflow jobs cancel fault-tolerance-lab --region=us-central1
gcloud monitoring policies delete $(gcloud monitoring policies list --filter="displayName=Dataflow Job Failure Alert" --format="value(name)")
gcloud services disable dataprep.googleapis.com bigquery.googleapis.com dataflow.googleapis.com monitoring.googleapis.com logging.googleapis.com
```

---

## 🎯 **Key Takeaways**
✅ Used **BigQuery Data Quality** to validate and clean data  
✅ Designed for **fault tolerance** using **Dataflow checkpointing** & **multi-region storage**  
✅ Enabled **monitoring & alerts** using **Cloud Logging, Monitoring, and Stackdriver**  

🚀 **Next Lab:** [Lab-3: Data Processing & Orchestration](../Lab_3-Scalability & Performance in GCP/Lab-3-Scalability & Performance in GCP.md)  
