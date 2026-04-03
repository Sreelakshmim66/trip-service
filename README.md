# Trip Service

A Spring Boot microservice responsible for managing trips. It exposes REST and GraphQL APIs for trip operations, communicates with the User Service via gRPC for user validation, and registers itself with a Eureka service registry.

---

## Tech Stack

- **Java 17**
- **Spring Boot 3.5.7**
- **Spring Data JPA** — trip persistence
- **Spring Security** — endpoint security
- **gRPC** — inter-service communication with User Service
- **Eureka Client** — service discovery
- **H2** — in-memory database (dev)
- **PostgreSQL** — production database
- **Lombok** — boilerplate reduction

---

## Running the Service

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
| `eureka.client.service-url.defaultZone` | `http://localhost:8761/eureka/` |
| `spring.h2.console.path` | `/h2-console` |

> Ensure a Eureka server is running on port `8761` before starting this service.

---

## REST API

Base path: `/api/trips`

### Create a Trip
**POST** `/api/trips`

Request:
```json
{
    "name": "Europe Getaway",
    "destination": "Paris",
    "userId": "user-123",
    "startDate": "2026-05-01",
    "endDate": "2026-05-10"
}
```

Response `201 Created`:
```json
{
    "id": "e3d2f...",
    "name": "Europe Getaway",
    "destination": "Paris",
    "userId": "user-123",
    "startDate": "2026-05-01",
    "endDate": "2026-05-10",
    "createdAt": "2026-04-03T12:00:00"
}
```

---

### Search Trip Inventories
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
            "price": 189.99,
            "photoUrl": "https://images.example.com/hotels/grand-horizon.jpg"
        }
        // ... 10 items total
    ]
}
```

> Currently returns mocked data.

---

### Get Trips by User
**GET** `/api/trips/user/{userId}`

Response `200 OK`: array of trip objects.

---

### Get Trip by ID
**GET** `/api/trips/{tripId}`

Response `200 OK`: single trip object.

---

## GraphQL API

Endpoint: **POST** `/graphql`

### Schema

```graphql
type Query {
    searchTrips(destination: String!, startDate: String, endDate: String): [Trip!]!
    myTrips(userId: String!): [Trip!]!
    trip(id: String!): Trip
}

type Trip {
    id: ID!
    name: String!
    destination: String!
    userId: String!
    startDate: String
    endDate: String
    createdAt: String
}
```

---

## gRPC

The service exposes a gRPC server on port `9092` and acts as a client to the User Service.

### Server — `TripGrpcService`
- **`validateTrip(tripId, userId)`** — validates that a trip exists and optionally belongs to the given user. Used by other microservices.

### Client — `UserGrpcClient`
- Calls the User Service (discovered via Eureka as `USER-SERVICE`) to validate a user before creating a trip.

---

## H2 Console (dev only)

Access the in-memory database at:
```
http://localhost:8082/h2-console
```
- **JDBC URL:** `jdbc:h2:mem:tripdb`
- **Username:** `sa`
- **Password:** *(empty)*
