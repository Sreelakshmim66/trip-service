package tripService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tripService.model.Trip;
import tripService.repository.TripRepository;

@Service
public class TripService {

    @Autowired
    TripRepository tripRepository;

    public String createTrip(Trip trip){
        String tripId = java.util.UUID.randomUUID().toString();
        trip.setTripId(tripId);
        tripRepository.save(trip);
        return "Trip Created with ID: " + tripId;
    }

    public String listTrips(String userId){
        java.util.List<Trip> trips = tripRepository.findAllById(java.util.Collections.singletonList(userId));
        StringBuilder userTrips = new StringBuilder("Trips for User ID " + userId + ":\n");
        for(Trip trip : trips){
                userTrips.append(trip.toString()).append("\n");
        }
        return userTrips.toString();
    }

}
