# 📌 Preparing and Using Data for Analysis in Google Cloud

## 🔹 Overview
Preparing and using data for analysis is a key competency for **Google Cloud Professional Data Engineers**. This guide covers how to clean, transform, and analyze data using **GCP services**, with **real-world use cases**, and **implementation examples in Python, Java, and GCP CLI commands**.

---

## 🔹 **GCP Data Engineering Technologies for Data Preparation & Analysis**
| **Service**          | **Use Case**  |
|----------------------|------------------------------------------------|
| **BigQuery**        | Data warehousing, SQL-based analytics         |
| **Cloud Dataproc**  | Managed Spark/Hadoop for large-scale processing |
| **Cloud Dataflow**  | Real-time and batch data transformation        |
| **Cloud Storage**   | Storing raw and processed datasets             |
| **Cloud Pub/Sub**   | Event-driven data streaming                    |
| **Cloud Composer**  | Workflow automation and orchestration         |
| **Dataplex**        | Data governance, cataloging, and metadata management |

---

## 1️⃣ **[Data Ingestion and Cleaning](./Data_Ingestion_and_Cleaning.md)**
### 💼 **Real-World Use Case: Cleaning Customer Purchase Data**
**Scenario:** A **retail company** collects customer purchase data from various locations. The data must be **cleaned, deduplicated, and loaded** into BigQuery for reporting.

✅ **GCP Implementation:**
- **Cloud Storage:** Stores raw purchase data.
- **Cloud Dataprep:** Cleans and standardizes data.
- **BigQuery:** Stores processed data for analysis.

---

## 2️⃣ **[Transforming Data for Analysis](./Transforming_Data_for_Analysis.md)**
### 💼 **Real-World Use Case: Processing Streaming Data from IoT Sensors**
**Scenario:** A **smart home company** collects real-time temperature readings from IoT sensors. Data must be **aggregated and normalized** before analysis.

✅ **GCP Implementation:**
- **Cloud Pub/Sub:** Streams IoT sensor data.
- **Cloud Dataflow:** Processes and normalizes data.
- **BigQuery Streaming:** Stores structured analytics data.

---

## 3️⃣ **[Analyzing and Visualizing Data](./Analyzing_and_Visualizing_Data.md)**
### 💼 **Real-World Use Case: Marketing Campaign Performance Analytics**
**Scenario:** A **digital marketing agency** wants to analyze **advertising campaign performance** using **BigQuery and Looker**.

✅ **GCP Implementation:**
- **BigQuery:** Stores campaign performance data.
- **Looker Studio:** Visualizes key marketing metrics.

---

## 4️⃣ **[Programming Apache Beam](./Apache_Beam_Programming.md)**
Apache Beam provides a **unified programming model** to build **batch and streaming data pipelines**. As a **language-independent** framework, Beam supports both **Python and Java** for processing large-scale data.

### 📌 **Topics Covered**
- **Understanding Beam Pipelines**: How to construct and run **batch and streaming** pipelines.
- **Transforms**: Applying **Map, Filter, ParDo, GroupByKey, and CoGroupByKey**.
- **Aggregate Functions**: Calculating **averages, sums, standard deviations** in real-world data.
- **Windowing**: Applying **tumbling, sliding, and session windows** for real-time streaming.
- **Custom Functions**: Creating **custom DoFn transforms** for business logic.
- **Joining Data Streams**: Using **CoGroupByKey** for **merging multiple data sources**.

✅ **GCP Implementation with Apache Beam**
- **Cloud Dataflow**: Serverless execution of Beam pipelines.
- **Pub/Sub**: Handling streaming event data.
- **BigQuery**: Storing processed and aggregated results.

### 📌 **Example: Computing Average Temperature from IoT Sensors**
#### **Python Example: Calculating Running Average with Windowing**
```python
import apache_beam as beam
from apache_beam.transforms.window import SlidingWindows

with beam.Pipeline() as pipeline:
    (pipeline
     | 'Read from Pub/Sub' >> beam.io.ReadFromPubSub(topic='projects/iot/topics/sensor_data')
     | 'Apply Sliding Window' >> beam.WindowInto(SlidingWindows(60, 30))
     | 'Compute Average' >> beam.CombinePerKey(beam.combiners.MeanCombineFn())
     | 'Write to BigQuery' >> beam.io.WriteToBigQuery('iot.analytics.sensor_averages')
    )

---

## 📌 Conclusion
🔹 **Ingestion & Cleaning:** Use Cloud Storage, Dataprep, and BigQuery for structured, high-quality data.
🔹 **Transformation:** Use Dataflow and Pub/Sub for batch and real-time processing.
🔹 **Analysis & Visualization:** Use BigQuery and Looker Studio for actionable insights.
