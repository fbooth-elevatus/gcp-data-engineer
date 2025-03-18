# 3️⃣ **Analyzing and Visualizing Data**

## 🔹 Overview
Analyzing and visualizing data is a crucial step in transforming raw data into actionable insights. **Google Cloud Platform (GCP)** provides robust tools to process, analyze, and visualize data efficiently. The right tools depend on data size, frequency, and intended audience.

This guide covers:
- **GCP technologies for data analysis and visualization**
- **Best practices for querying and reporting**
- **Examples in Python, Java, and GCP CLI**

---

## 🔹 **GCP Technologies for Data Analysis and Visualization**

| **Service**         | **Use Case** |
|--------------------|------------------------------------------------|
| **BigQuery**       | Serverless data warehouse for large-scale analytics |
| **Looker Studio**  | Business intelligence (BI) visualization and reporting |
| **Vertex AI**      | Machine learning-powered analytics |
| **Dataproc**       | Managed Hadoop and Spark for large-scale data processing |
| **Dataflow**       | Real-time and batch data transformation and processing |
| **Cloud SQL**      | Relational database with analytical capabilities |
| **Bigtable**       | NoSQL database optimized for high-throughput analytics |
| **Google Sheets**  | Lightweight analysis and visualization for ad-hoc reporting |

---

## 1️⃣ **BigQuery for Data Analysis**

### 💼 **Real-World Use Case: Marketing Campaign Performance Analytics**
**Scenario:** A **digital marketing agency** wants to analyze **advertising campaign performance** using **BigQuery and Looker**.

✅ **GCP Implementation:**
- **BigQuery:** Stores campaign performance data.
- **Looker Studio:** Visualizes key marketing metrics.

### **Python Example: Running an Aggregation Query in BigQuery**
```python
from google.cloud import bigquery

client = bigquery.Client()
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

### **Java Example: Querying Marketing Data in BigQuery**
```java
import com.google.cloud.bigquery.*;

public class BigQueryAnalysis {
    public static void main(String[] args) throws Exception {
        BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
        String query = "SELECT campaign_id, AVG(conversion_rate) as avg_conversion FROM marketing.analytics.campaign_performance " +
                       "GROUP BY campaign_id ORDER BY avg_conversion DESC;";
        TableResult result = bigquery.query(QueryJobConfiguration.newBuilder(query).build());
        
        result.iterateAll().forEach(row ->
            System.out.println("Campaign: " + row.get("campaign_id").getStringValue() +
                               ", Conversion Rate: " + row.get("avg_conversion").getDoubleValue()));
    }
}
```

### **GCP CLI Command: Query Marketing Data in BigQuery**
```sh
gcloud bigquery query --use_legacy_sql=false \
    "SELECT campaign_id, AVG(conversion_rate) as avg_conversion FROM marketing.analytics.campaign_performance GROUP BY campaign_id ORDER BY avg_conversion DESC;"
```

---

## 2️⃣ **Data Visualization with Looker Studio**

### 💼 **Real-World Use Case: Executive Dashboard for Sales Performance**
**Scenario:** A retail company wants to **create an executive dashboard** to monitor real-time **sales trends** and **profitability metrics**.

✅ **GCP Implementation:**
- **BigQuery:** Stores sales data.
- **Looker Studio:** Connects to BigQuery for real-time reporting.

### **Best Practices for Data Visualization**
- **Use interactive dashboards** for real-time analysis.
- **Aggregate data efficiently** in BigQuery before visualizing.
- **Leverage custom calculations** to display KPIs effectively.
- **Optimize queries** to reduce load times in dashboards.

### **Steps to Set Up Looker Studio with BigQuery**
1. Open **Looker Studio** and select **BigQuery** as a data source.
2. Connect to your **GCP project** and select the required dataset.
3. Build **visualizations** using tables, charts, and graphs.
4. Apply **filters and calculated fields** to enhance insights.
5. Share dashboards with stakeholders.

---

## 3️⃣ **Machine Learning-Powered Analysis with Vertex AI**

### 💼 **Real-World Use Case: Customer Churn Prediction**
**Scenario:** A **telecom company** wants to analyze customer behavior and predict **churn probability** based on historical data.

✅ **GCP Implementation:**
- **BigQuery ML:** Runs machine learning models directly in BigQuery.
- **Vertex AI:** Builds and deploys advanced ML models.

### **GCP CLI Command: Train a Churn Prediction Model in BigQuery ML**
```sh
gcloud bq query --use_legacy_sql=false ""
CREATE OR REPLACE MODEL telecom.churn_model
OPTIONS(model_type='logistic_reg') AS
SELECT tenure, monthly_charges, total_charges, churn
FROM telecom.customer_data;
""
```

### **Best Practices for ML-Based Analysis**
- **Use BigQuery ML** for quick model training within structured data.
- **Leverage Vertex AI** for complex AI-driven insights.
- **Automate model deployment** to streamline prediction updates.

---

## 📌 Conclusion
- **BigQuery** provides scalable, cost-effective analytics.
- **Looker Studio** enables interactive data visualization.
- **Vertex AI** allows machine learning-powered insights.
- **Use best practices** for querying, dashboard creation, and ML modeling.

