# 📌 Data Engineering Concepts & GCP Applications

## 🔹 Overview
Data engineering involves designing and managing systems for collecting, storing, and processing data efficiently. This document covers key concepts such as **Data Lakes, Data Warehouses, and Data Meshes**, along with **real-world use cases** and how to apply **Google Cloud Platform (GCP) services** to implement them, with examples in **Python and Java**.

---

## 1️⃣ **Data Lake**
### 📌 What is a Data Lake?
A **Data Lake** is a centralized repository that stores structured, semi-structured, and unstructured data at any scale. It enables organizations to store raw data **as-is** and apply transformations later as needed.

### 🔹 **Key Features:**
- **Schema-on-read:** Data is stored in raw format and schema is applied when querying.
- **Cost-effective storage:** Uses cloud object storage (e.g., Cloud Storage).
- **Scalable:** Can handle vast amounts of diverse data.

### 💼 **Real-World Use Case: Retail Data Lake**
A multinational **retail company** collects sales transactions, customer interactions, and inventory data from multiple sources. Instead of structuring the data upfront, they store everything in a Data Lake for later processing.

✅ **GCP Implementation:**
- **Cloud Storage:** Stores raw transactional logs, clickstream data, and images.
- **Dataproc:** Runs Spark/Hadoop jobs to clean and transform data.
- **BigQuery:** Performs interactive analysis on processed data.
- **Pub/Sub + Dataflow:** Streams real-time data into the lake.

#### **Python Example:**
```python
import apache_beam as beam
from apache_beam.options.pipeline_options import PipelineOptions

options = PipelineOptions(
    runner='DataflowRunner',
    project='retail-data-project',
    temp_location='gs://retail-bucket/temp',
    region='us-central1'
)

with beam.Pipeline(options=options) as pipeline:
    data = pipeline | 'Read Clickstream' >> beam.io.ReadFromText('gs://retail-bucket/clickstream.json')
    data | 'Store in BigQuery' >> beam.io.WriteToBigQuery('retail.analytics.clickstream')
```

#### **Java Example:**
```java
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;

public class DataLakePipeline {
    public static void main(String[] args) {
        Pipeline pipeline = Pipeline.create();

        PCollection<String> data = pipeline.apply("Read Clickstream", TextIO.read().from("gs://retail-bucket/clickstream.json"));
        
        data.apply("Write to BigQuery", TextIO.write().to("retail.analytics.clickstream"));
        
        pipeline.run().waitUntilFinish();
    }
}
```

---

## 2️⃣ **Data Warehouse**
### 📌 What is a Data Warehouse?
A **Data Warehouse** is a structured, optimized system designed for running analytical queries on large datasets. It is **schema-on-write**, meaning data is structured before being stored.

### 🔹 **Key Features:**
- Optimized for **OLAP (Online Analytical Processing)**.
- Uses **ETL (Extract, Transform, Load)** processes.
- Supports **SQL-based queries** for analysis.

### 💼 **Real-World Use Case: Financial Analytics Platform**
A **banking institution** needs to analyze customer transactions, credit history, and fraud detection patterns. The data is structured and pre-processed before being loaded into a Data Warehouse.

✅ **GCP Implementation:**
- **BigQuery:** Stores structured financial transaction data.
- **Dataflow:** ETL pipelines for data transformation.
- **Cloud Composer (Airflow):** Schedules data workflows.
- **Looker:** BI tool for dashboarding and reporting.

#### **Python Example:**
```python
from google.cloud import bigquery

client = bigquery.Client()
query = """
SELECT customer_id, SUM(amount) AS total_spent
FROM `banking.transactions`
WHERE transaction_date >= DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY)
GROUP BY customer_id
HAVING total_spent > 10000;
"""
result = client.query(query)
for row in result:
    print(f"Customer: {row.customer_id}, Total Spent: {row.total_spent}")
```

#### **Java Example:**
```java
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;

public class BigQueryExample {
    public static void main(String[] args) throws Exception {
        BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();

        String query = "SELECT customer_id, SUM(amount) AS total_spent FROM `banking.transactions` " +
                "WHERE transaction_date >= DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY) " +
                "GROUP BY customer_id HAVING total_spent > 10000;";

        TableResult result = bigquery.query(QueryJobConfiguration.newBuilder(query).build());
        result.iterateAll().forEach(row -> System.out.println("Customer: " + row.get("customer_id").getStringValue() +
                ", Total Spent: " + row.get("total_spent").getDoubleValue()));
    }
}
```

---

## 📌 Conclusion
- **Data Lakes** provide flexible, low-cost storage but require processing later.
- **Data Warehouses** are optimized for structured analytics and BI tools.
- **Data Meshes** enable **distributed, domain-driven data ownership**.
