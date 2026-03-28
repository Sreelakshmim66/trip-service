package tripService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tripService.model.Trip;

public interface TripRepository extends JpaRepository<Trip, String> {
}
