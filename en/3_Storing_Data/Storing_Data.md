# 📌 Storing Data in Google Cloud

## 🔹 Overview
Storing data efficiently is a fundamental requirement for **Google Cloud Professional Data Engineers**. Google Cloud Platform (**GCP**) offers **scalable, secure, and cost-effective** storage solutions for different types of data, including structured, semi-structured, and unstructured data.

This guide covers:
- **GCP storage services and their real-world use cases**.
- **Best practices for data storage and retrieval**.
- **Implementation examples using Python, Java, and GCP CLI commands**.

---

## 🔹 **GCP Storage Services and Use Cases**

| **Service**         | **Use Case** |
|--------------------|------------------------------------------------|
| **Cloud Storage**  | Unstructured data, backups, media, ML datasets |
| **BigQuery**       | Data warehousing, analytics, large-scale queries |
| **Cloud SQL**      | Relational databases (PostgreSQL, MySQL, SQL Server) |
| **Cloud Spanner**  | Globally distributed, strongly consistent database |
| **Firestore**      | NoSQL, real-time synchronization, app development |
| **Bigtable**       | High-throughput NoSQL for analytics and time-series data |
| **Dataplex**       | Data governance, metadata management, and lakehouse storage |

---

## 1️⃣ **Storing Unstructured Data**
### 💼 **Real-World Use Case: Media Storage for a Streaming Service**
**Scenario:** A **video streaming platform** needs to store and serve **millions of video files** efficiently.

✅ **GCP Implementation:**
- **Cloud Storage:** Stores raw video files.
- **Cloud CDN:** Speeds up global delivery.
- **Signed URLs:** Secures access to private files.

#### **Python Example: Uploading a File to Cloud Storage**
```python
from google.cloud import storage

client = storage.Client()
bucket = client.bucket("streaming-videos")
blob = bucket.blob("movies/movie1.mp4")
blob.upload_from_filename("movie1.mp4")
print("Upload successful!")
```

#### **Java Example: Uploading a File to Cloud Storage**
```java
import com.google.cloud.storage.*;
import java.nio.file.Paths;

public class UploadFile {
    public static void main(String[] args) throws Exception {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        BlobId blobId = BlobId.of("streaming-videos", "movies/movie1.mp4");
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, Files.readAllBytes(Paths.get("movie1.mp4")));
        System.out.println("Upload successful!");
    }
}
```

#### **GCP CLI Command: Upload File to Cloud Storage**
```sh
gsutil cp movie1.mp4 gs://streaming-videos/movies/
```

---

## 2️⃣ **Storing Structured Data for Analytics**
### 💼 **Real-World Use Case: E-Commerce Sales Data Warehousing**
**Scenario:** A **large online retailer** collects **transaction data** and needs a scalable solution for analytics and reporting.

✅ **GCP Implementation:**
- **BigQuery:** Stores transactional data.
- **Cloud Dataflow:** Transforms raw sales data.
- **Looker Studio:** Provides real-time dashboards.

#### **Python Example: Loading CSV Data into BigQuery**
```python
from google.cloud import bigquery

client = bigquery.Client()
dataset_id = "ecommerce.analytics"
table_id = "sales"
uri = "gs://ecommerce-sales-data/sales.csv"

job_config = bigquery.LoadJobConfig(
    source_format=bigquery.SourceFormat.CSV,
    skip_leading_rows=1,
    autodetect=True,
)

load_job = client.load_table_from_uri(uri, f"{dataset_id}.{table_id}", job_config=job_config)
load_job.result()
print("Sales data successfully loaded into BigQuery.")
```

#### **Java Example: Loading Data into BigQuery**
```java
import com.google.cloud.bigquery.*;

public class BigQueryLoad {
    public static void main(String[] args) throws Exception {
        BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
        String datasetName = "ecommerce.analytics";
        String tableName = "sales";
        String sourceUri = "gs://ecommerce-sales-data/sales.csv";

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

#### **GCP CLI Command: Load Data into BigQuery**
```sh
gcloud bigquery load --source_format=CSV --autodetect \
    ecommerce.analytics.sales gs://ecommerce-sales-data/sales.csv
```

---

## 3️⃣ **Storing NoSQL and Time-Series Data**
### 💼 **Real-World Use Case: IoT Sensor Data Storage**
**Scenario:** A **smart city project** collects IoT sensor data and needs a high-throughput, low-latency storage system for analysis.

✅ **GCP Implementation:**
- **Cloud Bigtable:** Stores time-series sensor data.
- **Cloud Pub/Sub:** Streams real-time IoT data.
- **Cloud Dataflow:** Processes sensor readings.

#### **Python Example: Writing Data to Cloud Bigtable**
```python
from google.cloud import bigtable

client = bigtable.Client(project="smartcity-project")
instance = client.instance("iot-instance")
table = instance.table("sensor_readings")

row_key = "sensor-123"
row = table.row(row_key)
row.set_cell("readings", "temperature", b"22.5")
row.commit()
print("Sensor data written to Bigtable.")
```

#### **Java Example: Writing to Cloud Bigtable**
```java
import com.google.cloud.bigtable.hbase.BigtableConfiguration;
import org.apache.hadoop.hbase.client.*;

public class BigtableWrite {
    public static void main(String[] args) throws Exception {
        Connection connection = BigtableConfiguration.connect("smartcity-project", "iot-instance");
        Table table = connection.getTable(TableName.valueOf("sensor_readings"));
        Put put = new Put("sensor-123".getBytes());
        put.addColumn("readings".getBytes(), "temperature".getBytes(), "22.5".getBytes());
        table.put(put);
        System.out.println("Sensor data written to Bigtable.");
    }
}
```

#### **GCP CLI Command: Create a Bigtable Table**
```sh
gcloud bigtable instances tables create sensor_readings --instance=iot-instance
```

---

## 📌 Conclusion
🔹 **Unstructured Data:** Use Cloud Storage for images, videos, and backups.
🔹 **Structured Data:** Use BigQuery and Cloud SQL for analytical processing.
🔹 **NoSQL & Time-Series Data:** Use Bigtable for high-throughput, low-latency applications.
