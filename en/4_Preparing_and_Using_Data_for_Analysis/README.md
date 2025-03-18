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

#### **Python Example: Cleaning and Loading Data to BigQuery**
```python
from google.cloud import bigquery

client = bigquery.Client()
query = """
DELETE FROM retail.analytics.purchases
WHERE row_number() OVER (PARTITION BY customer_id ORDER BY purchase_date DESC) > 1;
"""
client.query(query)
print("Duplicate customer purchases removed successfully.")
```

#### **Java Example: Deduplicating Data in BigQuery**
```java
import com.google.cloud.bigquery.*;

public class CleanPurchaseData {
    public static void main(String[] args) throws Exception {
        BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
        String query = "DELETE FROM retail.analytics.purchases " +
                       "WHERE row_number() OVER (PARTITION BY customer_id ORDER BY purchase_date DESC) > 1;";
        bigquery.query(QueryJobConfiguration.newBuilder(query).build());
        System.out.println("Duplicate purchases removed.");
    }
}
```

#### **GCP CLI Command: Running a Query in BigQuery**
```sh
gcloud bigquery query \
    --use_legacy_sql=false \
    "DELETE FROM retail.analytics.purchases WHERE row_number() OVER (PARTITION BY customer_id ORDER BY purchase_date DESC) > 1;"
```

---

## 2️⃣ **[Transforming Data for Analysis](./Transforming_Data_for_Analysis.md)**
### 💼 **Real-World Use Case: Processing Streaming Data from IoT Sensors**
**Scenario:** A **smart home company** collects real-time temperature readings from IoT sensors. Data must be **aggregated and normalized** before analysis.

✅ **GCP Implementation:**
- **Cloud Pub/Sub:** Streams IoT sensor data.
- **Cloud Dataflow:** Processes and normalizes data.
- **BigQuery Streaming:** Stores structured analytics data.

#### **Python Example: Dataflow Pipeline for Streaming Processing**
```python
import apache_beam as beam
from apache_beam.options.pipeline_options import PipelineOptions

options = PipelineOptions(
    streaming=True,
    runner='DataflowRunner',
    project='iot-analytics',
    temp_location='gs://iot-temp-bucket/temp',
    region='us-central1'
)

with beam.Pipeline(options=options) as pipeline:
    events = pipeline | 'Read from Pub/Sub' >> beam.io.ReadFromPubSub(topic='projects/iot/topics/sensor_data')
    parsed = events | 'Parse JSON' >> beam.Map(lambda x: eval(x.decode('utf-8')))
    parsed | 'Write to BigQuery' >> beam.io.WriteToBigQuery('iot.analytics.sensors')
```

#### **Java Example: Streaming Dataflow Pipeline**
```java
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;

public class StreamingDataflow {
    public static void main(String[] args) {
        Pipeline pipeline = Pipeline.create();
        
        pipeline.apply("Read Pub/Sub", PubsubIO.readStrings().fromTopic("projects/iot/topics/sensor_data"))
                .apply("Write to BigQuery", BigQueryIO.writeTableRows().to("iot.analytics.sensors"));
        
        pipeline.run().waitUntilFinish();
    }
}
```

#### **GCP CLI Command: Run Streaming Dataflow Job**
```sh
gcloud dataflow jobs run streaming-sensor-data \
    --gcs-location gs://dataflow-templates/latest/PubSub_to_BigQuery \
    --parameters inputTopic=projects/iot/topics/sensor_data,outputTable=iot.analytics.sensors
```

---

## 3️⃣ **[Analyzing and Visualizing Data](./Analyzing_and_Visualizing_Data.md)**
### 💼 **Real-World Use Case: Marketing Campaign Performance Analytics**
**Scenario:** A **digital marketing agency** wants to analyze **advertising campaign performance** using **BigQuery and Looker**.

✅ **GCP Implementation:**
- **BigQuery:** Stores campaign performance data.
- **Looker Studio:** Visualizes key marketing metrics.

#### **Python Example: Running an Aggregation Query in BigQuery**
```python
query = """
SELECT campaign_id, AVG(conversion_rate) as avg_conversion
FROM marketing.analytics.campaign_performance
GROUP BY campaign_id
ORDER BY avg_conversion DESC;
"""
query_job = client.query(query)
for row in query_job:
    print(f"Campaign: {row.campaign_id}, Conversion Rate: {row.avg_conversion}")
```

#### **Java Example: Querying Marketing Data in BigQuery**
```java
BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
String query = "SELECT campaign_id, AVG(conversion_rate) as avg_conversion FROM marketing.analytics.campaign_performance " +
               "GROUP BY campaign_id ORDER BY avg_conversion DESC;";
TableResult result = bigquery.query(QueryJobConfiguration.newBuilder(query).build());
result.iterateAll().forEach(row -> System.out.println("Campaign: " + row.get("campaign_id").getStringValue() +
        ", Conversion Rate: " + row.get("avg_conversion").getDoubleValue()));
```

#### **GCP CLI Command: Query Marketing Data in BigQuery**
```sh
gcloud bigquery query --use_legacy_sql=false \
    "SELECT campaign_id, AVG(conversion_rate) as avg_conversion FROM marketing.analytics.campaign_performance GROUP BY campaign_id ORDER BY avg_conversion DESC;"
```

---

## 📌 Conclusion
🔹 **Ingestion & Cleaning:** Use Cloud Storage, Dataprep, and BigQuery for structured, high-quality data.
🔹 **Transformation:** Use Dataflow and Pub/Sub for batch and real-time processing.
🔹 **Analysis & Visualization:** Use BigQuery and Looker Studio for actionable insights.
