package tripService.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "trips")
@Data
@NoArgsConstructor
public class Trip {

    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private String tripId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String destination;

    private String userId;

    private String hotelId;

    private String startDate;
    private String endDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
        if (this.tripId == null) {
            this.tripId = UUID.randomUUID().toString();
        }
    }
}
