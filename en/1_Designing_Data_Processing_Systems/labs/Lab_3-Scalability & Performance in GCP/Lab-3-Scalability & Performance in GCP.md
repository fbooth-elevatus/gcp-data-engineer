# 🏗️ **Lab-3: Scalability & Performance in GCP**

## 🎯 **Objective**
In this lab, you will learn how to optimize **scalability and performance** in Google Cloud by:
- **Optimizing Data Storage** using **BigQuery, Cloud Storage, and Spanner**.
- **Designing for Auto-Scaling** with **Dataflow's dynamic scaling** and **Dataproc autoscaling**.
- **Partitioning & Indexing Data** to improve query performance in **BigQuery** and **Cloud SQL**.

By the end of this lab, you will have a **scalable and high-performing data pipeline** that handles **large datasets efficiently**.

---

## 🛠️ **Lab Prerequisites**
Before starting, ensure you have:
1. **Google Cloud SDK** installed → [Install Guide](https://cloud.google.com/sdk/docs/install)
2. **Terraform** installed → [Download Here](https://developer.hashicorp.com/terraform/downloads)
3. A **GCP Project** with billing enabled
4. A user account with:
   - `roles/bigquery.admin`
   - `roles/storage.admin`
   - `roles/dataproc.admin`
   - `roles/spanner.admin`

---

## 🏗️ **Step 1: Optimize Data Storage with BigQuery, Cloud Storage & Spanner**

### 🔹 **Enable Required APIs**
```sh
gcloud services enable bigquery.googleapis.com storage.googleapis.com spanner.googleapis.com
```

### 🔹 **Create Optimized Data Storage**
#### **1️⃣ Structured Data - BigQuery**
1. Create a BigQuery dataset:
   ```sh
   bq mk --dataset $(gcloud config get-value project):scalability_lab
   ```
2. Load structured data:
   ```sh
   bq load --source_format=CSV \
   scalability_lab.sales_data \
   gs://cloud-samples-data/bigquery/sample-transactions/transactions.csv \
   transaction_id:STRING,customer_id:STRING,amount:FLOAT,timestamp:TIMESTAMP
   ```
✅ **Result:** **BigQuery is optimized** for structured data analytics.

#### **2️⃣ Unstructured Data - Cloud Storage**
1. Create a Cloud Storage bucket:
   ```sh
   gsutil mb -c STANDARD -l US gs://scalability-performance-lab/
   ```
2. Upload an unstructured dataset:
   ```sh
   gsutil cp sample.pdf gs://scalability-performance-lab/
   ```
✅ **Result:** **Cloud Storage is used** for scalable unstructured data storage.

#### **3️⃣ Globally Distributed Data - Spanner**
1. Create a **Spanner instance**:
   ```sh
   gcloud spanner instances create scalability-instance \
   --config=regional-us-central1 --processing-units=100
   ```
2. Create a **Spanner database**:
   ```sh
   gcloud spanner databases create salesdb --instance=scalability-instance
   ```
3. Create a **sales table**:
   ```sh
   gcloud spanner databases ddl update salesdb --instance=scalability-instance --ddl="
   CREATE TABLE Sales (
       TransactionID STRING(36) NOT NULL,
       CustomerID STRING(36),
       Amount FLOAT64,
       Timestamp TIMESTAMP
   ) PRIMARY KEY (TransactionID);
   "
   ```
✅ **Result:** **Cloud Spanner is ideal** for globally distributed, high-availability data.

---

## 🔄 **Step 2: Design for Auto-Scaling with Dataflow & Dataproc**

### 🔹 **Enable Dataflow & Dataproc APIs**
```sh
gcloud services enable dataflow.googleapis.com dataproc.googleapis.com
```

### 🔹 **Set Up Dataflow Auto-Scaling**
1. Create a Dataflow job with **auto-scaling enabled**:
   ```sh
   gcloud dataflow jobs run auto-scaling-job \
   --gcs-location gs://dataflow-templates/latest/Word_Count \
   --region us-central1 \
   --staging-location gs://scalability-performance-lab/tmp/ \
   --parameters inputFile=gs://scalability-performance-lab/sample.txt,output=gs://scalability-performance-lab/output/
   ```
✅ **Result:** **Dataflow dynamically scales** based on workload demand.

### 🔹 **Set Up Dataproc Auto-Scaling**
1. Create an autoscaling policy:
   ```sh
   gcloud dataproc autoscaling-policies create auto-scale-policy \
   --region=us-central1 \
   --max-workers=10
   ```
2. Create a Dataproc cluster with the policy:
   ```sh
   gcloud dataproc clusters create scaling-cluster \
   --region=us-central1 \
   --autoscaling-policy=auto-scale-policy
   ```
✅ **Result:** **Dataproc autoscaling ensures cost-efficient processing**.

---

## 📊 **Step 3: Partition & Index Data for Performance**

### 🔹 **Partition & Cluster BigQuery Data**
1. Create a partitioned & clustered BigQuery table:
   ```sh
   bq query --use_legacy_sql=false \
   'CREATE TABLE scalability_lab.partitioned_sales
   PARTITION BY DATE(timestamp)
   CLUSTER BY customer_id AS
   SELECT * FROM scalability_lab.sales_data;'
   ```
✅ **Result:** **Partitioning improves query performance** by reducing scanned data.

### 🔹 **Index Cloud SQL Data**
1. Enable Cloud SQL:
   ```sh
   gcloud services enable sqladmin.googleapis.com
   ```
2. Create a Cloud SQL instance:
   ```sh
   gcloud sql instances create sql-instance --tier=db-f1-micro --region=us-central1
   ```
3. Create an indexed sales table:
   ```sh
   gcloud sql databases create salesdb --instance=sql-instance
   gcloud sql users create admin --instance=sql-instance --password=securepassword
   ```
4. **Create an index for faster queries**:
   ```sh
   gcloud sql connect sql-instance --user=admin
   ```
   ```sql
   CREATE INDEX idx_customer ON Sales(CustomerID);
   ```
✅ **Result:** **Indexing speeds up query performance** in relational databases.

---

## 📌 **Lab Cleanup**
To avoid unnecessary charges, **delete the resources** after the lab:

```sh
bq rm -r -f scalability_lab
gsutil rm -r gs://scalability-performance-lab/
gcloud spanner instances delete scalability-instance
gcloud dataflow jobs cancel auto-scaling-job --region=us-central1
gcloud dataproc clusters delete scaling-cluster --region=us-central1
gcloud sql instances delete sql-instance
gcloud services disable bigquery.googleapis.com storage.googleapis.com spanner.googleapis.com dataflow.googleapis.com dataproc.googleapis.com sqladmin.googleapis.com
```

---

## 🎯 **Key Takeaways**
✅ Optimized **data storage** using **BigQuery, Cloud Storage, and Spanner**  
✅ Implemented **auto-scaling** with **Dataflow & Dataproc**  
✅ Partitioned & indexed data to **enhance query performance**  

🚀 **Next Lab:** [Lab-4: Data Processing & Orchestration](../Lab_4-Cost%20Optimization%20in%20GCP/Lab-4-Cost%20Optimization%20in%20GCP.md)  
