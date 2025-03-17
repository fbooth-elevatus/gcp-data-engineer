# 🏗️ **Lab-4: Cost Optimization in GCP**  

## 🎯 **Objective**  
In this lab, you will learn how to **optimize costs** in Google Cloud by:  
- Using **serverless architectures** like **BigQuery, Dataflow, and Cloud Functions**.  
- Optimizing **Cloud Storage costs** with **Lifecycle Policies**.  
- Reducing **query costs** with **BigQuery slot reservations** and **Dataflow job optimizations**.  

By the end of this lab, you will have an **efficient and cost-effective cloud data pipeline**.  

---

## 🛠️ **Lab Prerequisites**  
Before starting, ensure you have:  
1. **Google Cloud SDK** installed → [Install Guide](https://cloud.google.com/sdk/docs/install)  
2. **Terraform** installed → [Download Here](https://developer.hashicorp.com/terraform/downloads)  
3. A **GCP Project** with billing enabled  
4. A user account with:  
   - `roles/bigquery.admin`  
   - `roles/storage.admin`  
   - `roles/cloudfunctions.developer`  
   - `roles/dataflow.admin`  

---

## 🏗️ **Step 1: Use Serverless Architectures**  

### 🔹 **Enable Required APIs**  
```sh
gcloud services enable bigquery.googleapis.com dataflow.googleapis.com cloudfunctions.googleapis.com
```

### 🔹 **Deploy a Serverless Data Processing Pipeline**  
#### **1️⃣ BigQuery for Serverless Data Analytics**  
1. Create a BigQuery dataset:  
   ```sh
   bq mk --dataset $(gcloud config get-value project):cost_optimization_lab
   ```
2. Load sample data into BigQuery:  
   ```sh
   bq load --source_format=CSV \
   cost_optimization_lab.ecommerce_data \
   gs://cloud-samples-data/bigquery/sample-transactions/transactions.csv \
   transaction_id:STRING,customer_id:STRING,amount:FLOAT,timestamp:TIMESTAMP
   ```
✅ **Result:** **BigQuery eliminates the need for traditional servers, reducing costs.**  

#### **2️⃣ Dataflow for Pay-as-You-Go Processing**  
1. Create a Dataflow job:  
   ```sh
   gcloud dataflow jobs run cost-optimized-job \
   --gcs-location gs://dataflow-templates/latest/Word_Count \
   --region us-central1 \
   --staging-location gs://cost-optimization-lab/tmp/ \
   --parameters inputFile=gs://cloud-samples-data/text.txt,output=gs://cost-optimization-lab/output/
   ```
✅ **Result:** **Dataflow only charges for processing time, optimizing costs.**  

#### **3️⃣ Cloud Functions for Event-Driven Processing**  
1. Deploy a Cloud Function to process new files:  
   ```sh
   gcloud functions deploy processNewFile \
   --runtime python39 --trigger-bucket cost-optimization-lab \
   --entry-point process_file --memory=128MB --region=us-central1
   ```
✅ **Result:** **Cloud Functions scale automatically, reducing idle costs.**  

---

## 🔄 **Step 2: Optimize Storage Costs**  

### 🔹 **Enable Cloud Storage API**  
```sh
gcloud services enable storage.googleapis.com
```

### 🔹 **Set Up Lifecycle Management for Storage**  
1. Create a Cloud Storage bucket:  
   ```sh
   gsutil mb -c STANDARD -l US gs://cost-optimization-lab/
   ```
2. Create a lifecycle rule to automatically delete files after 30 days:  
   ```sh
   echo '{
     "rule": [{
       "action": {"type": "Delete"},
       "condition": {"age": 30}
     }]
   }' > lifecycle.json
   ```
3. Apply the lifecycle rule:  
   ```sh
   gsutil lifecycle set lifecycle.json gs://cost-optimization-lab/
   ```
✅ **Result:** **Unneeded data is automatically removed, saving storage costs.**  

---

## 📊 **Step 3: Monitor & Reduce Query Costs**  

### 🔹 **Reserve BigQuery Slots to Optimize Pricing**  
1. Enable BigQuery Reservations:  
   ```sh
   gcloud services enable bigqueryreservation.googleapis.com
   ```
2. Create a reservation for a **fixed number of slots**:  
   ```sh
   gcloud bigquery reservations create cost-optimized-reservation \
   --slots=100 --location=us-central1
   ```
3. Assign the reservation to the project:  
   ```sh
   gcloud bigquery reservations assignments create \
   --reservation-id=cost-optimized-reservation \
   --assignee=projects/$(gcloud config get-value project) \
   --location=us-central1
   ```
✅ **Result:** **Reserved slots provide predictable pricing and prevent costly on-demand queries.**  

### 🔹 **Optimize Dataflow Job Performance**  
1. Use **FlexRS for cost savings** in Dataflow:  
   ```sh
   gcloud dataflow jobs run optimized-job \
   --gcs-location gs://dataflow-templates/latest/Word_Count \
   --region us-central1 \
   --staging-location gs://cost-optimization-lab/tmp/ \
   --parameters inputFile=gs://cost-optimization-lab/sample.txt,output=gs://cost-optimization-lab/output/ \
   --flexrs
   ```
✅ **Result:** **FlexRS saves costs by running jobs in lower-cost resources when available.**  

---

## 🧹 **Lab Cleanup**  
To avoid unnecessary charges, **delete the resources** after the lab:  
```sh
bq rm -r -f cost_optimization_lab
gsutil rm -r gs://cost-optimization-lab/
gcloud dataflow jobs cancel cost-optimized-job --region=us-central1
gcloud bigquery reservations delete cost-optimized-reservation --location=us-central1
gcloud functions delete processNewFile --region=us-central1
gcloud services disable bigquery.googleapis.com dataflow.googleapis.com cloudfunctions.googleapis.com storage.googleapis.com bigqueryreservation.googleapis.com
```

---

## 🎯 **Key Takeaways**  
✅ Used **serverless services (BigQuery, Dataflow, Cloud Functions)** to minimize operational costs.  
✅ Implemented **Cloud Storage Lifecycle Policies** to optimize storage costs.  
✅ Used **BigQuery slot reservations & FlexRS** to reduce query processing costs.  

🚀 **Next Lab:** [Lab-5: Advanced Data Processing & Automation](./Lab_5-Advanced Data Processing & Automation/Lab-5-Advanced Data Processing & Automation.md)  
