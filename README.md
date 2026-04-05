# Trip Service

A Spring Boot microservice responsible for managing trips. It exposes REST APIs for trip operations, communicates with the User Service via gRPC for user validation, and registers itself with a Eureka service registry.

---

## Tech Stack

- **Java 17**
- **Spring Boot 3.5.7**
- **Spring Data JPA** — trip persistence
- **Spring Security** — endpoint security
- **gRPC** — inter-service communication with User Service
- **Eureka Client** — service discovery
- **PostgreSQL** — database
- **H2** — in-memory database (test only)
- **Lombok** — boilerplate reduction

---

## Running the Service

### Prerequisites
- PostgreSQL running with a database named `Trip-service`
- Eureka server running on port `8761`
- User Service running on port `8081` (gRPC on `9091`)

### Standard
```bash
./mvnw spring-boot:run
```

### Debug mode (JDWP on port 5005)
```bash
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
```

Attach your IDE's remote debugger to `localhost:5005`.

---

## Configuration

Key properties in `application.properties`:

| Property | Value |
|---|---|
| `server.port` | `8082` |
| `grpc.server.port` | `9092` |
| `spring.datasource.url` | `jdbc:postgresql://localhost:5432/Trip-service` |
| `spring.datasource.username` | `postgres` |
| `spring.datasource.password` | `0000` |
| `spring.jpa.hibernate.ddl-auto` | `update` |
| `eureka.client.service-url.defaultZone` | `http://localhost:8761/eureka/` |

> Hibernate will auto-create the `trips` table on first startup via `ddl-auto=update`.

---

## Database Setup

Create the database in PostgreSQL before starting the service:

```sql
CREATE DATABASE "Trip-service";
```

The `trips` table is created automatically by Hibernate on startup. To create it manually:

```sql
CREATE TABLE trips (
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    trip_id VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    user_id VARCHAR(255),
    hotel_id VARCHAR(255),
    start_date VARCHAR(255),
    end_date VARCHAR(255),
    created_at TIMESTAMP
);
```

---

## REST API

Base path: `/api/trips`

---

### Create Trip by Hotel (Confirm Reservation)
**POST** `/api/trips/createTrip`

Called when a user confirms a hotel reservation. Validates the user via gRPC, saves the trip, and returns a generated `tripId`.

Request:
```json
{
    "hotelName": "The Grand Horizon",
    "hotelId": "hotel-001",
    "startDate": "2026-05-01",
    "endDate": "2026-05-07",
    "userId": "user-123"
}
```

Response `201 Created`:
```json
{
    "tripId": "550e8400-e29b-41d4-a716-446655440000"
}
```

---

### Search Hotels
**POST** `/api/trips/searchTrips`

Request:
```json
{
    "destination": "Paris",
    "startDate": "2026-05-01",
    "endDate": "2026-05-10"
}
```

Response `200 OK`:
```json
{
    "inventories": [
        {
            "hotelId": "hotel-001",
            "hotelName": "The Grand Horizon",
            "price": 1899.9,
            "photoUrl": "https://..."
        }
    ]
}
```

> Currently returns mocked data (10 hotels).

---

### Get Trip by ID
**GET** `/api/trips/{tripId}`

Response `200 OK`:
```json
{
    "id": "e3d2f...",
    "tripId": "550e8400-...",
    "name": "The Grand Horizon",
    "destination": "The Grand Horizon",
    "userId": "user-123",
    "hotelId": "hotel-001",
    "startDate": "2026-05-01",
    "endDate": "2026-05-07",
    "createdAt": "2026-04-03T12:00:00"
}
```

---

## gRPC

The service exposes a gRPC server on port `9092` and acts as a client to the User Service.

### Server — `TripGrpcService`
- **`validateTrip(tripId, userId)`** — validates that a trip exists and optionally belongs to the given user. Used by other microservices.

### Client — `UserGrpcClient`
- Calls the User Service (discovered via Eureka as `USER-SERVICE`, gRPC port `9091`) to validate a user before creating a trip.
- Uses the proto definition from `src/main/proto/user.proto` (must match user-service exactly).

---

## Testing

Tests use an H2 in-memory database and have gRPC/Eureka disabled via `src/test/resources/application.properties`.

```bash
./mvnw test
```
