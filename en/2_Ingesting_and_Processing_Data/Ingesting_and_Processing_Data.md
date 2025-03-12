# 📌 Ingesting and Processing Data in Google Cloud

## 🔹 Overview
Ingesting and processing data efficiently is critical for **Google Cloud Professional Data Engineers**. This guide covers **batch and streaming data ingestion**, transformation, and processing pipelines using Google Cloud Platform (**GCP**) services, with **real-world use cases** and example implementations in **Python, Java, and GCP CLI commands**.

---

## 🔹 **Best Practices for Data Ingestion & Processing**

### 1️⃣ **Batch vs. Streaming Data Processing**
- **Batch Processing:**
  - Processes large data sets at scheduled intervals.
  - Best for ETL jobs, data warehousing, and historical analysis.
  - **GCP Services:** Cloud Storage, BigQuery, Dataproc, Cloud Composer.
- **Streaming Processing:**
  - Processes data in real-time as it arrives.
  - Best for IoT data, fraud detection, and real-time analytics.
  - **GCP Services:** Pub/Sub, Dataflow, BigQuery Streaming, Vertex AI.

### 2️⃣ **Optimizing Data Pipelines**
- **Use Auto-Scaling Services:** Cloud Dataflow and Dataproc scale dynamically.
- **Ensure Schema Consistency:** Use BigQuery’s schema enforcement for structured data.
- **Apply Data Validation:** Use Cloud Dataprep for preprocessing and data integrity.
- **Monitor Pipelines:** Utilize Cloud Logging and Cloud Monitoring.

---

## 1️⃣ **Batch Data Ingestion & Processing**
### 💼 **Real-World Use Case: ETL Pipeline for Sales Data**
**Scenario:** A **retail company** ingests daily sales data from multiple stores and processes it for business intelligence.

✅ **GCP Implementation:**
- **Cloud Storage:** Stores raw CSV sales data.
- **Cloud Composer:** Automates the ETL workflow.
- **BigQuery:** Stores processed data for analytics.

#### **Python Example: Ingest CSV Files to BigQuery**
```python
from google.cloud import storage, bigquery

gcs_client = storage.Client()
bq_client = bigquery.Client()

bucket_name = "retail-sales-data"
dataset_id = "retail_analytics"
table_id = "daily_sales"

uri = f"gs://{bucket_name}/sales_*.csv"
job_config = bigquery.LoadJobConfig(
    source_format=bigquery.SourceFormat.CSV,
    skip_leading_rows=1,
    autodetect=True,
)

load_job = bq_client.load_table_from_uri(uri, f"{dataset_id}.{table_id}", job_config=job_config)
load_job.result()
print("Sales data successfully loaded into BigQuery.")
```

#### **Java Example: Batch Data Load to BigQuery**
```java
import com.google.cloud.bigquery.*;

public class BigQueryBatchLoad {
    public static void main(String[] args) throws Exception {
        BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
        String datasetName = "retail_analytics";
        String tableName = "daily_sales";
        String sourceUri = "gs://retail-sales-data/sales_*.csv";

        LoadJobConfiguration loadConfig = LoadJobConfiguration.newBuilder(
                TableId.of(datasetName, tableName), sourceUri)
                .setFormatOptions(FormatOptions.csv())
                .setAutodetect(true)
                .build();

        Job job = bigquery.create(JobInfo.of(loadConfig));
        job.waitFor();
        System.out.println("Sales data successfully loaded into BigQuery.");
    }
}
```

#### **GCP CLI Command:**
```sh
gcloud bigquery load --source_format=CSV --autodetect \
    retail_analytics.daily_sales gs://retail-sales-data/sales_*.csv
```

---

## 2️⃣ **Streaming Data Ingestion & Processing**
### 💼 **Real-World Use Case: IoT Sensor Data Processing**
**Scenario:** A **manufacturing company** monitors factory machinery health with IoT sensors and needs real-time anomaly detection.

✅ **GCP Implementation:**
- **Pub/Sub:** Streams IoT sensor data.
- **Dataflow:** Processes sensor data in real-time.
- **BigQuery Streaming:** Stores structured data for analysis.

#### **Python Example: Stream Data from Pub/Sub to BigQuery**
```python
from google.cloud import pubsub_v1, bigquery
import json

subscriber = pubsub_v1.SubscriberClient()
topic_path = "projects/manufacturing-iot/topics/sensor-data"
subscription_path = "projects/manufacturing-iot/subscriptions/sensor-sub"

bq_client = bigquery.Client()
dataset_id = "iot_analytics"
table_id = "sensor_readings"

def callback(message):
    data = json.loads(message.data.decode("utf-8"))
    rows = [{"sensor_id": data["sensor_id"], "temperature": data["temperature"], "timestamp": data["timestamp"]}]
    errors = bq_client.insert_rows_json(f"{dataset_id}.{table_id}", rows)
    if not errors:
        print("Inserted into BigQuery.")
    message.ack()

subscriber.subscribe(subscription_path, callback=callback)
print("Listening for messages...")
```

#### **Java Example: Streaming Data from Pub/Sub**
```java
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import com.google.api.core.ApiService;
import com.google.cloud.bigquery.*;

public class PubSubToBigQuery {
    public static void main(String[] args) {
        String projectId = "manufacturing-iot";
        String subscriptionId = "sensor-sub";
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId);
        
        Subscriber subscriber = Subscriber.newBuilder(subscriptionName, (message, consumer) -> {
            System.out.println("Received message: " + message.getData().toStringUtf8());
            consumer.ack();
        }).build();
        subscriber.addListener(new ApiService.Listener() {}, java.util.concurrent.Executors.newSingleThreadExecutor());
        subscriber.startAsync().awaitRunning();
    }
}
```

#### **GCP CLI Command:**
```sh
gcloud pubsub subscriptions pull sensor-sub --auto-ack
```

---

## 📌 Conclusion
🔹 **Batch Processing:** Use Cloud Storage, BigQuery, and Dataproc for ETL pipelines.
🔹 **Streaming Processing:** Use Pub/Sub, Dataflow, and BigQuery for real-time analytics.
🔹 **Automate & Monitor:** Use Cloud Composer for orchestration and Cloud Logging for pipeline monitoring.

