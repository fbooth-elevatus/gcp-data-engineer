# Security & Compliance in GCP

## 📌 Overview
Security and compliance are critical components of a data engineering pipeline. Google Cloud Platform (GCP) offers various tools and services to ensure that data is securely managed, accessed, and stored in compliance with regulatory requirements.

This guide will cover key security concepts and best practices required for proficiency in **Security Processes** and success in the **GCP Professional Data Engineer certification**.

---

## 🔐 Apply Least Privilege Access Control
### Identity & Access Management (IAM)
- **Principle of Least Privilege:** Grant only the minimum required permissions to users and services.
- **IAM Roles:**
  - **Primitive roles** (Owner, Editor, Viewer) – Avoid using for security-critical projects.
  - **Predefined roles** – GCP provides granular roles for specific services (e.g., `roles/storage.objectViewer` for Cloud Storage).
  - **Custom roles** – Define specific permissions as per organization policies.
- **Service Accounts:**
  - Use service accounts for applications instead of human users.
  - Assign only necessary permissions to avoid excessive access.
  - Use Workload Identity Federation to authenticate workloads securely.
- **IAM Policy Best Practices:**
  - Follow **deny-by-default** principle.
  - Use **IAM Conditions** to allow access based on attributes (e.g., time, IP address).
  - Regularly review IAM policies with **Cloud Asset Inventory**.

---

## 🔒 Encrypt Data at Rest and in Transit
### Encryption Strategies in GCP
- **Data at Rest Encryption:**
  - Default **Google-managed encryption keys (CMEK)**.
  - **Customer-managed keys (CMEK)** via **Cloud Key Management Service (KMS)** for more control.
  - **Customer-supplied encryption keys (CSEK)** for external control.
- **Data in Transit Encryption:**
  - **TLS (Transport Layer Security)** ensures data integrity.
  - **Mutual TLS (mTLS)** for authentication between services.
  - Use **Cloud Armor** for DDoS protection.
- **Best Practices:**
  - Rotate encryption keys regularly.
  - Store encryption keys separately from data.
  - Use **VPC Service Controls** to prevent unauthorized access.

---

## 🔏 Ensure Data Privacy with Cloud Data Loss Prevention (DLP)
### Cloud DLP Overview
Google Cloud DLP provides tools for detecting, classifying, and redacting sensitive data.
- **Key Features:**
  - **Data classification:** Identifies sensitive data (e.g., PII, credit card numbers, healthcare records).
  - **Data masking:** Replace sensitive values with tokenized or redacted alternatives.
  - **De-identification:** Use pseudonymization techniques like format-preserving encryption.
- **Implementing DLP:**
  - Define **DLP Inspection Rules** to scan Cloud Storage, BigQuery, and Datastore.
  - Configure **DLP templates** to enforce privacy policies across different datasets.
  - Monitor **DLP job execution logs** for compliance.

---

## ⚖️ Comply with Regional Regulations
### Data Sovereignty & Regulatory Compliance
Organizations must comply with **GDPR, HIPAA, PCI-DSS**, and other data privacy laws.

- **GDPR (General Data Protection Regulation):**
  - Enforce **data residency policies** to store EU customer data in Europe.
  - Implement **user consent management** with IAM policies.
- **HIPAA (Health Insurance Portability and Accountability Act):**
  - Store **PHI (Protected Health Information)** securely in **Cloud Healthcare API**.
  - Use **Cloud KMS and Access Transparency** for audit logs.
- **PCI-DSS (Payment Card Industry Data Security Standard):**
  - Enforce **tokenization and encryption** on payment data.
  - Regularly scan for **vulnerabilities with Security Command Center**.

---

## 🏗 Best Practices for Secure Cloud Architecture
- Use **VPC Service Controls** to restrict access across service perimeters.
- Enable **Audit Logging** for all critical services.
- Implement **Cloud Identity-Aware Proxy (IAP)** for Zero Trust security.
- Monitor **Cloud Security Command Center** for threat detection and compliance violations.
- Automate **security monitoring** with **Security Health Analytics** and **Forseti Security**.


