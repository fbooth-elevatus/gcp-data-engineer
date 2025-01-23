### **README.md**

```markdown
# Dataflow Point of Sale Analytics

This project is a **Google Cloud Dataflow** pipeline, built using **Apache Beam**, to analyze real-time **Point of Sale (POS) transactions** streamed from a **Pub/Sub topic**. The pipeline demonstrates how to calculate:

1. **Total sales per minute by store.**
2. **Average items per order per store.**

---

## **Project Overview**

This pipeline reads JSON-formatted transaction data from the `pos-transactions` Pub/Sub topic, processes it in fixed time windows, and calculates key metrics for analytics. The processed data can be logged or written to a data sink like BigQuery or Cloud Storage.

### **Features**
- **Real-Time Data Analytics**:
  - Calculates total sales per minute by store.
  - Tracks average items per order per store.
- **Scalable**:
  - Designed to run on **Google Cloud Dataflow** for large-scale data processing.
- **Extensible**:
  - Can be extended to include more metrics or custom sinks (e.g., BigQuery, Pub/Sub).

---

## **Technologies Used**
- **Apache Beam**: Unified programming model for batch and stream processing.
- **Google Cloud Dataflow**: Managed runner for Apache Beam pipelines.
- **Google Cloud Pub/Sub**: Message ingestion.
- **Gson**: JSON parsing.
- **Java 8+**

---

## **Setup and Prerequisites**

### **1. Google Cloud Setup**
1. **Enable APIs**:
   - Enable the following APIs in your Google Cloud project:
     - Pub/Sub API
     - Dataflow API
   ```bash
   gcloud services enable pubsub.googleapis.com dataflow.googleapis.com
   ```

2. **Create Pub/Sub Topic**:
   - Create the `pos-transactions` topic:
     ```bash
     gcloud pubsub topics create pos-transactions
     ```

3. **Create a Google Cloud Storage Bucket**:
   - Required for Dataflow temporary files:
     ```bash
     gsutil mb -l <region> gs://<your-bucket-name>/
     ```

4. **Set Up Authentication**:
   - Authenticate with Google Cloud:
     ```bash
     gcloud auth application-default login
     ```

### **2. Java and Maven**
1. Install **Java 8+**:
   - Verify installation:
     ```bash
     java -version
     ```

2. Install **Maven**:
   - Verify installation:
     ```bash
     mvn -v
     ```

---

## **Project Structure**
```plaintext
dataflowPointOfSaleAnalytics/
├── pom.xml                    # Maven project descriptor
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/example/store/pos/dataflow/
│   │           └── PosTransactionAnalytics.java  # Main pipeline
│   └── test/
│       └── java/
│           └── com/example/store/pos/dataflow/
│               └── AppTest.java  # Placeholder for tests
```

---

## **How to Build and Run**

### **1. Build the Project**
Run the following command to build the project and create a fat JAR:
```bash
mvn clean package
```

### **2. Run Locally**
Test the pipeline locally using the **DirectRunner**:
```bash
java -cp target/dataflowPointOfSaleAnalytics-1.0-SNAPSHOT-shaded.jar com.example.store.pos.dataflow.PosTransactionAnalytics
```

### **3. Deploy to Dataflow**
Deploy the pipeline to Google Cloud Dataflow:
```bash
java -cp target/dataflowPointOfSaleAnalytics-1.0-SNAPSHOT-shaded.jar com.example.store.pos.dataflow.PosTransactionAnalytics \
    --runner=DataflowRunner \
    --project=<your-project-id> \
    --region=<your-region> \
    --tempLocation=gs://<your-bucket-name>/tmp/
```

---

## **Pipeline Logic**

### **Input Source**
- Reads messages from the `pos-transactions` Pub/Sub topic.

### **Processing**
1. **Windowing**:
   - Uses fixed windows of 1 minute to group transactions by time.
2. **Metrics Calculation**:
   - **Total Sales per Store**:
     - Aggregates the `totalAmount` field by `storeId`.
   - **Average Items per Order**:
     - Calculates the average `numItems` per transaction by `storeId`.

### **Output**
- Logs the calculated metrics for demonstration purposes.
- Metrics can be extended to write to sinks like BigQuery.

---

## **Sample Input**
Each message in Pub/Sub is a JSON-formatted transaction:
```json
{
  "storeId": "store_1",
  "totalAmount": 76.50,
  "numItems": 3
}
```

---

## **Sample Output**
The pipeline outputs aggregated metrics, such as:
```plaintext
Total Sales: store_1 = 1245.67
Total Sales: store_2 = 875.45
Average Items per Order: store_1 = 3.2
Average Items per Order: store_2 = 2.8
```

---

## **Extending the Pipeline**
- **Additional Metrics**:
  - Add more metrics, such as the number of transactions per store.
- **Data Sinks**:
  - Write results to BigQuery, Cloud Storage, or Pub/Sub.
- **Error Handling**:
  - Implement dead-letter queues for failed messages.

---

## **Dependencies**
### **Apache Beam**
```xml
<dependency>
    <groupId>org.apache.beam</groupId>
    <artifactId>beam-runners-google-cloud-dataflow-java</artifactId>
    <version>2.48.0</version>
</dependency>
<dependency>
    <groupId>org.apache.beam</groupId>
    <artifactId>beam-sdks-java-core</artifactId>
    <version>2.48.0</version>
</dependency>
```

### **Gson**
```xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.8.9</version>
</dependency>
```