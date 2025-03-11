
### **Store POS Stream Publisher README.md**

```markdown
# Store Point of Sale Stream Publisher

This project is a Java-based application that simulates **Point of Sale (POS) transactions** for multiple stores and publishes the data in real time to a **Google Cloud Pub/Sub topic**. The application is built using Maven and leverages **Google Cloud Pub/Sub** for messaging, **Gson** for JSON serialization, and **SLF4J** with **Logback** for logging.

---

## **Project Overview**

The application generates and streams JSON-formatted POS transactions to a Pub/Sub topic. Each transaction includes sales or returns for a random set of products, and customer purchases are tracked using loyalty IDs for future segmentation and marketing analysis.

### **Features**
- **Real-Time Data Streaming**:
  - Publishes 600 transactions per minute (100 per store for 5 stores).
- **Unique Transaction ID**:
  - Each store has a unique transaction ID counter for traceability.
- **Product Array**:
  - Each transaction includes an array of purchased or returned products, with details such as `product_id`, `quantity`, and `price`.
- **Customer Tracking**:
  - Transactions are associated with a `loyalty_id` to link purchases to customers.
- **Enhanced Logging**:
  - Uses **SLF4J** for structured and configurable logging at different levels (INFO, DEBUG, ERROR).

### **Technologies Used**
- **Java 8+**
- **Maven** for dependency management and build automation.
- **Google Cloud Pub/Sub** for real-time messaging.
- **Gson** for JSON serialization.
- **SLF4J with Logback** for logging.

---

## **Getting Started**

### **Prerequisites**
1. **Google Cloud Project**:
   - Ensure you have access to a Google Cloud project with Pub/Sub enabled.
   - Create a Pub/Sub topic named `pos-transactions`.

   ```bash
   gcloud pubsub topics create pos-transactions
   ```

2. **Google Cloud SDK**:
   - Install and authenticate the [Google Cloud SDK](https://cloud.google.com/sdk/docs/install).

   ```bash
   gcloud auth application-default login
   ```

3. **Service Account**:
   - Create and download a service account key with Pub/Sub permissions.
   - Set the `GOOGLE_APPLICATION_CREDENTIALS` environment variable to point to the key file:

   ```bash
   export GOOGLE_APPLICATION_CREDENTIALS="/path/to/key.json"
   ```

4. **Java and Maven**:
   - Install [Java 17 or higher](https://openjdk.org/projects/jdk/)
   - Install [Maven](https://maven.apache.org/install.html).

---

### **Project Structure**
```plaintext
storePointOfSaleStreamPublisher/
├── pom.xml                    # Maven configuration file
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/example/store/pos/stream/
│   │           └── PublishPOSDataStream.java  # Main application
│   └── test/
│       └── java/
│           └── com/example/store/pos/stream/
│               └── AppTest.java  # Placeholder for tests
```

---

## **How to Run**

### **1. Build the Project**
Run the following command to build the project and download dependencies:
```bash
mvn clean install
```

### **2. Run the Application**
Execute the `PublishPOSDataStream` class using the Maven Exec plugin:
```bash
mvn exec:java -Dexec.mainClass="com.example.store.pos.stream.PublishPOSDataStream"
```

---

## **Dependencies**

The project requires the following dependencies, specified in `pom.xml`:

### **Google Cloud Pub/Sub**
```xml
<dependency>
    <groupId>com.google.cloud</groupId>
    <artifactId>google-cloud-pubsub</artifactId>
    <version>1.117.1</version>
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

### **SLF4J API and Logback**
```xml
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>1.7.36</version>
</dependency>
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.2.11</version>
</dependency>
```

---

## **Sample JSON Transaction**

Each transaction published to Pub/Sub will follow this format:

```json
{
  "store_id": "store_1",
  "transaction_id": "store_1_txn_42",
  "transaction_type": "sale",
  "loyalty_id": "loyalty_123",
  "products": [
    {
      "product_id": "prod_101",
      "quantity": 2,
      "price": 20.50
    },
    {
      "product_id": "prod_103",
      "quantity": 1,
      "price": 35.00
    }
  ],
  "total_amount": 76.00,
  "timestamp": "2025-01-16T10:30:00Z"
}
```

---

## **Logging Levels**

- **INFO**: High-level updates such as transaction publishing progress and system initialization.
- **DEBUG**: Detailed logs for each published transaction.
- **ERROR**: Logs errors and exceptions during message publishing or system shutdown.

---
