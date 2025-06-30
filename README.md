# 🍎 Sporty Group - Backend Assignment

A Spring Boot microservice that tracks live sports events, polls an external API periodically, and publishes event updates to a Kafka topic.

---

## 📆 Features

- `POST /events/status` REST API to toggle event live status.
- For each live event:
    - Polls external REST API every 10 seconds.
    - Transforms the response and publishes to Kafka.
    - Retries failed publishing attempts and forwards to DLQ after retries are exhausted.
- Includes detailed logging, exception handling, and observability.

---

## ⚙️ Setup & Run Instructions

### 🛠 Prerequisites

- Docker and Docker Compose installed

### 🚀 Steps to Run

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/event-tracker.git
   cd event-tracker
   ```

2. **Build the JAR file**:
   ```bash
   mvn clean install
   ```

3. **Start the entire stack (Kafka + Zookeeper + event-tracker Service)**:
   ```bash
   docker-compose up --build
   ```

4. **Send an event status update**:
   ```bash
   curl -X POST http://localhost:8080/events/status \
     -H "Content-Type: application/json" \
     -d '{"eventId": "1234", "live": true}'
   ```

---

## 🧲 How to Run Tests

Run all tests using:
```bash
mvn test
```

Test coverage includes:
- Status endpoint input validation
- Scheduled polling tasks and stopping logic
- Kafka publishing success & retry
- DLQ fallback via `@Recover`
- Exception recovery and logging

---

## 💡 Design Decisions

| Component             | Choice                                             |
|-----------------------|----------------------------------------------------|
| Framework             | Spring Boot                                        |
| Scheduling            | `ScheduledExecutorService` for per-event control   |
| Kafka Integration     | Spring Kafka + KafkaTemplate + Spring Retry        |
| External API Client   | `RestTemplate` with configurable base URL          |
| Error Handling        | `@ControllerAdvice` for centralized exception handling |
| DLQ                   | Separate topic + custom publisher for failures     |
| Retry                 | Spring Retry with `@Retryable` and `@Recover`      |
| Mock API              | Separate Controller for external mock API)         |
| Deployment            | Managed via `docker-compose.yml` for local dev     |

---

## 🤖 AI-Assisted Components

| Tool           | Usage                                                            |
| GitHub Copilot | Assisted in writing repeated code structures and annotations     |

### ✅ Verification

- All AI-generated code reviewed and enhanced for clarity and correctness
- Custom exceptions added for service-specific error granularity
- DLQ tested via unit + integration layers
- Retry mechanism verified by simulating Kafka failures

---

## 📬 Contact

For issues or contributions, please open a GitHub issue or contact `akshay.saroj@klarna.com`.
