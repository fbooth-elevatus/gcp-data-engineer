# 📌 Storing Data in Google Cloud

## 🔹 Overview
Storing data efficiently is a fundamental requirement for **Google Cloud Professional Data Engineers**. Google Cloud Platform (**GCP**) offers **scalable, secure, and cost-effective** storage solutions for different types of data, including structured, semi-structured, unstructured, and in-memory data.

This guide covers:
- **GCP storage services and their real-world use cases**.
- **Best practices for data storage and retrieval**.
- **Implementation examples using Python, Java, and GCP CLI commands**.

---

## 🔹 **[GCP Storage Services and Use Cases](./GCP_Storage_Services_and_Use_Cases.md)**

| **Service**         | **Use Case** |
|--------------------|------------------------------------------------|
| **Cloud Storage**  | Unstructured data, backups, media, ML datasets |
| **BigQuery**       | Data warehousing, analytics, large-scale queries |
| **Cloud SQL**      | Relational databases (PostgreSQL, MySQL, SQL Server) |
| **Cloud Spanner**  | Globally distributed, strongly consistent database |
| **Firestore**      | NoSQL, real-time synchronization, app development |
| **Bigtable**       | High-throughput NoSQL for analytics and time-series data |
| **Dataplex**       | Data governance, metadata management, and lakehouse storage |
| **Memorystore**    | In-memory caching for high-speed applications (Redis & Memcached) |

---

## 1️⃣ **[Storing Unstructured Data](./Storing_Unstructured_Data.md)**
### 💼 **Real-World Use Case: Media Storage for a Streaming Service**
**Scenario:** A **video streaming platform** needs to store and serve **millions of video files** efficiently.

✅ **GCP Implementation:**
- **Cloud Storage:** Stores raw video files.
- **Cloud CDN:** Speeds up global delivery.
- **Signed URLs:** Secures access to private files.

---

## 2️⃣ **[Storing Structured Data (SQL Databases)](./Storing_Structured_Data.md)**
### 💼 **Real-World Use Case: E-Commerce Sales Data Warehousing**
**Scenario:** A **large online retailer** collects **transaction data** and needs a scalable solution for analytics and reporting.

✅ **GCP Implementation:**
- **Cloud SQL:** Best for OLTP applications requiring relational database features.
- **Cloud Spanner:** Ideal for globally distributed transactions.
- **BigQuery:** Optimized for analytics and reporting.

#### **GCP CLI Command: Create a Cloud SQL Instance**
```sh
 gcloud sql instances create ecommerce-db --database-version=POSTGRES_14 --tier=db-f1-micro --region=us-central1
```

---

## 3️⃣ **[Storing NoSQL and Time-Series Data](./Storing_Structured_Data.md)**
### 💼 **Real-World Use Case: IoT Sensor Data Storage**
**Scenario:** A **smart city project** collects IoT sensor data and needs a high-throughput, low-latency storage system for analysis.

✅ **GCP Implementation:**
- **Cloud Bigtable:** Stores time-series sensor data.
- **Cloud Pub/Sub:** Streams real-time IoT data.
- **Cloud Firestore:** Stores real-time app data.

---

## 4️⃣ **Using In-Memory Databases for High Performance**
### 💼 **Real-World Use Case: E-Commerce Cart Management**
**Scenario:** A **large e-commerce website** needs **fast** retrieval of user shopping cart data without database latency.

✅ **GCP Implementation:**
- **Memorystore (Redis):** Fast, in-memory cache for real-time session management.
- **Memorystore (Memcached):** Scales for distributed caching.

#### **GCP CLI Command: Create a Memorystore Redis Instance**
```sh
gcloud redis instances create cart-cache --size=1 --region=us-central1 --redis-version=REDIS_6_X
```

---

## 📌 Conclusion
🔹 **Unstructured Data:** Use Cloud Storage for images, videos, and backups.
🔹 **Structured Data:** Use BigQuery and Cloud SQL for analytical processing.
🔹 **NoSQL & Time-Series Data:** Use Bigtable for high-throughput, low-latency applications.
🔹 **In-Memory Databases:** Use Memorystore (Redis/Memcached) for ultra-fast caching and real-time session storage.

