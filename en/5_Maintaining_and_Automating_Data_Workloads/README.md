# 📌 Maintaining and Automating Data Workloads in Google Cloud

## 🔹 Overview
Maintaining and automating data workloads is critical for ensuring **efficiency, scalability, and reliability** in **big data processing**. Google Cloud Platform (**GCP**) provides several **big data services and automation tools** that support **batch and real-time processing**.

This guide covers:
- **Big Data services and their use cases**.
- **Batch vs. real-time data workload automation**.
- **Best practices for automating pipelines using Dataflow, Composer, and Workflows**.
- **Implementation with Python, Java, and GCP CLI commands**.

---

## 🔹 **GCP Big Data Services**
| **Service**          | **Use Case**  |
|----------------------|------------------------------------------------|
| **BigQuery**        | Data warehousing, analytical queries          |
| **Cloud Storage**   | Storing raw and processed data                 |
| **Cloud Pub/Sub**   | Event-driven data streaming                    |
| **Cloud Dataflow**  | Real-time and batch data processing            |
| **Cloud Dataproc**  | Managed Hadoop, Spark, and Presto clusters     |
| **Cloud Composer**  | Orchestration of batch and streaming workflows |
| **Cloud Functions** | Event-driven automation                        |
| **Workflows**       | Automating cross-service processes             |

---

## 1️⃣ **[Automating Batch Data Workloads](./Automating_Batch_Data_Workloads.md)**
### 💼 **Real-World Use Case: Automating Daily ETL Pipelines**
**Scenario:** A **financial institution** processes **daily transaction logs** from multiple sources and loads them into BigQuery for **fraud detection analysis**.

✅ **GCP Implementation:**
- **Cloud Storage:** Stores raw transaction data.
- **Cloud Dataflow:** Transforms and loads data.
- **BigQuery:** Stores and analyzes processed data.
- **Cloud Composer:** Schedules the ETL workflow.

#### **Python Example: Dataflow Pipeline for Batch Processing**
```python
import apache_beam as beam
from apache_beam.options.pipeline_options import PipelineOptions

options = PipelineOptions(
    runner='DataflowRunner',
    project='finance-etl',
    temp_location='gs://finance-temp-bucket/temp',
    region='us-central1'
)

with beam.Pipeline(options=options) as pipeline:
    transactions = pipeline | 'Read CSV' >> beam.io.ReadFromText('gs://finance-bucket/transactions.csv')
    cleaned = transactions | 'Clean Data' >> beam.Map(lambda x: x.strip().split(','))
    cleaned | 'Write to BigQuery' >> beam.io.WriteToBigQuery('finance.analytics.transactions')
```

#### **Java Example: Dataflow Batch Pipeline**
```java
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.values.TypeDescriptors;

public class BatchDataflow {
    public static void main(String[] args) {
        Pipeline pipeline = Pipeline.create();
        
        pipeline.apply("Read CSV", TextIO.read().from("gs://finance-bucket/transactions.csv"))
                .apply("Clean Data", MapElements.into(TypeDescriptors.strings()).via(line -> line.trim()))
                .apply("Write to BigQuery", TextIO.write().to("finance.analytics.transactions"));
        
        pipeline.run().waitUntilFinish();
    }
}
```

#### **GCP CLI Command: Run Batch Dataflow Job**
```sh
gcloud dataflow jobs run batch-etl-job \
    --gcs-location gs://dataflow-templates/latest/GCS_Text_to_BigQuery \
    --parameters inputFile=gs://finance-bucket/transactions.csv,outputTable=finance.analytics.transactions
```

---

## 2️⃣ **Automating Real-Time Data Workloads**
### 💼 **Real-World Use Case: Real-Time Clickstream Processing**
**Scenario:** An **e-commerce company** wants to process **user clickstream data** in real-time to **analyze customer behavior**.

✅ **GCP Implementation:**
- **Cloud Pub/Sub:** Streams website events.
- **Cloud Dataflow:** Processes and enriches data.
- **BigQuery Streaming:** Stores structured analytics data.

#### **Python Example: Streaming Dataflow Pipeline**
```python
import apache_beam as beam
from apache_beam.options.pipeline_options import PipelineOptions

options = PipelineOptions(
    streaming=True,
    runner='DataflowRunner',
    project='ecommerce-analytics',
    temp_location='gs://ecommerce-temp-bucket/temp',
    region='us-central1'
)

with beam.Pipeline(options=options) as pipeline:
    events = pipeline | 'Read from Pub/Sub' >> beam.io.ReadFromPubSub(topic='projects/ecommerce/topics/clickstream')
    parsed = events | 'Parse JSON' >> beam.Map(lambda x: eval(x.decode('utf-8')))
    parsed | 'Write to BigQuery' >> beam.io.WriteToBigQuery('ecommerce.analytics.clickstream')
```

#### **Java Example: Streaming Dataflow Pipeline**
```java
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;

public class StreamingDataflow {
    public static void main(String[] args) {
        Pipeline pipeline = Pipeline.create();
        
        pipeline.apply("Read Pub/Sub", PubsubIO.readStrings().fromTopic("projects/ecommerce/topics/clickstream"))
                .apply("Write to BigQuery", BigQueryIO.writeTableRows().to("ecommerce.analytics.clickstream"));
        
        pipeline.run().waitUntilFinish();
    }
}
```

#### **GCP CLI Command: Run Streaming Dataflow Job**
```sh
gcloud dataflow jobs run streaming-clickstream \
    --gcs-location gs://dataflow-templates/latest/PubSub_to_BigQuery \
    --parameters inputTopic=projects/ecommerce/topics/clickstream,outputTable=ecommerce.analytics.clickstream
```

---

## 📌 Conclusion
🔹 **Batch Processing:** Use Cloud Storage, Dataflow, and BigQuery for ETL jobs.
🔹 **Real-Time Processing:** Use Pub/Sub, Dataflow, and BigQuery for continuous analytics.
🔹 **Automate Pipelines:** Use Cloud Composer and Workflows to manage and schedule processes.
