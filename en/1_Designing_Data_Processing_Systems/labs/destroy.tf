provider "google" {
  credentials = file("gcp_credentials.json")
  region      = "us-central1"
}

locals {
  developers = jsondecode(file("${path.module}/developers.json"))
}

# Destroy GCP Projects
resource "google_project" "sandbox" {
  for_each = { for dev in local.developers : dev.email => dev }

  name       = "sandbox-${each.value.email}"
  project_id = "sandbox-${replace(each.value.email, "/[@.]/", "-")}"
  org_id     = "your-gcp-org-id"
  billing_account = "your-billing-account-id"

  lifecycle {
    prevent_destroy = false
  }

  # Mark for deletion
  delete_default_network = true
}

# Remove IAM Roles from Developers
resource "google_project_iam_member" "remove_developer_role" {
  for_each = google_project.sandbox

  project = each.value.project_id
  role    = "roles/editor"
  member  = "user:${each.key}"
}

# Remove Developer Passwords from Secret Manager
resource "google_secret_manager_secret" "remove_passwords" {
  for_each = google_project.sandbox

  secret_id = "sandbox-password-${replace(each.key, "/[@.]/", "-")}"
}

# Destroy Compute Instances
resource "google_compute_instance" "sandbox_vm" {
  for_each = google_project.sandbox

  name         = "sandbox-vm-${replace(each.key, "/[@.]/", "-")}"
  machine_type = "e2-micro"
  zone         = "us-central1-a"

  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-11"
    }
  }

  network_interface {
    network = "default"
    access_config {}
  }

  metadata = {
    ssh-keys = "${each.key}:${file("~/.ssh/id_rsa.pub")}"
  }
}
