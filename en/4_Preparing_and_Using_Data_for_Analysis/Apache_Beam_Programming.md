# 📌 Apache Beam Programming Guide

## 🔹 Overview
Apache Beam is a powerful **unified data processing framework** that enables developers to **process batch and streaming data pipelines** efficiently. It provides **language SDKs for Java and Python**, offering a flexible API to implement transformations, aggregations, and custom business logic.

This guide will cover:
- **Core Apache Beam concepts**
- **Pipeline structure**
- **Using built-in and custom transforms**
- **Aggregating data (averages, standard deviations, etc.)**
- **Handling batch and streaming data with windowing**

---

## 🔹 Apache Beam Core Concepts
Apache Beam follows a **Pipeline model** consisting of:
1. **Pipeline:** The main execution framework.
2. **PCollections:** Distributed datasets in Beam.
3. **Transforms:** The operations applied to PCollections.
4. **I/O Sources & Sinks:** Read and write operations.

---

## 🔹 Example: Processing IoT Sensor Data in Apache Beam
### 💼 **Real-World Use Case: Sensor Data Analytics**
**Scenario:** A **smart city project** collects **IoT temperature sensor data**. The system must:
- **Calculate the average temperature per device over a sliding window**.
- **Detect anomalies based on standard deviation thresholds**.

✅ **GCP Implementation:**
- **Cloud Pub/Sub:** Streams sensor data.
- **Cloud Dataflow:** Runs the Beam pipeline.
- **BigQuery:** Stores processed data.

---

## 🛠 **Building Apache Beam Pipelines**
### **1️⃣ Setting Up a Basic Pipeline**
### **Python Example: Reading and Writing Data**
```python
import apache_beam as beam
from apache_beam.options.pipeline_options import PipelineOptions

# Define pipeline options
options = PipelineOptions(
    runner='DataflowRunner',
    project='smart-city',
    temp_location='gs://smart-city-temp/temp',
    region='us-central1'
)

# Define the pipeline
with beam.Pipeline(options=options) as pipeline:
    (
        pipeline
        | 'Read from Pub/Sub' >> beam.io.ReadFromPubSub(topic='projects/smart-city/topics/sensors')
        | 'Parse JSON' >> beam.Map(lambda x: eval(x.decode('utf-8')))
        | 'Write to BigQuery' >> beam.io.WriteToBigQuery('smart_city.analytics.sensors')
    )
```

### **Java Example: Reading and Writing Data**
```java
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;

public class SensorDataPipeline {
    public static void main(String[] args) {
        Pipeline pipeline = Pipeline.create();
        
        pipeline.apply("Read from Pub/Sub", PubsubIO.readStrings().fromTopic("projects/smart-city/topics/sensors"))
                .apply("Write to BigQuery", BigQueryIO.writeTableRows().to("smart_city.analytics.sensors"));

        pipeline.run().waitUntilFinish();
    }
}
```

---

## 🔹 **2️⃣ Aggregating Data with Apache Beam**
Aggregation is essential for **data analytics and summarization**. Apache Beam supports **built-in** and **custom aggregations**.

### **Calculating Average Sensor Temperature in Python**
```python
class ComputeMeanTemperature(beam.DoFn):
    def process(self, element):
        device_id, readings = element
        avg_temp = sum(readings) / len(readings)
        yield {'device_id': device_id, 'avg_temperature': avg_temp}

with beam.Pipeline(options=options) as pipeline:
    (
        pipeline
        | 'Read Sensor Data' >> beam.io.ReadFromPubSub(topic='projects/smart-city/topics/sensors')
        | 'Parse JSON' >> beam.Map(lambda x: eval(x.decode('utf-8')))
        | 'Group by Device' >> beam.GroupByKey()
        | 'Compute Average Temp' >> beam.ParDo(ComputeMeanTemperature())
        | 'Write to BigQuery' >> beam.io.WriteToBigQuery('smart_city.analytics.sensor_averages')
    )
```

### **Calculating Average Sensor Temperature in Java**
```java
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.values.KV;
import java.util.List;

public class ComputeMeanTemperature extends DoFn<KV<String, Iterable<Double>>, KV<String, Double>> {
    @ProcessElement
    public void processElement(ProcessContext c) {
        String deviceId = c.element().getKey();
        Iterable<Double> readings = c.element().getValue();
        double sum = 0;
        int count = 0;
        for (Double temp : readings) {
            sum += temp;
            count++;
        }
        double avg = sum / count;
        c.output(KV.of(deviceId, avg));
    }
}
```

---

## 🔹 **3️⃣ Implementing Windowing for Real-Time Data**
### **Sliding Window for Streaming Data in Python**
```python
import apache_beam as beam
from apache_beam.transforms.window import SlidingWindows

with beam.Pipeline(options=options) as pipeline:
    (
        pipeline
        | 'Read Pub/Sub' >> beam.io.ReadFromPubSub(topic='projects/smart-city/topics/sensors')
        | 'Apply Sliding Window' >> beam.WindowInto(SlidingWindows(60, 30))
        | 'Compute Mean Temperature' >> beam.CombinePerKey(beam.combiners.MeanCombineFn())
        | 'Write to BigQuery' >> beam.io.WriteToBigQuery('smart_city.analytics.sensor_averages')
    )
```

---

## 🔹 **4️⃣ Custom Transform: Calculating Standard Deviation**
### **Standard Deviation Calculation in Python**
```python
import math

class ComputeStandardDeviation(beam.DoFn):
    def process(self, element):
        device_id, readings = element
        mean = sum(readings) / len(readings)
        variance = sum((x - mean) ** 2 for x in readings) / len(readings)
        std_dev = math.sqrt(variance)
        yield {'device_id': device_id, 'std_dev': std_dev}

with beam.Pipeline(options=options) as pipeline:
    (
        pipeline
        | 'Read Sensor Data' >> beam.io.ReadFromPubSub(topic='projects/smart-city/topics/sensors')
        | 'Group by Device' >> beam.GroupByKey()
        | 'Compute Standard Deviation' >> beam.ParDo(ComputeStandardDeviation())
        | 'Write to BigQuery' >> beam.io.WriteToBigQuery('smart_city.analytics.sensor_variability')
    )
```

---

## 🔹 **5️⃣ Joining Data from Multiple Sources**
Apache Beam supports **joining data from multiple sources** using **CoGroupByKey()**.

### **Example: Joining IoT Sensor Data with Weather API Data**
```python
joined_data = ({'sensors': sensor_pcoll, 'weather': weather_pcoll}
    | 'Join Data' >> beam.CoGroupByKey())
```

---

## 🔹 **6️⃣ Best Practices for Apache Beam**
✅ **Optimize Performance:**
- Use **CombinePerKey()** for aggregations instead of **GroupByKey()** to reduce shuffle.
- Prefer **Fixed Windows** over **Global Windows** for efficiency in streaming.

✅ **Use Correct I/O Sources:**
- **Pub/Sub** for streaming events.
- **Cloud Storage** for batch processing.
- **BigQuery** for structured analytics.

✅ **Leverage Dataflow Autoscaling:**
- Use **Streaming Engine** for lower latency.
- Enable **Horizontal Autoscaling** for handling variable workloads.

---

## 📌 **Conclusion**
Apache Beam is a **powerful framework** for building **batch and streaming pipelines** with **Python and Java**. This guide covered:
- **Building Apache Beam Pipelines**
- **Aggregations (Averages, Standard Deviations)**
- **Custom Transforms**
- **Windowing and Streaming Processing**
- **Joining Data Sources**
- **Best Practices**

🚀 **By mastering these techniques, you'll be well-prepared for Apache Beam development on GCP Dataflow!**
