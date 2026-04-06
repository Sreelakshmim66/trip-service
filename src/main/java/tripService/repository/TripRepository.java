package tripService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tripService.model.Trip;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, String> {
    List<Trip> findByUserId(String userId);
    boolean existsByTripIdAndUserId(String tripId, String userId);
}

