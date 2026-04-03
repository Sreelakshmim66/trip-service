package tripService.service;

import org.springframework.stereotype.Service;
import tripService.dto.TripDtos;
import tripService.model.Trip;
import tripService.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final UserGrpcClient userGrpcClient;   // injected gRPC client

    public TripDtos.TripResponse createTrip(TripDtos.CreateTripRequest req) {
        // Validate userId via gRPC call to user-service
        boolean userValid = userGrpcClient.validateUser(req.getUserId());
        if (!userValid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User not found: " + req.getUserId());
        }

        Trip trip = new Trip();
        trip.setName(req.getName());
        trip.setDestination(req.getDestination());
        trip.setUserId(req.getUserId());
        trip.setStartDate(req.getStartDate());
        trip.setEndDate(req.getEndDate());

        return new TripDtos.TripResponse(tripRepository.save(trip));
    }

    public List<TripDtos.TripResponse> getTripsByUser(String userId) {
        return tripRepository.findByUserId(userId)
                .stream()
                .map(TripDtos.TripResponse::new)
                .collect(Collectors.toList());
    }

    public TripDtos.TripResponse getTripById(String tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));
        return new TripDtos.TripResponse(trip);
    }

    public TripDtos.SearchTripsResponse searchTrips(TripDtos.SearchTripsRequest req) {
        List<TripDtos.InventoryItem> inventories = Arrays.asList(
            new TripDtos.InventoryItem("hotel-001", "The Grand Horizon", 189.99, "https://images.example.com/hotels/grand-horizon.jpg"),
            new TripDtos.InventoryItem("hotel-002", "Sunset Bay Resort", 249.50, "https://images.example.com/hotels/sunset-bay.jpg"),
            new TripDtos.InventoryItem("hotel-003", "Azure Coast Inn", 134.00, "https://images.example.com/hotels/azure-coast.jpg"),
            new TripDtos.InventoryItem("hotel-004", "Palm Garden Hotel", 210.75, "https://images.example.com/hotels/palm-garden.jpg"),
            new TripDtos.InventoryItem("hotel-005", "The Royal Suite", 399.00, "https://images.example.com/hotels/royal-suite.jpg"),
            new TripDtos.InventoryItem("hotel-006", "Harbor View Lodge", 159.99, "https://images.example.com/hotels/harbor-view.jpg"),
            new TripDtos.InventoryItem("hotel-007", "Mountain Peak Retreat", 175.00, "https://images.example.com/hotels/mountain-peak.jpg"),
            new TripDtos.InventoryItem("hotel-008", "Cityscape Boutique", 220.00, "https://images.example.com/hotels/cityscape.jpg"),
            new TripDtos.InventoryItem("hotel-009", "Oasis Desert Spa", 299.00, "https://images.example.com/hotels/oasis-desert.jpg"),
            new TripDtos.InventoryItem("hotel-010", "Lakeside Haven", 145.50, "https://images.example.com/hotels/lakeside-haven.jpg")
        );
        return new TripDtos.SearchTripsResponse(inventories);
    }

    // Used by gRPC server layer
    public boolean validateTrip(String tripId, String userId) {
        // If userId is blank, just check trip exists
        if (userId == null || userId.isBlank()) {
            return tripRepository.existsById(tripId);
        }
        return tripRepository.existsByIdAndUserId(tripId, userId);
    }

    public Trip findById(String tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));
    }
}
