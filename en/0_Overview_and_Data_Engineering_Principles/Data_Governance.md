# 📌 Data Governance & Data Access Policies

## 🔹 Overview
Data governance ensures that data is **secure, compliant, and managed effectively**. Organizations must implement policies to control **data access, security, and regulatory compliance**. This document outlines **least privilege access principles**, how to enforce them in **Google Cloud Platform (GCP)**, and real-world examples of compliance, with **Python, Java, and GCP CLI** commands.

---

## 1️⃣ **Least Privilege Access Control**
### 📌 What is Least Privilege Access?
The **Principle of Least Privilege (PoLP)** states that **users and services should have the minimum level of access necessary to perform their job functions**.

### 🔹 **Best Practices:**
- **Use IAM roles effectively:** Assign predefined or custom roles rather than granting broad permissions.
- **Avoid granting `Owner` or `Editor` roles broadly:** Use granular **Viewer**, **Reader**, and **Writer** roles.
- **Use Attribute-Based Access Control (ABAC):** Control access based on metadata tags and conditions.
- **Enable auditing and logging:** Monitor access using **Cloud Audit Logs** and **Cloud Monitoring**.

### 💼 **Real-World Use Case: Financial Institution Compliance**
A **bank** processes sensitive customer transactions and needs to restrict access to customer data.

✅ **GCP Implementation:**
- **IAM Roles:** Assign predefined `roles/bigquery.dataViewer` to analysts.
- **VPC Service Controls:** Restrict data access across projects.
- **Cloud Audit Logs:** Monitor access attempts.

#### **Python Example: Assigning IAM Role**
```python
from google.cloud import iam_v1

client = iam_v1.IAMClient()
policy = client.get_iam_policy("projects/banking-analytics")
policy.bindings.append({
    "role": "roles/bigquery.dataViewer",
    "members": ["user:analyst@bank.com"]
})
client.set_iam_policy("projects/banking-analytics", policy)
print("IAM policy updated.")
```

#### **Java Example: Assigning IAM Role**
```java
import com.google.cloud.resourcemanager.Policy;
import com.google.cloud.resourcemanager.ResourceManager;
import com.google.cloud.resourcemanager.ResourceManagerOptions;

public class IAMPolicyExample {
    public static void main(String[] args) {
        ResourceManager resourceManager = ResourceManagerOptions.getDefaultInstance().getService();
        Policy policy = resourceManager.getPolicy("banking-analytics");
        policy.toBuilder().addBinding("roles/bigquery.dataViewer", "user:analyst@bank.com").build();
        resourceManager.setPolicy("banking-analytics", policy);
        System.out.println("IAM policy updated.");
    }
}
```

#### **GCP CLI Command:**
```sh
gcloud projects add-iam-policy-binding banking-analytics \
    --member="user:analyst@bank.com" \
    --role="roles/bigquery.dataViewer"
```

---

## 2️⃣ **Data Security Measures**
### 🔹 **Key Practices:**
- **Encryption:** Ensure all data is encrypted at rest and in transit.
- **Data Loss Prevention (DLP):** Use Cloud DLP to mask sensitive information.
- **Access Context Manager:** Set context-based access policies.

### 💼 **Real-World Use Case: Healthcare Compliance (HIPAA)**
A **hospital network** stores patient data and must comply with **HIPAA regulations**.

✅ **GCP Implementation:**
- **Cloud KMS:** Encrypt patient records.
- **Cloud DLP:** Automatically redact PII before storage.
- **VPC Service Controls:** Limit access to sensitive datasets.

#### **Python Example: Masking Sensitive Data with Cloud DLP**
```python
from google.cloud import dlp_v2

dlp = dlp_v2.DlpServiceClient()
item = {"value": "Patient SSN: 123-45-6789"}

response = dlp.deidentify_content(
    parent="projects/hospital-security",
    item=item,
    deidentify_config={"info_type_transformations": {"transformations": [{
        "primitive_transformation": {"replace_with_info_type_config": {}}
    }]}}
)

print(response.item.value)  # Output: "Patient SSN: [REDACTED]"
```

#### **Java Example: Masking Sensitive Data with Cloud DLP**
```java
import com.google.cloud.dlp.v2.DlpServiceClient;
import com.google.privacy.dlp.v2.ContentItem;
import com.google.privacy.dlp.v2.DeidentifyConfig;
import com.google.privacy.dlp.v2.InfoTypeTransformation;
import com.google.privacy.dlp.v2.ReplaceWithInfoTypeConfig;

public class DlpExample {
    public static void main(String[] args) throws Exception {
        try (DlpServiceClient dlp = DlpServiceClient.create()) {
            ContentItem item = ContentItem.newBuilder().setValue("Patient SSN: 123-45-6789").build();
            DeidentifyConfig config = DeidentifyConfig.newBuilder()
                .setInfoTypeTransformations(InfoTypeTransformation.newBuilder()
                .setPrimitiveTransformation(ReplaceWithInfoTypeConfig.getDefaultInstance()).build())
                .build();
            ContentItem response = dlp.deidentifyContent("hospital-security", item, config);
            System.out.println(response.getValue());
        }
    }
}
```

#### **GCP CLI Command:**
```sh
gcloud dlp deidentify-content \
    --project=hospital-security \
    --content-file=input.txt \
    --info-types=PERSON_NAME,EMAIL_ADDRESS,PHONE_NUMBER \
    --replacement="[REDACTED]"
```

---

## 3️⃣ **Regulatory Compliance & Auditing**
### 🔹 **Key Compliance Standards:**
| Compliance Framework | Industry |
|----------------------|----------|
| **HIPAA** | Healthcare |
| **GDPR** | Data Privacy (EU) |
| **CCPA** | Consumer Protection (California) |
| **SOX** | Financial Reporting |
| **PCI-DSS** | Credit Card Transactions |

### 💼 **Real-World Use Case: GDPR Compliance for E-Commerce**
An **e-commerce company** serving European customers must comply with **GDPR**.

✅ **GCP Implementation:**
- **Cloud Logging:** Track access to customer data.
- **IAM Policies:** Restrict access to personal data.
- **Cloud DLP:** Anonymize user data for analytics.

#### **GCP CLI Command to Enable Audit Logs:**
```sh
gcloud logging sinks create my-audit-logs \
    --log-filter="logName=projects/ecommerce/logs/access_logs" \
    --destination=storage.googleapis.com/my-audit-logs-bucket
```

---

## 📌 Conclusion
🔹 **Least Privilege Access** ensures only necessary permissions are granted.
🔹 **Data Security** protects against breaches using encryption and masking.
🔹 **Compliance Policies** must align with industry standards like GDPR, HIPAA, PCI-DSS.

