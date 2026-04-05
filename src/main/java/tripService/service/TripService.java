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


    public TripDtos.CreateTripResponse createTrip(TripDtos.CreateTripByHotelRequest req) {

//        boolean userValid = userGrpcClient.validateUser(req.getUserId());
//        if (!userValid) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
//                    "User not found: " + req.getUserId());
//        }



        Trip trip = new Trip();
        trip.setName(req.getHotelName());
        trip.setDestination(req.getHotelName());
        trip.setHotelId(req.getHotelId());
        trip.setStartDate(req.getStartDate());
        trip.setEndDate(req.getEndDate());
        trip.setUserId(req.getUserId());
        Trip saved = tripRepository.save(trip);
        return new TripDtos.CreateTripResponse(saved.getTripId());
    }

//    public List<TripDtos.TripResponse> getTripsByUser(String userId) {
//        return tripRepository.findByUserId(userId)
//                .stream()
//                .map(TripDtos.TripResponse::new)
//                .collect(Collectors.toList());
//    }

    public TripDtos.TripResponse getTripById(String tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));
        return new TripDtos.TripResponse(trip);
    }

    public TripDtos.SearchTripsResponse searchTrips(TripDtos.SearchTripsRequest req) {
        List<TripDtos.InventoryItem> inventories = Arrays.asList(
            new TripDtos.InventoryItem("hotel-001", "The Grand Horizon", 1899.9, "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?auto=format&fit=crop&w=800&q=80"),
            new TripDtos.InventoryItem("hotel-002", "Sunset Bay Resort", 2495.0, "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?auto=format&fit=crop&w=800&q=80"),
            new TripDtos.InventoryItem("hotel-003", "Azure Coast Inn", 1340.0, "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80"),
            new TripDtos.InventoryItem("hotel-004", "Palm Garden Hotel", 2107.5, "https://images.unsplash.com/photo-1571896349842-33c89424de2d?auto=format&fit=crop&w=800&q=80"),
            new TripDtos.InventoryItem("hotel-005", "The Royal Suite", 3990.0, "https://images.unsplash.com/photo-1618773928121-c32242e63f39?auto=format&fit=crop&w=800&q=80"),
            new TripDtos.InventoryItem("hotel-006", "Harbor View Lodge", 1599.9, "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?auto=format&fit=crop&w=800&q=80"),
            new TripDtos.InventoryItem("hotel-007", "Mountain Peak Retreat", 1750.0, "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=800&q=80"),
            new TripDtos.InventoryItem("hotel-008", "Cityscape Boutique", 2200.0, "https://images.unsplash.com/photo-1445019980597-93fa8acb246c?auto=format&fit=crop&w=800&q=80"),
            new TripDtos.InventoryItem("hotel-009", "Oasis Desert Spa", 2990.0, "https://images.unsplash.com/photo-1542621334-a254cf47733d?auto=format&fit=crop&w=800&q=80"),
            new TripDtos.InventoryItem("hotel-010", "Lakeside Haven", 1455.0, "https://images.unsplash.com/photo-1439066615861-d1af74d74000?auto=format&fit=crop&w=800&q=80")
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
