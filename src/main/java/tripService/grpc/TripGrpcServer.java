package tripService.grpc;

import tripService.service.TripService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class TripGrpcServer extends TripGrpcServiceGrpc.TripGrpcServiceImplBase {

    private final TripService tripService;

    @Override
    public void validateTrip(ValidateTripRequest request,
                             StreamObserver<ValidateTripResponse> responseObserver) {
        boolean valid = tripService.validateTrip(request.getTripId(), request.getUserId());

        String tripName = "";
        if (valid) {
            try {
                tripName = tripService.findById(request.getTripId()).getName();
            } catch (Exception ignored) {}
        }

        ValidateTripResponse response = ValidateTripResponse.newBuilder()
                .setValid(valid)
                .setName(tripName)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
