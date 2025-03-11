# Designing Data Processing Systems

## Overview
Designing data processing systems is a core competency for a **Google Cloud Professional Data Engineer**. This section focuses on building secure, scalable, and efficient data pipelines while ensuring compliance, reliability, and flexibility. Below, we will explore real-world **use cases**, best practices, and **Google Cloud services** commonly used in designing data processing solutions.

---

## 1️⃣ Designing for Security and Compliance
### 🔹 **Key Considerations**
- **Identity and Access Management (IAM):** Ensure least-privilege access to data services.
- **Data Encryption:** Use Cloud KMS for key management.
- **Data Loss Prevention (DLP):** Detect and protect sensitive data.
- **Regional Compliance:** Consider data sovereignty laws (e.g., GDPR, HIPAA).

### 💡 **Real-World Use Case: Protecting Healthcare Data**
**Scenario:** A healthcare company needs to process **patient records** while complying with **HIPAA regulations**.

✅ **Solution:**
- **Use Cloud IAM** to enforce fine-grained access controls.
- **Encrypt sensitive data** at rest using **Cloud Storage CMEK** (Customer-Managed Encryption Keys).
- **Apply Cloud Data Loss Prevention (DLP)** to mask personally identifiable information (PII) before storage.

#### **Python Example:**
```python
from google.cloud import dlp_v2

dlp = dlp_v2.DlpServiceClient()
project = "my-healthcare-project"
info_types = ["PERSON_NAME", "EMAIL_ADDRESS", "PHONE_NUMBER"]

# Redact PII from patient records
response = dlp.deidentify_content(
    parent=f"projects/{project}",
    item={'value': 'Patient Name: John Doe, Email: johndoe@email.com'},
    deidentify_config={'info_type_transformations': {'transformations': [{'primitive_transformation': {'replace_with_info_type_config': {}}}]}}
)

print(response.item.value)  # Output: "Patient Name: [PERSON_NAME], Email: [EMAIL_ADDRESS]"
```

#### **Java Example:**
```java
import com.google.cloud.dlp.v2.DlpServiceClient;
import com.google.privacy.dlp.v2.ContentItem;
import com.google.privacy.dlp.v2.DeidentifyConfig;
import com.google.privacy.dlp.v2.DlpServiceClient;
import com.google.privacy.dlp.v2.InfoType;
import com.google.privacy.dlp.v2.InfoTypeTransformation;
import com.google.privacy.dlp.v2.ReplaceWithInfoTypeConfig;
import com.google.privacy.dlp.v2.Transformation;

public class DlpExample {
    public static void main(String[] args) throws Exception {
        try (DlpServiceClient dlp = DlpServiceClient.create()) {
            String text = "Patient Name: John Doe, Email: johndoe@email.com";
            ContentItem contentItem = ContentItem.newBuilder().setValue(text).build();
            
            DeidentifyConfig config = DeidentifyConfig.newBuilder()
                .setInfoTypeTransformations(
                    InfoTypeTransformation.newBuilder()
                        .setPrimitiveTransformation(ReplaceWithInfoTypeConfig.getDefaultInstance())
                        .build())
                .build();
            
            ContentItem response = dlp.deidentifyContent("my-healthcare-project", contentItem, config);
            System.out.println(response.getValue());
        }
    }
}
```

---

## 2️⃣ Designing for Reliability and Data Integrity
### 🔹 **Key Considerations**
- **Data Validation:** Ensure data quality using Cloud Dataprep and Dataflow.
- **Monitoring Pipelines:** Use Cloud Logging and Cloud Monitoring.
- **Disaster Recovery:** Implement failover strategies.
- **Transaction Consistency:** Apply ACID principles when necessary.

### 💡 **Real-World Use Case: ETL Pipeline for Financial Transactions**
**Scenario:** A financial institution processes **daily transaction data** from multiple sources.

✅ **Solution:**
- Use **Cloud Dataflow** (Apache Beam) for real-time ETL.
- Enable **checkpointing** in Dataflow to handle failures.
- Store validated data in **BigQuery** for analytics.

#### **Python Example:**
```python
import apache_beam as beam
from apache_beam.options.pipeline_options import PipelineOptions

options = PipelineOptions(
    runner='DataflowRunner',
    project='finance-project',
    temp_location='gs://finance-bucket/temp',
    region='us-central1'
)

with beam.Pipeline(options=options) as pipeline:
    transactions = pipeline | 'Read Transactions' >> beam.io.ReadFromText('gs://finance-bucket/transactions.csv')
    validated = transactions | 'Validate' >> beam.Filter(lambda x: 'error' not in x)
    validated | 'Write to BigQuery' >> beam.io.WriteToBigQuery('finance-project:analytics.transactions')
```

#### **Java Example:**
```java
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.transforms.Filter;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.values.TypeDescriptors;

public class DataflowExample {
    public static void main(String[] args) {
        Pipeline pipeline = Pipeline.create();

        pipeline.apply("Read Transactions", TextIO.read().from("gs://finance-bucket/transactions.csv"))
                .apply("Validate", Filter.by(line -> !line.contains("error")))
                .apply("Write to BigQuery", TextIO.write().to("finance-project:analytics.transactions"));

        pipeline.run().waitUntilFinish();
    }
}
```