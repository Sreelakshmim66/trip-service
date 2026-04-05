package tripService.controller;

import tripService.dto.TripDtos;
import tripService.service.TripService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    // POST /api/trips/searchTrips
    @PostMapping("/searchTrips")
    public ResponseEntity<TripDtos.SearchTripsResponse> searchTrips(@Valid @RequestBody TripDtos.SearchTripsRequest req) {
        return ResponseEntity.ok(tripService.searchTrips(req));
    }

    // POST /api/trips/createTrip
    @PostMapping("/createTrip")
    public ResponseEntity<TripDtos.CreateTripResponse> createTrip(@Valid @RequestBody TripDtos.CreateTripByHotelRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tripService.createTrip(req));
    }

//    // GET /api/trips/user/{userId}
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<TripDtos.TripResponse>> getTripsByUser(@PathVariable String userId) {
//        return ResponseEntity.ok(tripService.getTripsByUser(userId));
//    }

    // GET /api/trips/{tripId}
    @GetMapping("/{tripId}")
    public ResponseEntity<TripDtos.TripResponse> fetchTripDetailsById(@PathVariable String tripId) {
        return ResponseEntity.ok(tripService.fetchTripDetailsById(tripId));
    }
}
