package tripService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.AllArgsConstructor;

import java.util.List;

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
    public static class SearchTripsRequest {
        @NotBlank
        private String destination;
        private String startDate;
        private String endDate;
    }

    @Data
    @AllArgsConstructor
    public static class InventoryItem {
        private String hotelId;
        private String hotelName;
        private double price;
        private String photoUrl;
    }

    @Data
    @AllArgsConstructor
    public static class SearchTripsResponse {
        private List<InventoryItem> inventories;
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

