# Phase 5 — Observability

Phase 5 adds the first production-support monitoring layer to the payment platform.

## Components

- Spring Boot Actuator + Micrometer Prometheus registry on the three services
- Prometheus scraping `/actuator/prometheus`
- Grafana with a provisioned Prometheus datasource
- A provisioned payment-platform dashboard
- Custom payment outcome and processing-latency metrics
- Payment executor metrics
- HikariCP metrics from Spring Boot/Micrometer
- Initial Prometheus alert rules

## Run

From the repository root:

```bash
mvn clean package
docker compose up -d --build
```

Useful endpoints:

- Payment API: http://localhost:8080
- Bank simulator: http://localhost:8081
- Notification service: http://localhost:8082
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000

The Prometheus targets should become healthy after the application containers start.

## What to inspect first

Payment API metrics:

```text
http://localhost:8080/actuator/prometheus
```

Useful metric families include:

- `jvm_threads_*`
- `executor_*`
- `hikaricp_*`
- `http_server_requests_*`
- `payments_processed_*`
- `payments_processing_*`

## Learning sequence

1. Verify all Prometheus targets are UP.
2. Open the provisioned Grafana dashboard.
3. Generate normal payment traffic.
4. Observe JVM threads, executor activity, queue depth, Hikari connections and payment latency.
5. Phase 5 follow-up will add controlled incident labs: slow bank, executor exhaustion, HikariCP exhaustion, Kafka lag/failure and deadlock/thread-dump analysis.
