provider "google" {
  credentials = file("gcp_credentials.json") # Update with your GCP service account JSON key
  region      = "us-central1"
}

locals {
  developers = jsondecode(file("${path.module}/developers.json"))
}

# GCP Organization and Billing Information
variable "org_id" {
  default = "your-gcp-org-id"  # Replace with your GCP Organization ID
}

variable "billing_account" {
  default = "your-billing-account-id" # Replace with your Billing Account ID
}

# Create a GCP Project for each developer
resource "google_project" "sandbox" {
  for_each = { for dev in local.developers : dev.email => dev }

  name       = "sandbox-${each.value.email}"
  project_id = "sandbox-${replace(each.value.email, "/[@.]/", "-")}"
  org_id     = var.org_id
  billing_account = var.billing_account

  lifecycle {
    prevent_destroy = false
  }
}

# Assign IAM Role to Developer
resource "google_project_iam_member" "developer_role" {
  for_each = google_project.sandbox

  project = each.value.project_id
  role    = "roles/editor"
  member  = "user:${each.key}"
}

# Store Developer Passwords in Secret Manager
resource "google_secret_manager_secret" "developer_password" {
  for_each = local.developers

  secret_id = "sandbox-password-${replace(each.value.email, "/[@.]/", "-")}"
  replication {
    automatic = true
  }
}

resource "google_secret_manager_secret_version" "password_version" {
  for_each = google_secret_manager_secret.developer_password

  secret = each.value.secret_id
  secret_data = each.value.password
}

# Create a Free-Tier Compute VM for Each Developer
resource "google_compute_instance" "sandbox_vm" {
  for_each = google_project.sandbox

  name         = "sandbox-vm-${replace(each.key, "/[@.]/", "-")}"
  machine_type = "e2-micro"  # Free tier eligible
  zone         = "us-central1-a"

  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-11"
    }
  }

  network_interface {
    network = "default"
    access_config {
      # Assign external IP for SSH
    }
  }

  metadata = {
    ssh-keys = "${each.key}:${file("~/.ssh/id_rsa.pub")}"
  }
}

# Output Developer Sandbox Details
output "developer_sandboxes" {
  value = {
    for dev in local.developers :
    dev.email => {
      project_id       = google_project.sandbox[dev.email].project_id
      service_account  = "sandbox-sa-${replace(dev.email, "/[@.]/", "-")}@${google_project.sandbox[dev.email].project_id}.iam.gserviceaccount.com"
      compute_instance = google_compute_instance.sandbox_vm[dev.email].name
      external_ip      = google_compute_instance.sandbox_vm[dev.email].network_interface[0].access_config[0].nat_ip
    }
  }
}
