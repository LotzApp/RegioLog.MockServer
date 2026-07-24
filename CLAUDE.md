# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

RegioLog.MockServer is a Spring Boot 3.5.7 (Java 24) mock API server that simulates the RegioLog distribution/supply chain management system. It serves as a test double for client integrations, providing realistic data with configurable delay simulation.

## Build & Run Commands

```bash
# Build
./gradlew build
./gradlew clean build

# Run
./gradlew bootRun

# Test
./gradlew test
```

> Note: The test task is currently commented out in `build.gradle`. The test suite contains only a basic context load test.

**External credentials required** — create `~/.gradle/gradle.properties` (Linux) or `%USERPROFILE%\.gradle\gradle.properties` (Windows):
```properties
nexusUrl=https://repository.lotzapp.work/repository/maven-hosted/
nexusUsername=<username>
nexusPassword=<password>
```

## Database Setup

Start the local MySQL instance via Docker Compose:
```bash
docker-compose -f docker/docker-compose.yml up -d
```

- MySQL runs on port `3306`, phpMyAdmin on port `8080`
- Credentials: user `user` / password `password` / database `app_db`
- The application uses `ddl-auto=create-drop`, so the schema is recreated on every startup
- `DataLoader.java` (CommandLineRunner) populates mock data at startup

The app runs on **port 3131** (`server.port=3131`).

Swagger UI is available at `/swagger`, OpenAPI spec at `/api-docs`.

## Architecture

The codebase follows a strict layered architecture enforced by ArchUnit:

```
Controller → Service → Repository → Entity
```

### Two distinct controller families

1. **`backend/controller/`** — JPA-backed endpoints using the full service/repository/entity/converter stack:
   - `client/` — ClientController, MockClientController (clients, locations, delivery rhythms)
   - `product/` — RegioLogProductController (products, prices, assortments)

2. **`component/`** — Stateless mock components that return in-memory generated data, intercepted by `RequestAspect`:
   - ClientComponent, ProductComponent, OrderComponent, RequestComponent, CategoryComponent, UserComponent, PermissionComponent, InstanceComponent

### Cross-cutting concerns

`RequestAspect.java` (AOP) intercepts all requests to handle auth simulation, logging, and delay injection. `TimeUtils.handleSpecialNames()` reads entity names to inject delays:
- `"Very-Long-Loading-Duration"` → 50 s delay
- `"Loading-Timeout"` → `Long.MAX_VALUE` delay (simulates timeout)
- Default → 200 ms delay

### Converter pattern

All entity↔DTO conversions go through `IConverter<TFrom, TTo>` implementations in `backend/converter/`. Never convert directly in controllers or services.

### Package constraint

The project uses `javax.persistence` — do **not** introduce `jakarta.persistence` imports.

## Key Configuration

`src/main/resources/application.properties` controls:
- `spring.jpa.hibernate.ddl-auto=create-drop` — schema rebuilt on each startup
- `spring.jpa.open-in-view=false` — explicit transaction boundaries required
- CORS is fully open (`WebConfig.addCorsMappings` allows all origins)
