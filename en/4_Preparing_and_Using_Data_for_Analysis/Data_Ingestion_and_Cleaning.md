# 📌 Data Ingestion and Cleaning in Google Cloud

## 🔹 Overview
Data ingestion and cleaning are critical components of **data engineering**, ensuring that data is properly collected, cleaned, and prepared for analysis.

In this document, we will explore:
- **ETL vs. ELT** methodologies
- **Batch vs. Streaming Ingestion** methods
- **GCP Technologies for Data Ingestion and Cleaning**
- **Implementation Best Practices**
- **Code Examples using Python, Java, and GCP CLI**

---

## 🔹 ETL vs. ELT: Understanding the Differences
### **Extract, Transform, Load (ETL)**
- Data is extracted from **source systems**, transformed into a required format, and then loaded into a **destination system**.
- **Best for:** Traditional **data warehouses**, where structured and cleaned data is required before loading.
- **Common GCP Services:** Cloud Dataflow, Cloud Dataproc, BigQuery.

### **Extract, Load, Transform (ELT)**
- Data is first extracted and loaded into a **data lake or warehouse**, then transformed inside the storage system.
- **Best for:** **Big data and analytics workloads**, where **schema-on-read** processing is required.
- **Common GCP Services:** BigQuery, Cloud Dataplex, Cloud Storage.

---

## 🔹 Batch vs. Streaming Data Ingestion
### **Batch Ingestion**
- Data is collected and processed in predefined time intervals (e.g., hourly or daily).
- **Best for:** **Scheduled ETL jobs**, traditional **data warehouses**.
- **Common GCP Services:** Cloud Storage, BigQuery Data Transfer Service, Dataproc.

### **Streaming Ingestion**
- Data is continuously processed in real time as it arrives.
- **Best for:** **Fraud detection, IoT analytics, real-time dashboards**.
- **Common GCP Services:** Pub/Sub, Dataflow, BigQuery Streaming API.

---

## 🔹 GCP Technologies for Data Ingestion
### **1️⃣ Cloud Storage (Batch & Streaming)**
✅ **Use Case:** Storing raw structured or unstructured data for batch or streaming ingestion.
✅ **Best Practices:**
- Use **Lifecycle Policies** to automate archiving and deletion.
- Enable **Object Versioning** to recover lost or overwritten data.

#### **GCP CLI Example: Uploading a File to Cloud Storage**
```sh
gsutil cp sales_data.csv gs://ecommerce-data/sales/
```

---

### **2️⃣ Cloud Pub/Sub (Streaming)**
✅ **Use Case:** Real-time data streaming from multiple sources.
✅ **Best Practices:**
- Use **topic retention policies** to avoid data loss.
- Implement **dead-letter topics** for failed messages.

#### **Python Example: Publishing Messages to a Pub/Sub Topic**
```python
from google.cloud import pubsub_v1

publisher = pubsub_v1.PublisherClient()
topic_path = publisher.topic_path("my-project", "sales-topic")
publisher.publish(topic_path, b"New order received")
print("Message published!")
```

---

### **3️⃣ Cloud Dataflow (Batch & Streaming ETL)**
✅ **Use Case:** Processing large-scale data pipelines in real time.
✅ **Best Practices:**
- Use **checkpointing** to ensure fault tolerance.
- Optimize **worker autoscaling** to reduce cost.

#### **Java Example: Dataflow Pipeline for Processing Streaming Data**
```java
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.transforms.ParDo;

Pipeline pipeline = Pipeline.create();
pipeline.apply("Read from Pub/Sub", PubsubIO.readStrings().fromTopic("projects/my-project/topics/sales-topic"))
        .apply("Process Data", ParDo.of(new DataProcessor()))
        .apply("Write to BigQuery", BigQueryIO.writeTableRows().to("my-dataset.sales_data"));
pipeline.run();
```

---

### **4️⃣ BigQuery (Batch & Streaming ELT)**
✅ **Use Case:** **Data warehousing and real-time analytics**.
✅ **Best Practices:**
- Use **partitioned tables** for faster queries.
- Enable **automatic schema detection** when loading new data.

#### **GCP CLI Example: Load Data into BigQuery**
```sh
gcloud bigquery load --source_format=CSV --autodetect \
    ecommerce.analytics.sales gs://ecommerce-data/sales.csv
```

---

### **5️⃣ Cloud Dataprep (Data Cleaning & Transformation)**
✅ **Use Case:** **Data preparation and cleansing before loading into analytics systems**.
✅ **Best Practices:**
- Use **predefined data cleansing templates** for common transformations.
- Automate **data wrangling jobs** for scheduled ETL workflows.

#### **Python Example: Deduplicating Data Before Loading to BigQuery**
```python
from google.cloud import bigquery

client = bigquery.Client()
query = """
DELETE FROM retail.analytics.purchases
WHERE row_number() OVER (PARTITION BY customer_id ORDER BY purchase_date DESC) > 1;
"""
client.query(query)
print("Duplicate records removed successfully.")
```

---

## 📌 Conclusion
✅ **Batch vs. Streaming:** Choose batch for scheduled processing, streaming for real-time ingestion.
✅ **ETL vs. ELT:** Use ETL for structured processing, ELT for big data analytics.
✅ **Best GCP Services:**
- **Cloud Storage & Pub/Sub** for ingestion
- **Dataflow & Dataprep** for transformation
- **BigQuery** for storage & analytics
