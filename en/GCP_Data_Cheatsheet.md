# 🚀 GCP Data Engineering Cheatsheet

## 📌 Overview
This cheatsheet provides a quick reference for **Google Cloud Platform (GCP) tools, APIs, and services** commonly used by **GCP Data Engineers**. It explains **when** to use each tool, **why** it's important, and provides **real-world business use cases**, along with their **equivalents in AWS, Azure, and Oracle Cloud** to help guide multi-cloud decision-making.

---

## 🔹 Data Storage & Warehousing

### **1️⃣ BigQuery (BQ)**
![Big Query](../images/gcp/Big%20Data/bigquery-productcard.jpg)
✅ **When to Use:**
- Large-scale data analytics and reporting.
- Running **fast** SQL queries on petabyte-scale data.
- Integrating with BI tools like Looker & Data Studio.

🛠 **Why:**
- Fully managed, **serverless data warehouse**.
- Optimized for **OLAP (Online Analytical Processing)** workloads.

💼 **Real-World Use Case:**
*A retail company processes sales transactions from millions of customers daily. They use BigQuery for real-time analytics, fraud detection, and customer segmentation.*

🔄 **Equivalent Services:**
- **AWS:** Amazon Redshift
- **Azure:** Azure Synapse Analytics
- **Oracle:** Oracle Autonomous Data Warehouse

---

### **2️⃣ Cloud Storage (GCS)**
![Cloud Storage](../images/gcp/Storage%20and%20Databases/cloud-storage-productcard.jpg)
✅ **When to Use:**
- Storing unstructured data (images, videos, logs, backups).
- Data lake for batch/streaming ETL processing.
- Cost-effective archival storage (Coldline, Nearline).

🛠 **Why:**
- High availability, low-cost object storage.
- Supports **multi-regional replication** for reliability.

💼 **Real-World Use Case:**
*An eCommerce platform stores product images and customer invoices in Cloud Storage and processes batch analytics using Dataflow and BigQuery.*

🔄 **Equivalent Services:**
- **AWS:** Amazon S3
- **Azure:** Azure Blob Storage
- **Oracle:** Oracle Cloud Object Storage

---

### **3️⃣ Cloud SQL & Spanner**
![Cloud SQL](../images/gcp/Storage%20and%20Databases/cloud-sql-productcard.jpg)
✅ **When to Use:**
- **Cloud SQL:** Relational databases (MySQL, PostgreSQL) for transactional applications.
- **Spanner:** Global-scale, **strongly consistent** distributed relational database.

🛠 **Why:**
- **Cloud SQL** = Managed relational DB, auto backups.
- **Spanner** = **High availability & scalability** with strong consistency.

💼 **Real-World Use Case:**
*A fintech company uses Cloud SQL for customer transaction history and Cloud Spanner for global trading data that requires strong consistency across regions.*

🔄 **Equivalent Services:**
- **AWS:** Amazon RDS (for Cloud SQL), Amazon Aurora (for Spanner)
- **Azure:** Azure SQL Database, Cosmos DB
- **Oracle:** Oracle Autonomous Transaction Processing (ATP)

---

## 🔹 Data Processing & Pipelines

### **4️⃣ Dataflow (Apache Beam)**
![Dataflow](../images/gcp/Big%20Data/dataflow-productcard.jpg)
✅ **When to Use:**
- Real-time & batch data processing.
- ETL pipelines (Extract, Transform, Load).

🛠 **Why:**
- **Serverless & auto-scaling.**
- Supports **batch & stream processing**.

💼 **Real-World Use Case:**
*A telecom company analyzes customer call logs in real-time using Dataflow to detect fraudulent behavior and trigger alerts instantly.*

🔄 **Equivalent Services:**
- **AWS:** AWS Glue, Kinesis Data Analytics
- **Azure:** Azure Data Factory, Azure Stream Analytics
- **Oracle:** Oracle Cloud Data Flow

---

### **5️⃣ Dataproc (Managed Hadoop/Spark)**
![Dataproc](../images/gcp/Big%20Data/dataproc-productcard.jpg)
✅ **When to Use:**
- Running Apache Spark, Hadoop, Presto, or Hive on GCP.
- Migrating on-premise Hadoop clusters to GCP.

🛠 **Why:**
- Fully managed, **on-demand clusters**.
- Integrated with BigQuery, Cloud Storage.

💼 **Real-World Use Case:**
*A bank migrates its on-premise Hadoop cluster to Dataproc to run credit risk modeling on historical transaction data.*

🔄 **Equivalent Services:**
- **AWS:** Amazon EMR
- **Azure:** Azure HDInsight
- **Oracle:** Oracle Big Data Service

---

## 🔹 Messaging & Event-Driven Data

### **6️⃣ Pub/Sub**
![Pub/Sub](../images/gcp/Big%20Data/pub_sub-productcard.jpg)
✅ **When to Use:**
- Event-driven architectures (real-time streaming data processing).
- Streaming ingestion pipelines.

🛠 **Why:**
- **Decouples event producers & consumers**.
- **Scales dynamically** to handle millions of messages per second.

💼 **Real-World Use Case:**
*A ride-sharing app uses Pub/Sub to send real-time location updates from drivers to riders.*

🔄 **Equivalent Services:**
- **AWS:** Amazon SNS (Simple Notification Service), Amazon SQS (Simple Queue Service)
- **Azure:** Azure Event Grid, Azure Service Bus
- **Oracle:** Oracle Cloud Streaming

---

## 🔹 Machine Learning & AI

### **7️⃣ Vertex AI**
![Vertex AI](../images/gcp/Machine%20Learning/AIPlatform-productcard.jpg)
✅ **When to Use:**
- Building & deploying **ML models at scale**.
- AutoML for **no-code ML model training**.

🛠 **Why:**
- Integrated with **BigQuery ML, TensorFlow, PyTorch**.
- End-to-end MLOps support.

💼 **Real-World Use Case:**
*A marketing agency predicts customer churn using AutoML in Vertex AI and deploys a personalized retention strategy.*

🔄 **Equivalent Services:**
- **AWS:** Amazon SageMaker
- **Azure:** Azure Machine Learning
- **Oracle:** Oracle AI & Machine Learning

---

## 📌 Final Thoughts
🚀 **Choosing the Right Cloud Service:**
| **Business Need**         | **GCP**          | **AWS**            | **Azure**                 | **Oracle Cloud**           |
|---------------------------|-----------------|---------------------|---------------------------|---------------------------|
| Store structured data     | BigQuery, Spanner | Redshift, Aurora   | Synapse, Cosmos DB       | Autonomous Data Warehouse |
| Store unstructured data   | Cloud Storage    | S3                 | Blob Storage             | Object Storage           |
| Real-time streaming data  | Pub/Sub         | Kinesis            | Event Grid               | Streaming                 |
| Batch data processing     | Dataflow, Dataproc | Glue, EMR        | Data Factory, HDInsight | Data Flow, Big Data      |
| Data pipeline orchestration | Cloud Composer | Step Functions     | Azure Data Factory       | Oracle Data Integrator   |
| Build ML models           | Vertex AI       | SageMaker          | Azure ML                 | Oracle AI & ML           |
| Logging & monitoring      | Cloud Logging   | CloudWatch         | Azure Monitor            | Oracle Management Cloud  |
