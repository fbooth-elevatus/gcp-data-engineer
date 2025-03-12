# TOGAF Part 1 Practice Tests - Docker Setup

This guide provides instructions to run the **TOGAF Part 1 Practice Test** using Docker.

## **Prerequisites**
- Install **Docker** ([Download Docker](https://www.docker.com/get-started))
- Ensure the following files are in the `Practice-Tests` directory:
  - `index.html` (Main quiz page)
  - `questions.json` (Quiz questions and answers)
  - `Dockerfile` (Configuration for running the quiz with Nginx)

## **Build and Run the Docker Container**

1. **Navigate to the `Practice-Tests` folder** in your terminal:
   ```sh
   cd /path/to/togaf_ea_studyguide/Practice-Tests
   ```

2. **Build the Docker image**:
   ```sh
   docker build -t gcp-data-quiz .
   ```

3. **Run the container**:
   ```sh
   docker run -d -p 8080:80 togaf-quiz
   ```

4. **Access the TOGAF quiz in your browser**:
   ```
   http://localhost:8080/
   ```

## **Stopping and Removing the Container**
To stop the running container:
```sh
   docker ps  # Find the container ID
   docker stop <container_id>
```

To remove the container:
```sh
   docker rm <container_id>
```

## **Troubleshooting**
- **Port 8080 is already in use?** Try changing the port mapping:
  ```sh
  docker run -d -p 9090:80 togaf-quiz
  ```
  Then access the quiz at: `http://localhost:9090/`

- **Docker build fails due to permissions?** Try running with `sudo`:
  ```sh
  sudo docker build -t togaf-quiz .
  sudo docker run -d -p 8080:80 togaf-quiz
  ```

## **Next Steps**
- Add more questions to `questions.json` for extended testing.
- Customize `index.html` for improved UI/UX.
- Deploy this container to a cloud-based service (AWS, Azure, or GCP) for remote access.

