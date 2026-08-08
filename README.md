# Payments - Production Support Lab

## Phase 4

This phase implements the first end-to-end payment flow.

### Architecture

Client -> payment-api -> bank-simulator -> PostgreSQL
                                  |
                                  +-> Kafka -> notification-service

### Services

| Service | Port | Responsibility |
|---|---:|---|
| payment-api | 8080 | Accepts payments, persists status, processes asynchronously |
| bank-simulator | 8081 | Simulates an external bank authorization API |
| notification-service | 8082 | Consumes payment events from Kafka |

### Payment lifecycle

CREATED -> PROCESSING -> SUCCESS / FAILED

### Phase 4 technologies

- Java 21
- Spring Boot 3.5.3
- Spring Web / RestClient
- Spring Data JPA
- PostgreSQL
- HikariCP
- Apache Kafka
- Spring Boot Actuator
- ThreadPoolTaskExecutor
- Docker Compose

### Run locally

Prerequisites:
- JDK 21
- Maven 3.9+
- Docker Desktop

Build the applications:

```bash
mvn clean package
```

Start the infrastructure and services:

```bash
docker compose up --build
```

Create a payment:

```bash
curl -X POST http://localhost:8080/payments \
  -H "Content-Type: application/json" \
  -d '{"amount":25.50}'
```

The response is accepted immediately with a payment id. Query its status:

```bash
curl http://localhost:8080/payments/<PAYMENT_ID>
```

The notification service logs the corresponding Kafka event.

### Monitoring endpoints

Payment API exposes:

- `/actuator/health`
- `/actuator/metrics`
- `/actuator/prometheus`
- `/actuator/threaddump`

### Important learning point

The asynchronous processing uses a separate `PaymentProcessor` bean. This is intentional: calling an `@Async` method through the same Spring bean would bypass the proxy and would not execute asynchronously.

### Next phase

Phase 5 will add Prometheus, Grafana, detailed executor metrics, structured/correlation logging, load testing, and intentional production failure scenarios.
