# 📌 Storing Structured Data in Google Cloud

## 🔹 Overview
Structured data is **organized in tables with defined schemas**. It is typically stored in **relational databases**, **data warehouses**, or **globally distributed databases** for transactional and analytical workloads.

This guide covers:
- **SQL & NoSQL storage options on Google Cloud**.
- **Best practices for data consistency, reliability, and scalability**.
- **Implementation examples using Python, Java, and GCP CLI**.

---

## 🔹 **Google Cloud Structured Data Storage Services**

| **Service**         | **Use Case** |
|--------------------|------------------------------------------------|
| **Cloud SQL**      | Managed relational databases (PostgreSQL, MySQL, SQL Server) |
| **Cloud Spanner**  | Globally distributed, highly consistent relational database |
| **BigQuery**       | Data warehousing, analytics, large-scale queries |
| **Firestore**      | NoSQL, real-time synchronization for web and mobile apps |
| **Bigtable**       | NoSQL, high-throughput storage for analytics and time-series data |

---

## **1️⃣ Storing Transactional Data in Cloud SQL**
### 💼 **Real-World Use Case: E-Commerce Order Processing**
**Scenario:** An **e-commerce company** needs a **managed relational database** for order management.

✅ **GCP Implementation:**
- **Cloud SQL (PostgreSQL)** for relational data.
- **Cloud Functions** to trigger actions upon order placement.

---

### **🔹 Python Example: Writing Data to Cloud SQL (PostgreSQL)**
```python
import psycopg2

db_host = "34.123.45.67"
db_name = "ecommerce"
db_user = "admin"
db_password = "password123"

try:
    conn = psycopg2.connect(
        host=db_host, database=db_name, user=db_user, password=db_password
    )
    cursor = conn.cursor()

    query = "INSERT INTO orders (customer_id, total_amount, status) VALUES (%s, %s, %s)"
    cursor.execute(query, (123, 99.99, 'completed'))

    conn.commit()
    print("Order inserted successfully!")

except Exception as e:
    print("Error:", e)
finally:
    if cursor:
        cursor.close()
    if conn:
        conn.close()
```

---

### **🔹 Java Example: Writing Data to Cloud SQL (PostgreSQL) with Proper Resource Management**
```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CloudSQLWrite {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:postgresql://34.123.45.67:5432/ecommerce";
        String user = "admin";
        String password = "password123";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, user, password);
             PreparedStatement pstmt = conn.prepareStatement(
                 "INSERT INTO orders (customer_id, total_amount, status) VALUES (?, ?, ?)")) {

            pstmt.setInt(1, 123);
            pstmt.setDouble(2, 99.99);
            pstmt.setString(3, "completed");

            pstmt.executeUpdate();
            System.out.println("Order inserted successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

---

### **🔹 GCP CLI: Create a Cloud SQL Database and Table**
```sh
# Create Cloud SQL instance (PostgreSQL)
gcloud sql instances create ecommerce-db --database-version=POSTGRES_13 \
    --tier=db-f1-micro --region=us-central1

# Create a database inside the Cloud SQL instance
gcloud sql databases create ecommerce --instance=ecommerce-db

# Connect to Cloud SQL and create an orders table
gcloud sql connect ecommerce-db --user=postgres

# Inside PostgreSQL shell:
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    customer_id INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL
);
```

---

## **2️⃣ Storing Analytical Data in BigQuery**
### 💼 **Real-World Use Case: Sales Data Warehousing**
**Scenario:** A **retail company** collects transaction logs and needs to analyze sales trends.

✅ **GCP Implementation:**
- **BigQuery** for large-scale analysis.
- **Cloud Storage** for staging CSV files before import.

---

### **🔹 Python Example: Loading Data into BigQuery**
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

---

### **🔹 Java Example: Querying Data from BigQuery**
```java
import com.google.cloud.bigquery.*;

public class BigQueryQuery {
    public static void main(String[] args) {
        BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
        String query = "SELECT COUNT(*) FROM sales WHERE total_amount > 100";

        try {
            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();
            TableResult results = bigquery.query(queryConfig);

            for (FieldValueList row : results.iterateAll()) {
                System.out.println("High-value orders: " + row.get(0).getLongValue());
            }
        } catch (BigQueryException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

---

### **🔹 GCP CLI: Load Data into BigQuery**
```sh
gcloud bigquery load --source_format=CSV --autodetect \
    ecommerce.analytics.sales gs://ecommerce-sales-data/sales.csv
```

---

## **3️⃣ Storing NoSQL Data in Cloud Firestore**
### 💼 **Real-World Use Case: User Profiles for a Social Media App**
**Scenario:** A **social media platform** needs **real-time updates** for user profiles.

✅ **GCP Implementation:**
- **Firestore** for real-time NoSQL storage.
- **Firebase SDK** for web and mobile app integrations.

---

### **🔹 Python Example: Writing Data to Firestore**
```python
from google.cloud import firestore

db = firestore.Client()
doc_ref = db.collection("users").document("user123")
doc_ref.set({
    "name": "Alice",
    "email": "alice@example.com",
    "followers": 500
})

print("User profile saved to Firestore.")
```

---

### **🔹 GCP CLI: Add a Firestore Document**
```sh
gcloud firestore documents create users/user123 \
    --fields="name=Alice,email=alice@example.com,followers=500"
```

---

## 📌 **Conclusion**
🔹 **Transactional Data:** Use **Cloud SQL** for structured data with high consistency.  
🔹 **Analytical Data:** Use **BigQuery** for scalable data warehousing and analysis.  
🔹 **NoSQL Data:** Use **Firestore** for real-time synchronization in applications.  

---

## **✅ Best Practices**
✔ **Use managed services** like Cloud SQL, Spanner, and BigQuery for scalability.  
✔ **Follow least privilege access** with IAM roles for database security.  
✔ **Ensure data durability** with replication and backup strategies.  

---
