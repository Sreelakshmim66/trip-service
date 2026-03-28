package tripService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Trip {

    @Id
    private String tripId;
    private String source;
    private String destination;

    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }
}
