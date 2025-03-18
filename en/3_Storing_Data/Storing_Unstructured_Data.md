# 📦 Storing Unstructured Data in Google Cloud

## Overview
Unstructured data includes images, videos, audio files, PDFs, logs, and other non-tabular content that does not fit into a traditional relational database. Google Cloud provides several storage solutions optimized for handling large volumes of unstructured data efficiently and securely.

## 🔹 Google Cloud Storage (GCS)
Google Cloud Storage is the primary service for storing unstructured data, offering scalable, durable, and secure object storage.

### ✅ **Key Features:**
- **Scalability:** Handles petabyte-scale data storage.
- **Durability:** 99.999999999% (11 nines) durability with multi-regional replication.
- **Security:** IAM roles, encryption (at rest and in transit), and VPC Service Controls.
- **Lifecycle Management:** Automatically transitions objects between storage classes.
- **Integration:** Works with BigQuery, Dataflow, and AI/ML services.

### 📂 **Storage Classes:**
| Storage Class | Use Case |
|--------------|---------|
| **Standard** | Frequently accessed data, low-latency apps |
| **Nearline** | Infrequently accessed data (once a month) |
| **Coldline** | Archival data, rarely accessed (once a year) |
| **Archive** | Long-term storage for compliance and backups |

## 🔹 Cloud Filestore
Google Cloud Filestore provides high-performance **managed file storage** for applications that require shared access to files.

### ✅ **Key Features:**
- **Fully managed network file system (NFS).**
- **Supports workloads like machine learning, rendering, and enterprise apps.**
- **Three performance tiers: Basic HDD, Basic SSD, and High Scale SSD.**
- **Supports Kubernetes workloads.**

## 🔹 Bigtable for Semi-Structured Data
Bigtable is a NoSQL wide-column database optimized for high-throughput, low-latency access to unstructured or semi-structured data.

### ✅ **Key Features:**
- **Ideal for time-series data, analytics, and IoT applications.**
- **Handles terabytes to petabytes of data.**
- **Integrated with BigQuery and Dataflow.**
- **Strong consistency and replication support.**

## 🔹 Logging & Analytics Solutions
For storing logs and real-time unstructured data, GCP provides:
- **Cloud Logging** – Stores system and application logs for monitoring.
- **BigQuery** – Allows querying log data for analytics.
- **Pub/Sub** – Real-time event ingestion for streaming unstructured data.

## 🔹 Best Practices for Storing Unstructured Data
✅ **Use appropriate storage classes** to optimize cost vs. retrieval needs.
✅ **Enable object versioning** in Cloud Storage to protect against accidental deletions.
✅ **Encrypt data at rest and in transit** using Cloud KMS.
✅ **Implement IAM policies** to restrict access based on roles.
✅ **Monitor and optimize storage usage** using Cloud Monitoring and audit logs.

## 📌 Hands-on Lab
1. **Create a Cloud Storage bucket and upload files.**
2. **Set up lifecycle policies** to transition objects between storage classes.
3. **Enable object versioning** and test recovery of deleted files.
4. **Secure storage with IAM roles and encryption.**
5. **Integrate Cloud Storage with AI/ML models for image classification.**

