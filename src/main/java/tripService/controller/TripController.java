package tripService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tripService.model.Trip;
import tripService.service.TripService;

@RestController
@RequestMapping("/tripController")
public class TripController {

//    Takes  user
//    create trip id
    @Autowired
    TripService tripService;

    @PostMapping("/createTrip")
    public String createTrip(Trip trip) {
        return tripService.createTrip(trip);
    }

    @PostMapping("/listTrips")
    public String listTrips(String userId) {
        return tripService.listTrips(userId);
    }
}
