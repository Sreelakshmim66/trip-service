package tripService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class TripDtos {

    @Data
    public static class CreateTripRequest {
        @NotBlank
        private String name;

        @NotBlank
        private String destination;

        @NotBlank
        private String userId;

        private String startDate;
        private String endDate;
    }

    @Data
    public static class TripResponse {
        private String id;
        private String name;
        private String destination;
        private String userId;
        private String startDate;
        private String endDate;
        private String createdAt;

        public TripResponse(tripService.model.Trip t) {
            this.id          = t.getId();
            this.name        = t.getName();
            this.destination = t.getDestination();
            this.userId      = t.getUserId();
            this.startDate   = t.getStartDate();
            this.endDate     = t.getEndDate();
            this.createdAt   = t.getCreatedAt() != null ? t.getCreatedAt().toString() : null;
        }
    }
}

