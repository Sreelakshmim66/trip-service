# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
mvn clean install

# Run
./mvnw spring-boot:run

# Run tests only
mvn test

# Run a single test class
mvn test -Dtest=TripServiceApplicationTests

# Skip tests during build
mvn clean install -DskipTests

# Compile without running tests
mvn clean compile
```

## Architecture

This is a **Spring Boot 3.5.7 microservice** (Java 17) in a larger travel platform. It runs on port `8082` and sits behind a Eureka service registry.

### Service Dependencies
- **Eureka Server** (`localhost:8761`) — must be running before this service starts
- **User Service** (`localhost:8081`, gRPC on `9091`) — called via gRPC to validate users on trip creation
- **PostgreSQL** (`localhost:5432`, database `Trip-service`, user `postgres`, password `0000`)

### Key Flow: Create Trip
`POST /api/trips/createTrip` → `TripController` → `TripService.createTrip()` → saves to DB → calls `fetchTripDetailsById()` → returns full `TripResponse`

The gRPC user validation in `createTrip` is currently **commented out**. Re-enable it when the User Service gRPC is stable.

### gRPC Setup
The service acts as **both a gRPC server and client**:
- **Server** (port `9092`): `TripGrpcServer` exposes `validateTrip` for other services — proto at `src/main/proto/trip_service.proto`
- **Client**: `UserGrpcClient` calls User Service — proto at `src/main/proto/user.proto` (must stay in sync with user-service repo: `Sreelakshmim66/user-service`)

The `user.proto` in this repo is a copy of the user-service proto. If the user-service proto changes, update `src/main/proto/user.proto` here too, then rebuild to regenerate stubs.

### Data Model
`Trip` entity has two ID fields:
- `id` — internal primary key (UUID, auto-generated in `@PrePersist`)
- `tripId` — business-level identifier (UUID, also auto-generated in `@PrePersist`, unique, returned to clients)

The `fetchTripDetailsById` method looks up by `id` (primary key), not `tripId`.

### All DTOs in one file
`TripDtos.java` contains all request/response classes: `CreateTripByHotelRequest`, `CreateTripResponse`, `TripResponse`, `SearchTripsRequest`, `SearchTripsResponse`, `InventoryItem`.

### Test Configuration
Tests use a separate `src/test/resources/application.properties` that swaps PostgreSQL for H2 in-memory and disables gRPC server (`grpc.server.port=-1`) and Eureka (`eureka.client.enabled=false`) to allow isolated unit testing.

### Security
All endpoints are open (`anyRequest().permitAll()`). CORS is configured to allow all origins. No JWT filter is active in this service — `userId` is passed explicitly in the request body.
